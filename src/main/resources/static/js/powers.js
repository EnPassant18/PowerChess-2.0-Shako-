class Power {
    constructor(image, followUp) {
        this.image = image;
        this.followUp = followUp; // ACTION
        this.hasFollowUp = (followUp !== ACTION.NONE);
    }
}

class DisplayedPower extends Power {
    constructor(power, option) {
        super(power.image, power.followUp);
        this.option = option; // boolean (true = first)
        this._selected = false;
    }

    get selected() {
        return this._selected;
    }
    set selected(newValue) {
        this._selected = newValue;
        UI.highlightPower(this.option, newValue);
    }

    activate() {
        if (this.hasFollowUp) {
            this.selected = true;
        } else {
            connection.usePower(this.option);
        }
    }

    activateFollowUp(followUpObject) {
        if (this.selected) {
            connection.usePower(this.option, followUpObject);
        } else {
            console.log("Error: power must be selected to perform followup");
        }
    }
}