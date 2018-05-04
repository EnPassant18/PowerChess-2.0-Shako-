MESSAGE = {
    CREATE_GAME: 0,
    JOIN_GAME: 1,
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
    MOVE_THIS: 4
}

COLOR = {
    WHITE: true,
    BLACK: false
}

PLAYER = {
    PLAYER: true,
    OPPONENT: false
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

POWER_OBJECT = {
    0: {
        0: new Power("images/powers/Adjust.png", ACTION.MOVE_THIS),
        1: new Power("images/powers/Rewind.png", ACTION.NONE),
        2: new Power("images/powers/Second Effort.png", ACTION.MOVE_THIS),
        3: new Power("images/powers/Shield.png", ACTION.NONE),
        4: new Power("images/powers/Swap.png", ACTION.SELECT_SQUARE)
    },
    1: {
        0: new Power("images/powers/Black Hole.png", ACTION.SELECT_SQUARE),
        1: new Power("images/powers/Energize.png", ACTION.NONE),
        2: new Power("images/powers/Eye for an Eye.png", ACTION.SELECT_SQUARE),
        3: new Power("images/powers/Safety Net.png", ACTION.NONE),
        4: new Power("images/powers/Send Away.png", ACTION.SELECT_SQUARE)
    },
    2: {
        0: new Power("images/powers/Armageddon.png", ACTION.NONE),
        1: new Power("images/powers/Awaken.png", ACTION.NONE),
        2: new Power("images/powers/Clone.png", ACTION.NONE),
        3: new Power("images/powers/Reanimate.png", ACTION.NONE)
    }
}