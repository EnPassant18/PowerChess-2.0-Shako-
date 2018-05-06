let game;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/play");
    UI.drawBoard();

    if (localStorage.create !== undefined) {
        game = new Game(localStorage.create.color, localStorage.create.timeControl);
        $("#playerName").html(localStorage.create.name);
        connection.createGame(
            localStorage.create.color, localStorage.create.name,
            localStorage.create.timeControl, localStorage.create.isPublic)
    } else if (localStorage.join !== undefined) {
        $("#playerName").html(localStorage.create.name);
        connection.joinGame(localStorage.join.id, localStorage.join.name);
    }

    $("#option1").click(() => game.powerSelect(false));
    $("#option2").click(() => game.powerSelect(true));
    $("#draw").click(() => connection.draw());
    $("#resign").click(() => connection.lose(GAME_END_CAUSE.RESIGNATION));
    $(".square").click(click);
    $(".piece").click(click);
});

function debugCreate() {
    $("#playerName").html("Daniel");
    game = new Game(true, 0);
    connection.createGame(true, "Daniel", 0, false);
}

function debugJoin(gameId) {
    $("#playerName").html("Katie");
    connection.joinGame(gameId, "Katie");
}
