// Represents square on board, where TOP-LEFT IS (0, 0)
class Square {
    constructor(row, col) {
        this.row = row;
        this.col = col;
    }
    adjusted() { 
        if (game.color) {
            return new Square(7 - this.row, this.col);
        } else {
            return new Square(this.row, 7 - this.col);
        }
    }
}

class Move {
    constructor(from, to) {
        this.from = from;
        this.to = to;
    }
    adjusted() {
        return new Move(this.from.adjusted(), this.to.adjusted());
    }
}