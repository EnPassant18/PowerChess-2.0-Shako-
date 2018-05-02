const SQUARE_SIZE = 75;
const GAP_BETWEEN = SQUARE_SIZE / 5;
let boardBox;

$(document).ready(() => {
    $board = $("#board");
    $board.css({
        width: SQUARE_SIZE*8 + "px",
        height: SQUARE_SIZE*8 + "px"
    });
    for (let i = 0; i < 8; i++) {
        for (let j = 0; j < 8; j++) {
            color = (i + j) % 2 === 1 ? "black" : "white";
            $board.append(`<div id="row${i}col${j}" class="square ${color}"
            style="width:${SQUARE_SIZE}px; height:${SQUARE_SIZE}px;
            top:${SQUARE_SIZE*i}px; left:${SQUARE_SIZE*j}px"></div>`);
        }
    }
    $("#sidebar").css({
        width: SQUARE_SIZE*3 + "px",
        height: SQUARE_SIZE*8 + "px",
        "margin-left": SQUARE_SIZE*8 + GAP_BETWEEN + "px"
    })
    $("#container").css("width", SQUARE_SIZE*12 + GAP_BETWEEN + "px");

    boardBox = $board[0].getBoundingClientRect();

    $("#option1").click(() => select(true));
    $("#option2").click(() => select(false));

    spawnStart();

    setupSocket();
});

function spawnStart() {
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.ROOK], new Square(0,0));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.KNIGHT], new Square(0,1));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.BISHOP], new Square(0,2));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.QUEEN], new Square(0,3));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.KING], new Square(0,4));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.BISHOP], new Square(0,5));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.KNIGHT], new Square(0,6));
    spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.ROOK], new Square(0,7));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.ROOK], new Square(7,0));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.KNIGHT], new Square(7,1));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.BISHOP], new Square(7,2));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.QUEEN], new Square(7,3));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.KING], new Square(7,4));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.BISHOP], new Square(7,5));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.KNIGHT], new Square(7,6));
    spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.ROOK], new Square(7,7));
    for (let col in [0, 1, 2, 3, 4, 5, 6, 7]) {
        spawn(PIECE_IMAGE[COLOR.BLACK][PIECE.PAWN], new Square(1,col));
    }
    for (let col in [0, 1, 2, 3, 4, 5, 6, 7]) {
        spawn(PIECE_IMAGE[COLOR.WHITE][PIECE.PAWN], new Square(6,col));
    }
}