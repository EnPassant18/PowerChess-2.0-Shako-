let game;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/play");
    UI.drawBoard();

    if (localStorage.create !== undefined) {
        game = new Game(localStorage.create.color, localStorage.create.timeControl);
        connection.createGame(
            localStorage.create.color, localStorage.create.name,
            localStorage.create.timeControl, localStorage.create.isPublic)
    } else if (localStorage.join !== undefined) {
        connection.joinGame(localStorage.join.id, localStorage.join.name);
    }

    $("#option1").click(() => game.powerSelect(true));
    $("#option2").click(() => game.powerSelect(false));
    $("#draw").click(connection.draw);
    $("#resign").click(() => connection.lose(GAME_END_CAUSE.RESIGNATION));
});
