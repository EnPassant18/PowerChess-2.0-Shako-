let games;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/home");

    $("#logo").css("width", $("#form").width() + "px");

    if (localStorage.name === undefined) localStorage.name = "Guest" + Math.trunc(10000*Math.random());
    $("#welcome").html(`Welcome, ${localStorage.name}!`);
    $("#submitName").click(event => {
        localStorage.name = $("#name").val();
        $("#welcome").html(`Welcome, ${localStorage.name}!`);
    });
    $("#signIn").submit(event => event.preventDefault());
})

function joinGame(event) {
    delete sessionStorage.create;
    window.location = `game.html?id=${event.target.parentElement.parentElement.id}`;
}