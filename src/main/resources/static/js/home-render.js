function render(allGames) {
    games = allGames;
    const gameList = $("#right");
    let areGames = false;
    for (gameId in allGames) {
        areGames = true;
        addGame(gameId, allGames[gameId], gameList);
    }
}

function removeGame(gameId) {
    $(`#${gameId}`).remove();
}

function addGame(gameId, game, gameList) {
    gameList.append(
        `<tr id="${gameId}">
            <td>${gameId}</td>
            <td>${TIME_CONTROL[gameId[timeControl]]}</td>
            <td>${COLOR[gameId[color]]}</td>
            <td><button class="play">Play</button></td>
        </tr>`)
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