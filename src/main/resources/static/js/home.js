let games;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/home");

    $("#logo").css("width", $("#form").width() + "px");

    if (localStorage.name === undefined) localStorage.name = "Guest" + Math.trunc(10000*Math.random());
    $("#welcome").html(`Welcome, ${localStorage.name}!`);
    $("#signIn").submit(event => {
        event.preventDefault();
        localStorage.name = $("#name").val();
        $("#welcome").html(`Welcome, ${localStorage.name}!`);
    })

    /*
    $("#submitCreate").click(() => {
        if ($("input[name=color]:checked").val() === "Random") {
            color = Math.random() > 0.5;
        } else {
            color = $("input[name=color]:checked").val();
        }
        sessionStorage.create = JSON.stringify({
            name: name,
            color: color,
            timeControl: $("input[name=time]:checked").val(),
            public: $("input[name=privacy]:checked").val()
        });
        delete sessionStorage.join;
        window.location = "game.html";
    })
    */
})

function joinGame(event) {
    delete sessionStorage.create;
    window.location = `game.html?id=${event.target.parentElement.parentElement.id}`;
}