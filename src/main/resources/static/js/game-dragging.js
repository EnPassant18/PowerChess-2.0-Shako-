let moving = null;

// Contains information about the piece being moved
class Moving {
    constructor(piece, x, y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.startSquare = getSquare(x, y);
    }
}

// Returns the square the piece is being dragged over, 
// or null if it's outside the board
function getSquare(x, y) {
    const row = Math.floor((y - UI.boardBox.top) / UI.SQUARE_SIZE);
    const col = Math.floor((x - UI.boardBox.left) / UI.SQUARE_SIZE);
    if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
        return new Square(row, col);
    } else {
        return null;
    }
}

// When user clicks on a piece (event: MouseEvent)
function dragStart(event) {
    wantsToMove = new Moving(
        $(event.target),
        event.pageX,
        event.pageY
    );
    if (game.action === ACTION.MOVE
        || (game.action === ACTION.MOVE_THIS 
            && JSON.stringify(wantsToMove.startSquare) === JSON.stringify(game.lastMoved))) {
        moving = wantsToMove;
        $(event.target).css("z-index", 3);
        $(document).on("mousemove", drag);
        $(document).on("mouseup", drop);
    }
}

// When user drags a piece
function drag(event) {
    const dX = moving.x - event.pageX;
    const dY = moving.y - event.pageY;
    moving.x = event.pageX;
    moving.y = event.pageY;
    moving.piece.offset({
        top: (moving.piece.offset().top - dY),
        left: (moving.piece.offset().left - dX)
    });
}

// When user drops a piece
function drop(event) {
    moving.toSquare = getSquare(moving.x, moving.y);
    moving.piece.css("z-index", 2);
    $(document).off("mousemove");
    $(document).off("mouseup");
    if (JSON.stringify(moving.startSquare) !== JSON.stringify(moving.toSquare)) {
        if (game.selected !== null) {
            game.powerFollowUp(new Move(moving.startSquare, moving.toSquare));
        } else {
            connection.attemptMove(new Move(moving.startSquare, moving.toSquare)); 
        }
    } else {
        UI.teleport(moving.piece, moving.startSquare);
        moving = null;
    }
}

// When user clicks on a square
function click(event) {
    if (game.action === ACTION.SELECT_SQUARE) {
        game.powerFollowUp(getSquare(event.pageX, event.pageY));
    }
}