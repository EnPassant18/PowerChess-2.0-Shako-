$(document).ready(() => {

    game = new Game(COLOR.WHITE, Timer.TIME_CONTROL.STANDARD);
    connection = new Connection("");
    UI.drawGame();

    $("#option1").click(() => game.powerSelect(true));
    $("#option2").click(() => game.powerSelect(false));
    $("#draw").click(connection.draw);
    $("#resign").click(() => connection.lose(GAME_END_CAUSE.RESIGNATION));
});