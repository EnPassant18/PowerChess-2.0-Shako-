let moving;

// Contains information about the piece being moved
class Moving {
    constructor(piece, x, y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.startSquare = this.getSquare();
    }
    // Returns the square the piece is being dragged over, 
    // or null if it's outside the board
    getSquare() {
        const row = Math.floor((this.y - UI.boardBox.top) / UI.SQUARE_SIZE);
        const col = Math.floor((this.x - UI.boardBox.left) / UI.SQUARE_SIZE);
        if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            return new Square(row, col);
        } else {
            return null;
        }
    }
}

// When user clicks on a piece (event: MouseEvent)
function dragStart(event) {
    if (game.action === ACTION.MOVE
        || (game.action === ACTION.MOVE_THIS 
            && event.target === game.mustMove)) {
        moving = new Moving(
            $(event.target),
            event.pageX,
            event.pageY
        );
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
    const moveEnd = moving.getSquare();
    UI.teleport(moving.piece, moveEnd);
    moving.piece.css("z-index", 2);
    $(document).off("mousemove");
    $(document).off("mouseup");
    if (JSON.stringify(moving.startSquare) !== JSON.stringify(moveEnd)) {
        if (game.selected !== null) {
            game.powerFollowUp(new Move(moving.startSquare, moveEnd));
        } else {
            connection.attemptMove(new Move(moving.startSquare, moveEnd)); 
        }
    }
    moving = null;
}

// When user clicks on a square
function click(event) {

}