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
            case MESSAGE.CREATE_GAME:
                this.GAME_ID = message.gameId;
                this.PLAYER_ID = message.playerId;
                break;
            case MESSAGE.JOIN_GAME:
                $("#opponentName").html(message.name);
                if (message.playerId !== undefined) {
                    this.PLAYER_ID = message.playerId;
                    game = new Game(message.color, message.timeControl);
                }
                game.start();
                break;
            case MESSAGE.GAME_OVER:
                // TODO embellish
                game.gameOver(message.result, message.reason);
                break;
            case MESSAGE.REQUEST_DRAW:
                $("#drawOffered").removeAttr("hidden");
                break;
            case MESSAGE.GAME_UPDATE:
                let moveDelay;
                if (message.move !== undefined) {
                    UI.move(new Move(
                        new Square(message.move.from.row, message.move.from.col),
                        new Square(message.move.to.row, message.move.to.col))
                        .adjusted());
                }
                game.action = message.action;
                if (moving !== null) {
                    game.lastMoved = moving.toSquare;
                    UI.clear(moving.toSquare);
                    UI.teleport(moving.piece, moving.toSquare);
                    moving = null;
                }
                UI.clearPowers();
                UI.updates(message.updates);
                if (message.action === ACTION.SELECT_POWER) {
                    game.powerPrompt(
                        POWER_OBJECT[message.rarity][message.id1],
                        POWER_OBJECT[message.rarity][message.id2]
                    )
                }
                break;
            case MESSAGE.ILLEGAL_ACTION:
                if (moving !== null) {
                    UI.teleport(moving.piece, moving.startSquare);
                }
            }
        }
    }

    // message: String
    // Displays the error banner and logs the error to the console
    connectionError(message) {
        $("#error").removeAttr("hidden");
        console.log(message);
    }

    createGame(color, name, timeControl, isPublic) {
        this.socket.send(JSON.stringify({
            type: MESSAGE.CREATE_GAME,
            color: color,
            name: name,
            timeControl: timeControl,
            public: isPublic
        }));
    }

}