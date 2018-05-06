function render(allGames) {
    games = allGames;
    const gameList = $("#right");
    for (gameId in allGames) {
        addGame(gameId, allGames[gameId], gameList);
    }
    areGames();
}

function removeGame(gameId) {
    $(`#${gameId}`).remove();
    areGames();
}

function addGame(gameId, game, gameList) {
    gameList.append(
        `<tr id="${gameId}">
            <td>${game.name}</td>
            <td>${TIME_CONTROL[game.timeControl]}</td>
            <td>${COLOR[game.color]}</td>
            <td><button class="play">Play</button></td>
        </tr>`);
    $($(".play")[$(".play").length-1]).click(joinGame);
    areGames();
}

function areGames() {
    if ($("tr").length === 2) {
        $("#noGames").removeAttr("hidden");
    } else {
        $("#noGames").attr("hidden", "true");
    }
}

TIME_CONTROL = {
    0: "Quick",
    1: "Standard",
    2: "Slow"
}

COLOR = {
    1: "White",
    0: "Black"
}