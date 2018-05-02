let websocket;
const SOCKET_URL = "";
let GAME_ID;
let PLAYER_ID;
let awaiting = null; // The expected type of the next message, if any

function setupSocket() {
    websocket = new WebSocket(SOCKET_URL);

    websocket.onerror = event => {
        connectionError(event);
    }

    websocket.onmessage = event => {
        $("#error").attr("hidden", "true");
        message = JSON.parse(event.data);
        if (awaiting !== null) {
            if (awaiting === message.type) {
                switch (message.type) {
                    default:
                        console.log("Should not be waiting for message of type " + message.type);
                        break;
                    case MESSAGE.CREATE_GAME:
                        GAME_ID = message.gameId;
                        break;
                    case MESSAGE.ADD_PLAYER:
                        PLAYER_ID = message.playerId;
                        start();
                        break;
                    case MESSAGE.GAME_UPDATE:
                        updates(message.updates);
                        setAction(message.action);
                        if (message.action === ACTION.SELECT_POWER) {
                            powerPrompt(
                                POWER_IMAGE[message.options.rarity][message.options.id1],
                                POWER_IMAGE[message.options.rarity][message.options.id2]
                            )
                        }
                }
            } else {
                connectionError(`Expecting message type ${awaiting}, received ${message.type}`);
            }
        } else {
            switch (message.type) {
                default:
                    connectionError("Unexpected or unrecognized message type: " + message.type);
                    break;
                case MESSAGE.ADD_PLAYER:
                    $("opponentName").html(message.name);
                    start();
                    break;
                case MESSAGE.GAME_OVER:
                    // TODO embellish
                    setAction(ACTION.NONE);
                    gameOver(message.result, message.reason);
                    break
                case MESSAGE.REQUEST_DRAW:
                    $("#drawOffered").attr("hidden", "true"); 
                    break;
                case MESSAGE.GAME_UPDATE:
                    if (message.move !== undefined) {
                        move(message.move.from, message.move.to);
                    }
                    updates(message.updates);
                    setAction(message.action);
                    break;
            }
        }
    }
}

// message: String
// Displays the error banner and logs the error to the console
function connectionError(message) {
    $("#error").attr("hidden", "false");
    console.log(message);
}

// Called when the user attempts to move
function attemptMove(from, to) {
    hold();
    websocket.send(JSON.stringify({
        type: MESSAGE.PLAYER_ACTION,
        action: ACTION.MOVE,
        move: {from: from, to: to}
    }))
}

// Called when the user selects a power
// option: boolean (true = first selected)
function select(option) {
    if (_action !== ACTION.SELECT_POWER) {
        console.log("Error: expected action is not SELECT_POWER");
    } else {
        hold();
        $("#log").attr("hidden", null);
        $("#selection").attr("hidden", "true");
        websocket.send(JSON.stringify({
            type: MESSAGE.PLAYER_ACTION,
            action: ACTION.SELECT_POWER,
            selection: option
        }))
    }
}

// When a player resigns or loses on time
// reason: RESIGNATION (1) / TIME (2)
function lose(reason) {
    setAction(ACTION.NONE);
    websocket.send(JSON.stringify({
        type: MESSAGE.GAME_OVER,
        result: GAME_RESULT
        selection: option
    }))
}