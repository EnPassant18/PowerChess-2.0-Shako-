let name = "Guest" + Math.trunc(10000*Math.random());
let games;

$(document).ready(() => {

    connection = new Connection("ws://localhost:4567/home");

    $("#logo").css("width", $("#form").width() + "px");

    $("#welcome").html(`Welcome, ${name}!`);
    $("#signIn").submit(event => {
        event.preventDefault();
        name = $("#name").val();
        $("#welcome").html(`Welcome, ${name}!`);
    })

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
})

function joinGame(event) {
    sessionStorage.join = JSON.stringify({
        name: name,
        id: event.target.parentElement.parentElement.id
    });
    delete sessionStorage.create;
    window.location = "game.html";
}