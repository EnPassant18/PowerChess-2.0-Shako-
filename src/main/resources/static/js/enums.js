MESSAGE = {
    CREATE_GAME: 0,
    ADD_PLAYER: 1,
    GAME_OVER: 2,
    OFFER_DRAW: 3,
    DRAW_OFFERED: 4,
    PLAYER_ACTION: 5,
    GAME_UPDATE: 6,
    ILLEGAL_ACTION: 7,
    ERROR: 8
}

COLOR = {
    WHITE: true,
    BLACK: false
}

GAME_END_CAUSE = {
    MATE: 0, 
    RESIGNATION: 1,
    TIME: 2, 
    DRAW_AGREED: 3
}

GAME_RESULT = {
    WIN: 0,
    LOSS: 1,
    DRAW: 2
}

RARITY = {
    COMMON: 0,
    RARE: 1,
    LEGENDARY: 2
}

PIECE = {
    NOTHING: 0,
    KING: 1,
    QUEEN: 2,
    ROOK: 3,
    BISHOP: 4,
    KNIGHT: 5,
    PAWN: 6
}

PIECE_IMAGE = {
    WHITE: {
        KING: "images/pieces/white-king.png",
        QUEEN: "images/pieces/white-queen.png",
        ROOK: "images/pieces/white-rook.png",
        BISHOP: "images/pieces/white-bishop.png",
        KNIGHT: "images/pieces/white-knight.png",
        PAWN: "images/pieces/white-pawn.png",
    },
    BLACK: {
        KING: "images/pieces/black-king.png",
        QUEEN: "images/pieces/black-queen.png",
        ROOK: "images/pieces/black-rook.png",
        BISHOP: "images/pieces/black-bishop.png",
        KNIGHT: "images/pieces/black-knight.png",
        PAWN: "images/pieces/black-pawn.png",
    }
}