let moving = null;

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
        const row = Math.floor((this.y - boardBox.top) / SQUARE_SIZE);
        const col = Math.floor((this.x - boardBox.left) / SQUARE_SIZE);
        if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            return new Square(row, col);
        } else {
            return null
        }
    }
}

// When user clicks on a piece (event: MouseEvent)
function dragStart(event) {
    if (action === ACTION.MOVE && !_hold) {
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
    teleport(moving.piece, moveEnd);
    moving.piece.css("z-index", 2);
    moving = null;
    $(document).off("mousemove");
    $(document).off("mouseup");
    attemptMove(moving.startSquare, moveEnd);
}