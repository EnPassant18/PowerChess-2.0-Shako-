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
            <td>${gameId}</td>
            <td>${TIME_CONTROL[gameId[timeControl]]}</td>
            <td>${COLOR[gameId[color]]}</td>
            <td><button class="play">Play</button></td>
        </tr>`);
    areGames();
}

function areGames() {
    if ($("tr").length === 1) {
        $("noGames").removeAttr("hidden");
    } else {
        $("noGames").attr("hidden", "true");
    }
}

TIME_CONTROL = {
    0: "Quick",
    1: "Standard",
    2: "Slow"
}

COLOR = {
    true: "White",
    false: "Black"
}