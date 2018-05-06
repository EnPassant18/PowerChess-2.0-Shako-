let name = "Guest" + Math.trunc(10000*Math.random());
let games;

$(document).ready(() => {

    $("#logo").css("width", $("#form").width() + "px");

    $("#welcome").html(`Welcome, ${name}!`);
    $("#submitName").click(() => {
        name = $("#name").val();
        $("#welcome").html(`Welcome, ${name}!`);
    })

    $("#submitCreate").click(() => {
        if ($("input[name=color]:checked").val() === "Random") {
            color = Math.random() > 0.5;
        } else {
            color = $("input[name=color]:checked").val();
        }
        sessionStorage.create = {
            name: name,
            color: color,
            timeControl: $("input[name=time]:checked").val(),
            public: $("input[name=privacy]:checked").val()
        }
        window.location = "game url";
    })
})