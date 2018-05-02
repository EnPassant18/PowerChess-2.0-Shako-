let COLOR;

// Represents square on board, where TOP-LEFT IS (0, 0)
class Square {
    constructor(row, col) {
        this.row = row;
        this.col = col;
    }
    adjusted() { return new AdjustedSquare(row, col) }
}

// Represents square on board, where BOTTOM-LEFT IS (1, 1)
class AdjustedSquare extends Square {
    constructor(row, col) {
        super(8 - row, 1 + col);
    }
}

// piece: jQuery element
// Teleports a piece (or box) to a given Square
function teleport(piece, square) {
    piece.offset({
        top: boardBox.top + SQUARE_SIZE * square.row,
        left: boardBox.left + SQUARE_SIZE * square.col
    });
}

// Removes all pieces and powers from the given square
function clear(square) {
    $("#board img").each((index, element) => {
        if (JSON.stringify($(element).offset()) === JSON.stringify({
            top: boardBox.top + SQUARE_SIZE * square.row,
            left: boardBox.left + SQUARE_SIZE * square.col
        })) {
            $(element).remove();
        }
    });
}

// Creates a piece on the given square
function spawn(imageUrl, square) {
    piece = $(`<img class="piece" src="${imageUrl}" draggable=false />`);
    $("#pieces").append(piece);
    teleport(piece, square);
    piece.on("mousedown", dragStart);
}

// Creates a power box on the given square
function spawnBox(imageUrl, square) {
    box = $(`<img class="power" src="${imageUrl}" draggable=false />`);
    $("#powers").append(box);
    teleport(box, square);
}

// Animates the movement of a piece from the given Square to the given Square
function move(from, to) {
    let piece;
    $("#pieces > img").each((index, element) => {
        if (JSON.stringify($(element).offset()) === JSON.stringify({
            top: boardBox.top + SQUARE_SIZE * from.row,
            left: boardBox.left + SQUARE_SIZE * from.col
        })) {
            piece = $(element);
        }
    });
    if (piece === undefined) {
        console.log("No piece on given start square");
    } else {
        let frame = 1;
        const FRAMES = 100;
        const xStart = piece.offset().left;
        const yStart = piece.offset().top;
        const xDiff = SQUARE_SIZE * (to.col - from.col) / FRAMES;
        const yDiff = SQUARE_SIZE * (to.row - from.row) / FRAMES;
        const interval = setInterval(() => {
            if (frame === FRAMES) {
                clearInterval(interval);
                teleport(piece, to);
            } else {
                piece.offset({
                    left: xStart + frame * xDiff,
                    top: yStart + frame * yDiff
                })
                frame++;
            }
        })
    }
}

// update: {row:..., col:..., state:..., piece/color/rarity:...}
function update(update) {
    const square = new Square(update.row, update.col);
    clear(square);
    switch (update.state) {
        default: 
            console.log("Invalid update, unrecognized state: " + update.state);
            break;
        case ENTITY.NOTHING: break;
        case ENTITY.PIECE:
            spawn(PIECE_IMAGE[update.color][update.piece], square);
            break;
        case ENTITY.POWER:
            spawnBox(BOX_IMAGE[update.rarity], square);
            break;
        case ENTITY.OTHER: break;
    }
}

// Performs a list of updates
function updates(updates) {
    for (let i = 0; i < updates.length; i++) {
        update(updates[i]);
    }
}

// Prompts the user to select between two powers
function powerPrompt(imageUrl1, imageUrl2) {
    if (_action !== ACTION.SELECT_POWER) {
        console.log("Error: expected action is not SLECT_POWER");
    } else {
        $("#log").attr("hidden", "true");
        $("#selection").attr("hidden", null);
        $("#option1").attr("src", imageUrl1);
        $("#option2").attr("src", imageUrl2);
    }
}

// Displays a popup when the game ends
function gameOver(result, reason) {
    switch (result) {
        case GAME_RESULT.WIN: alert("You win"); break;
        case GAME_RESULT.LOSS: alert("You lose"); break;
        case GAME_RESULT.DRAW: alert("Game drawn"); break;
    }
}

// DO NOT MUTATE
// What the player is expected to do next (if anything): e.g. NONE, MOVE, SELECT_POWER
let _action = ACTION.NONE; 

// DO NOT MUTATE
// True when waiting for the server
let _hold = false;

function setAction(newAction) {
    resume();
    _action = newAction;
}

function hold() {
    _hold = true;
}

function resume() {
    _hold = false;
}