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

// Teleports a piece (jQuery) to a given Square
function teleport(piece, square) {
    piece.offset({
        top: boardBox.top + SQUARE_SIZE * square.row,
        left: boardBox.left + SQUARE_SIZE * square.col
    });
}

// Removes all pieces and powers from the given square
function clear(square) {
    $("#pieces > img").each((index, element) => {
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
}