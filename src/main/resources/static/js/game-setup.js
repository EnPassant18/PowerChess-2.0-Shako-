let game;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/play");
    UI.drawBoard();

    if (sessionStorage.create !== undefined) {
        create = JSON.parse(sessionStorage.create);
        game = new Game(create.color, create.timeControl);
        $("#playerName").html(create.name);
        connection.socket.onopen = () => connection.createGame(
            create.color, create.name,
            create.timeControl, create.isPublic);
    } else if (sessionStorage.join !== undefined) {
        join = JSON.parse(sessionStorage.join);
        $("#playerName").html(join.name);
        connection.socket.onopen = () => connection.joinGame(join.id, join.name);
    } else {
        connection.connectionError("No game info");
    }

    $("#option1").click(() => game.powerSelect(false));
    $("#option2").click(() => game.powerSelect(true));
    $("#draw").click(() => connection.draw());
    $("#resign").click(() => connection.lose(GAME_END_CAUSE.RESIGNATION));
    $(".square").click(click);
});

function debugCreate() {
    $("#playerName").html("Daniel");
    game = new Game(true, 2);
    connection.createGame(true, "Daniel", 2, true);
}

function debugJoin(gameId) {
    $("#playerName").html("Katie");
    connection.joinGame(gameId, "Katie");
}
