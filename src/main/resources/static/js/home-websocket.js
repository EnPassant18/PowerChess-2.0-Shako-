class Connection {

    constructor(url) {
        try {
            this.socket = new WebSocket(url);
            this._setup();
        } catch (error) {
            this.connectionError(error);
        }
    }

    _setup() {
        this.socket.onerror = event => {
            this.connectionError(event);
        }

        this.socket.onmessage = event => {
            $("#error").attr("hidden", "true");
            const message = JSON.parse(event.data);
            console.log(message);
            switch (message.type) {
            default:
                this.connectionError("Unexpected or unrecognized message type: " + message.type);
                break;
            case MESSAGE.ALL_GAMES:
                render(message.games);
                break;
            case MESSAGE.ADD_GAME:
                addGame(
                    message.gameId, 
                    {name: message.name, color: message.color, timeControl: message.timeControl},
                    $("#right"))
                break;
            case MESSAGE.REMOVE_GAME:
                removeGame(message.gameId);
                break;
          }
      }
  }

    // message: String
    // Displays the error banner and logs the error to the console
    connectionError(message) {
        $("#error").removeAttr("hidden");
        console.log(message);
    }

}

MESSAGE = {
    ALL_GAMES: 0,
    ADD_GAME: 1,
    REMOVE_GAME: 2
}
