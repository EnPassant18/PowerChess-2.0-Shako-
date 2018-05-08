class Connection {
    constructor(url) {
        this.url = url;
        try {
            this.socket = new WebSocket(url);
            this._setup();
        } catch (error) {
            this.connectionError(error);
        }
    }

    _setup() {
        this.socket.onopen = () => {
            this.ping = setInterval(() => {
                this.socket.send("");
            }, 300);
        }

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
                $("#darkVeil").removeAttr("hidden");
                $("#invite").removeAttr("hidden");
                $("#inviteLink").html("sako.localtunnel.me/game.html?id=" + message.gameId);
                this.GAME_ID = message.gameId;
                this.PLAYER_ID = message.playerId;
                break;
            case MESSAGE.JOIN_GAME:
                $("#darkVeil").attr("hidden", "true");
                $("#invite").attr("hidden", "true");
                $("#opponentName").html(message.name);
                if (message.playerId !== undefined) {
                    this.PLAYER_ID = message.playerId;
                    game = new Game(message.color, message.timeControl);
                }
                game.start();
                break;
            case MESSAGE.GAME_OVER:
                game.gameOver(message.result, message.reason);
                break;
            case MESSAGE.REQUEST_DRAW:
                drawSound.play();
                $("#drawOffered").removeAttr("hidden");
                break;
            case MESSAGE.GAME_UPDATE:
                let moveDelay;
                game.action = message.action;
                if (moving !== null) {
                    game.lastMoved = moving.toSquare;
                    UI.clear(moving.toSquare);
                    UI.teleport(moving.piece, moving.toSquare);
                    moving = null;
                    moveSound.play();
                }
                if (message.move !== undefined) {
                    UI.move(new Move(
                        new Square(message.move.from.row, message.move.from.col),
                        new Square(message.move.to.row, message.move.to.col))
                        .adjusted(), message.updates);
                } else {
                    UI.updates(message.updates);
                }
                UI.clearPowers();
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
        console.log({
            gameId: this.GAME_ID,
            playerId: this.PLAYER_ID,
            type: MESSAGE.PLAYER_ACTION,
            action: ACTION.MOVE,
            move: move.adjusted()
        });
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
        if (followUpObject !== undefined) {
            message.followUp = followUpObject.adjusted();
        }
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
        drawSound.play();
        this.socket.send(JSON.stringify({
            gameId: this.GAME_ID,
            playerId: this.PLAYER_ID,
            type: MESSAGE.REQUEST_DRAW
        }));
    }

    spawn(row, col, rarity) {
      this.socket.send(JSON.stringify({
        gameId: this.GAME_ID,
        type: MESSAGE.SPAWN,
        row: row,
        col: col,
        rarity: RARITY[rarity]
      }))
    }

    give(row, col, pow1, pow2, rarity) {
      let r = RARITY[rarity];
      this.socket.send(JSON.stringify({
        gameId: this.GAME_ID,
        type: MESSAGE.GIVE,
        id1: POWER[r][pow1],
        id2: POWER[r][pow2],
        whereCaptured:
          {
            row: row,
            col: col,
          },
        rarity: r
      }))
    }

}
