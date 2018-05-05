class UI {

    static drawGame() {
        const $board = $("#board");
        $board.css({
            width: UI.SQUARE_SIZE*8 + "px",
            height: UI.SQUARE_SIZE*8 + "px"
        });
        for (let i = 0; i < 8; i++) {
            for (let j = 0; j < 8; j++) {
                const color = (i + j) % 2 === 1 ? "black" : "white";
                $board.append(`<div id="row${i}col${j}" class="square ${color}"
                style="width:${UI.SQUARE_SIZE}px; height:${UI.SQUARE_SIZE}px;
                top:${UI.SQUARE_SIZE*i}px; left:${UI.SQUARE_SIZE*j}px"></div>`);
            }
        }
        $("#sidebar").css({
            width: UI.SQUARE_SIZE*3 + "px",
            height: UI.SQUARE_SIZE*8 + "px",
            "margin-left": UI.SQUARE_SIZE*8 + UI.GAP_BETWEEN + "px"
        })
        $("#container").css("width", UI.SQUARE_SIZE*12 + UI.GAP_BETWEEN + "px");
    
        UI.boardBox = $board[0].getBoundingClientRect();

        UI.drawBoard();
    }

    static drawBoard() {
        let first, second, seventh, eighth;
        if (game.color === COLOR.WHITE) {
            first = 7;
            second = 6;
            seventh = 1;
            eighth = 0;
        } else if (game.color === COLOR.BLACK) {
            first = 0;
            second = 1;
            seventh = 6;
            eighth = 7;
        }
        UI.spawn(COLOR.BLACK, PIECE.ROOK, new Square(eighth,0));
        UI.spawn(COLOR.BLACK, PIECE.KNIGHT, new Square(eighth,1));
        UI.spawn(COLOR.BLACK, PIECE.BISHOP, new Square(eighth,2));
        UI.spawn(COLOR.BLACK, PIECE.QUEEN, new Square(eighth,3));
        UI.spawn(COLOR.BLACK, PIECE.KING, new Square(eighth,4));
        UI.spawn(COLOR.BLACK, PIECE.BISHOP, new Square(eighth,5));
        UI.spawn(COLOR.BLACK, PIECE.KNIGHT, new Square(eighth,6));
        UI.spawn(COLOR.BLACK, PIECE.ROOK, new Square(eighth,7));
        UI.spawn(COLOR.WHITE, PIECE.ROOK, new Square(first,0));
        UI.spawn(COLOR.WHITE, PIECE.KNIGHT, new Square(first,1));
        UI.spawn(COLOR.WHITE, PIECE.BISHOP, new Square(first,2));
        UI.spawn(COLOR.WHITE, PIECE.QUEEN, new Square(first,3));
        UI.spawn(COLOR.WHITE, PIECE.KING, new Square(first,4));
        UI.spawn(COLOR.WHITE, PIECE.BISHOP, new Square(first,5));
        UI.spawn(COLOR.WHITE, PIECE.KNIGHT, new Square(first,6));
        UI.spawn(COLOR.WHITE, PIECE.ROOK, new Square(first,7));
        for (let col in [0, 1, 2, 3, 4, 5, 6, 7]) {
            UI.spawn(COLOR.BLACK, PIECE.PAWN, new Square(seventh,col));
        }
        for (let col in [0, 1, 2, 3, 4, 5, 6, 7]) {
            UI.spawn(COLOR.WHITE, PIECE.PAWN, new Square(second,col));
        }
    }

    // piece: jQuery element
    // Teleports a piece (or box) to a given Square
    static teleport(piece, square) {
        piece.offset({
            top: UI.boardBox.top + UI.SQUARE_SIZE * square.row,
            left: UI.boardBox.left + UI.SQUARE_SIZE * square.col
        });
    }

    // Removes all pieces and powers from the given square
    static clear(square) {
        $("#board img").each((index, element) => {
            if (JSON.stringify($(element).offset()) === JSON.stringify({
                top: UI.boardBox.top + UI.SQUARE_SIZE * square.row,
                left: UI.boardBox.left + UI.SQUARE_SIZE * square.col
            })) {
                $(element).remove();
            }
        });
    }

    // Creates a piece on the given square
    static spawn(color, type, square) {
        const piece = $(`<img class="piece" src="${PIECE_IMAGE[color][type]}" draggable=false />`);
        $("#pieces").append(piece);
        UI.teleport(piece, square);
        if (color === game.color) {
            piece.on("mousedown", dragStart);
        }
    }

    // Creates a power box on the given square
    static spawnBox(imageUrl, square) {
        box = $(`<img class="power" src="${imageUrl}" draggable=false />`);
        $("#powers").append(box);
        UI.teleport(box, square);
    }

    // Animates the movement of a piece from the given Square to the given Square
    static move(move) {
        let piece;
        $("#pieces > img").each((index, element) => {
            if (JSON.stringify($(element).offset()) === JSON.stringify({
                top: UI.boardBox.top + UI.SQUARE_SIZE * move.from.row,
                left: UI.boardBox.left + UI.SQUARE_SIZE * move.from.col
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
            const xDiff = UI.SQUARE_SIZE * (move.to.col - move.from.col) / FRAMES;
            const yDiff = UI.SQUARE_SIZE * (move.to.row - move.from.row) / FRAMES;
            const interval = setInterval(() => {
                if (frame === FRAMES) {
                    clearInterval(interval);
                    teleport(piece, move.to);
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
    static update(update) {
        const square = new Square(update.row, update.col).adjusted();
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
    static updates(updates) {
        for (let i = 0; i < updates.length; i++) {
            update(updates[i]);
        }
    }

    // Prompts the user to select between two powers
    static showPowers(image1, image2) {
        if (game.action !== ACTION.SELECT_POWER) {
            console.error("Error: expected action is not SELECT_POWER");
        } else {
            $("#log").attr("hidden", "true");
            $("#selection").attr("hidden", null);
            $("#option1").attr("src", image1);
            $("#option2").attr("src", image2);
        }
    }

    // option: boolean (true = first)
    // isSelected: boolean
    static highlightPower(option, isSelected) {
        const element = option ? $("#option1") : $("#option2");
        if (isSelected) {
            element.css("background-color", "rgb(188, 188, 188)");
        } else {
            element.css("background-color", "");
        } 
    }

    static clearPowers() {
        $("#log").removeAttr("hidden");
        $("#selection").attr("hidden");
        UI.highlightPower(true, false);
        UI.highlightPower(false, false);
    }
}

UI.SQUARE_SIZE = 75;
UI.GAP_BETWEEN = UI.SQUARE_SIZE / 5;