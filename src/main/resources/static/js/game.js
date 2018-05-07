class Game {
    constructor(color, timeControl) {
        this.color = color;
        this.timer = new Timer(Timer.TIME_CONTROL[timeControl]);
        this.power1 = null;
        this.power2 = null;
        this.selected = null;
        color ? this._action = ACTION.MOVE : this._action = ACTION.NONE;
        setTimeout(() => UI.drawPieces(), 10);
    }

    get action() {
        return this._action;
    }
    set action(newAction) {
        if (this._action === ACTION.NONE) {
            this.timer.increment(false);
        } else if (this.selected === null) {
            this.timer.increment(true);
            $("#drawOffered").attr("hidden", "true"); 
        }
        if (newAction === ACTION.NONE) {
            $("#playerClock").css("font-weight","");
            $("#opponentClock").css("font-weight","Bold");
        } else {
            $("#playerClock").css("font-weight","Bold");
            $("#opponentClock").css("font-weight","");
        }
        this._action = newAction;
        this.selected = null;
    }

    // Prompts the user to select between two powers
    powerPrompt(power1, power2) {
        UI.showPowers(power1.image, power2.image);
        this.power1 = new DisplayedPower(power1, true);
        this.power2 = new DisplayedPower(power2, false);
    }

    // When the user selects a power
    // selection: boolean (false = first)
    powerSelect(selection) {
        const notSelected = selection ? this.power1 : this.power2;
        const isSelected = selection ? this.power2 : this.power1;
        if (isSelected.hasFollowUp) {
            this.action = isSelected.followUp;
            this.selected = isSelected;
        }
        if (notSelected.selected) {
            notSelected.selected = false;
        }
        isSelected.activate();
    }

    powerFollowUp(followUpObject) {
        this.selected.activateFollowUp(followUpObject);
    }

    start() {
        this.timer.start();
        this.color ? $("#playerClock").css("font-weight","Bold") : $("#opponentClock").css("font-weight","Bold");
    }

    // Displays a popup when the game ends
    gameOver(result, reason) {
        this._action = ACTION.NONE;
        this.timer.stop();
        $("#darkVeil").removeAttr("hidden");
        $("#gameOver").removeAttr("hidden");
        let resultMsg, reasonMsg;
        switch (result) {
            case GAME_RESULT.WIN: resultMsg = "You won"; break;
            case GAME_RESULT.LOSS: resultMsg = "You lost"; break;
            case GAME_RESULT.DRAW: resultMsg = "Game drawn"; break;
        }
        switch (reason) {
            case GAME_END_CAUSE.MATE: reasonMsg = " by mate"; break;
            case GAME_END_CAUSE.RESIGNATION: reasonMsg = " by resignation"; break;
            case GAME_END_CAUSE.TIME: reasonMsg = " on time"; break;
            case GAME_END_CAUSE.DRAW_AGREED: reasonMsg = " by agreement"; break;
        }
        $("#gameOverMessage").html(resultMsg + reasonMsg);
    }
}