let game;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/play");
    UI.drawBoard();

    let name;
    if (localStorage.name) {
        name = localStorage.name;
    } else if (getUrlVar("name")) {
        name = getUrlVar("name");
    } else {
        name = "Guest" + Math.trunc(10000 * Math.random());
    }
    $("#playerName").html(name);

    if ((time = getUrlVar("time")) 
    && (color = getUrlVar("color")) 
    && (privacy = getUrlVar("privacy"))) {
        if (color === "Random") color = (Math.random() > 0.5);
        game = new Game(color, time);
        connection.socket.onopen = () => connection.createGame(color, name, time, privacy);
    } else if (id = getUrlVar("id")) {
        connection.socket.onopen = () => connection.joinGame(id, name);
    } else {
        window.location = "home.html";
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

function getUrlVar(variable) {
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}