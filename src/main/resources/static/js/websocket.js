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
            connectionError(event);
        }

        this.socket.onmessage = event => {
            $("#error").attr("hidden");
            const message = JSON.parse(event.data);
            console.log("Received: " + message.type);
            switch (message.type) {
            default:
                connectionError("Unexpected or unrecognized message type: " + message.type);
                break;
            case MESSAGE.CREATE_GAME:
                this.GAME_ID = message.gameId;
                this.PLAYER_ID = message.playerId;
                break;
            case MESSAGE.JOIN_GAME:
                if (message.name !== undefined) {
                    $("opponentName").html(message.name);
                } else {
                    this.PLAYER_ID = message.playerId;
                    game = new Game(message.color, message.timeControl);
                }
                game.start();
                break;
            case MESSAGE.GAME_OVER:
                // TODO embellish
                gameOver(message.result, message.reason);
                break;
            case MESSAGE.REQUEST_DRAW:
                $("#drawOffered").removeAttr("hidden");
                break;
            case MESSAGE.GAME_UPDATE:
                if (message.move !== undefined) {
                    move(new Move(message.move.from, message.move.to).adjusted());
                }
                UI.clearPowers();
                UI.updates(message.updates);
                game.action = message.action;
                if (message.action === ACTION.SELECT_POWER) {
                    game.powerPrompt(
                        POWER_OBJECT[message.options.rarity][message.options.id1],
                        POWER_OBJECT[message.options.rarity][message.options.id2]
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

    joinGame(id, name) {
        this.GAME_ID = id;
        this.socket.send(JSON.stringify({
            gameId: this.GAME_ID,
            type: MESSAGE.JOIN_GAME,
            name: name
        }));
    }

    // Called when the user attempts to move
    attemptMove(move) {
        this.socket.send(JSON.stringify({
            gameId: this.GAME_ID,
            playerId: this.PLAYER_ID,
            type: MESSAGE.PLAYER_ACTION,
            action: ACTION.MOVE,
            move: move.adjusted()
        }))
    }

    // Called when the user selects a power
    // option: boolean (true = first selected)
    // followUpObject: contains followUp action result
    usePower(option, followUpObject) {
        let message = {
            gameId: this.GAME_ID,
            playerId: this.PLAYER_ID,
            type: MESSAGE.PLAYER_ACTION,
            action: ACTION.SELECT_POWER,
            selection: option
        }
        if (followUpObject !== undefined) { message.followUp = followUpObject.adjusted() }
        this.socket.send(JSON.stringify(message));
    }

    // When a player resigns or loses on time
    // reason: RESIGNATION (1) / TIME (2)
    lose(reason) {
        this.socket.send(JSON.stringify({
            gameId: this.GAME_ID,
            playerId: this.PLAYER_ID,
            type: MESSAGE.GAME_OVER,
            result: GAME_RESULT,
            reason: reason
        }));
        game.gameOver(GAME_RESULT.LOSS, reason);
    }

    // When a player offers a draw
    draw() {
        this.socket.send(JSON.stringify({
            gameId: this.GAME_ID,
            playerId: this.PLAYER_ID,
            type: MESSAGE.REQUEST_DRAW
        }));
    }
}
