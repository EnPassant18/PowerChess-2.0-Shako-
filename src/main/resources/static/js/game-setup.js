$(document).ready(() => {

    let game;
    connection = new Connection("");
    UI.drawGame();

    if (localStorage.create !== undefined) {
        game = new Game(localStorage.create.color, localStorage.create.timeControl);
        connection.create(
            localStorage.create.color, localStorage.create.name,
            localStorage.create.timeControl, localStorage.create.isPublic)
    } else if (localStorage.join !== undefined) {
        connection.join(localStorage.join.id, localStorage.join.name);
    } else {
        window.location = "homeurl";
    }

    $("#option1").click(() => game.powerSelect(true));
    $("#option2").click(() => game.powerSelect(false));
    $("#draw").click(connection.draw);
    $("#resign").click(() => connection.lose(GAME_END_CAUSE.RESIGNATION));
});
