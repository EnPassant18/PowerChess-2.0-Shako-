MESSAGE = {
    CREATE_GAME: 0,
    ADD_PLAYER: 1,
    GAME_OVER: 2,
    REQUEST_DRAW: 3,
    PLAYER_ACTION: 4,
    GAME_UPDATE: 5,
    ILLEGAL_ACTION: 6,
    ERROR: 7
}

ACTION = {
    NONE: 0,
    MOVE: 1,
    SELECT_POWER: 2,
    SELECT_SQUARE: 3,
    SELECT_PIECE: 4,
    MOVE_THIS: 5
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

ENTITY = {
    NOTHING: 0,
    PIECE: 1,
    POWER: 2,
    OTHER: 3
}

RARITY = {
    COMMON: 0,
    RARE: 1,
    LEGENDARY: 2
}

PIECE = {
    KING: 0,
    QUEEN: 1,
    ROOK: 2,
    BISHOP: 3,
    KNIGHT: 4,
    PAWN: 5
}

BOX_IMAGE = {
    0: "images/boxes/CommonBox.png",
    1: "images/boxes/RareBox.png",
    2: "images/boxes/LegendaryBox.png"
}

PIECE_IMAGE = {
    true: {
        0: "images/pieces/white-king.png",
        1: "images/pieces/white-queen.png",
        2: "images/pieces/white-rook.png",
        3: "images/pieces/white-bishop.png",
        4: "images/pieces/white-knight.png",
        5: "images/pieces/white-pawn.png",
    },
    false: {
        0: "images/pieces/black-king.png",
        1: "images/pieces/black-queen.png",
        2: "images/pieces/black-rook.png",
        3: "images/pieces/black-bishop.png",
        4: "images/pieces/black-knight.png",
        5: "images/pieces/black-pawn.png",
    }
}

POWER = {
    0: {
        ADJUST: 0,
        REWIND: 1,
        SECOND_EFFORT: 2,
        SHIELD: 3,
        SWAP: 4
    },
    1: {
        BLACK_HOLE: 0,
        ENERGIZE: 1,
        EYE_FOR_AN_EYE: 2,
        SAFETY_NET: 3,
        SEND_AWAY: 4
    },
    2: {
        ARMAGEDDON: 0,
        AWAKEN: 1,
        CLONE: 2,
        REANIMATE: 3
    }
}

POWER_IMAGE = {
    0: {
        0: "images/powers/Adjust.png",
        1: "images/powers/Rewind.png",
        2: "images/powers/Second Effort.png",
        3: "images/powers/Shield.png",
        4: "images/powers/Swap.png"
    },
    1: {
        0: "images/powers/Black Hole.png",
        1: "images/powers/Energize.png",
        2: "images/powers/Eye for an Eye.png",
        3: "images/powers/Safety Net.png",
        4: "images/powers/Send Away.png"
    },
    2: {
        0: "images/powers/Armageddon.png",
        1: "images/powers/Awaken.png",
        2: "images/powers/Clone.png",
        3: "images/powers/Reanimate.png"
    }
}