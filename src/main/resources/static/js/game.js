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
        if (this._action === ACTION.MOVE_THIS) {
            this.mustMove = moving.piece;
        } else {
            this.mustMove = null;
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
    }

    // Displays a popup when the game ends
    gameOver(result, reason) {
        this._action = ACTION.NONE;
        this.timer.stop();
        $("#draw").off("click");
        $("#resign").off("click");
        switch (result) {
            case GAME_RESULT.WIN: alert("You win"); break;
            case GAME_RESULT.LOSS: alert("You lose"); break;
            case GAME_RESULT.DRAW: alert("Game drawn"); break;
        }
    }
}