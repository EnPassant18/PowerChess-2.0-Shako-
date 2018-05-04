class TimeControl {
    constructor(initial, increment) {
        // Both in SECONDS
        this.initial = initial;
        this.increment = increment;
    }
}

class Timer {
    constructor(timeControl) {
        this._myTime = timeControl.initial;
        this._theirTime = timeControl.initial;
        this._increment = timeControl.increment;
        this._myDisplay = "";
        this._theirDisplay = "";
        this._display(PLAYER.PLAYER);
        this._display(PLAYER.OPPONENT);
    }

    start() {
        this._timer = setInterval(() => {
            if (game.action === ACTION.NONE) {
                this._theirTime = Math.max(0, this._theirTime - 0.01);
                this._display(PLAYER.OPPONENT);
            } else {
                this._myTime -= 0.01;
                this._display(PLAYER.PLAYER);
                if (this._myTime <= 0) {
                    connection.lose(GAME_END_CAUSE.TIME);
                }
            }
        }, 10);
    }

    stop() {
        clearInterval(this._timer);
    }

    increment(player) {
        if (player) {
            this._myTime += this._increment;
            this._display(true);
        } else {
            this._theirTime += this._increment;
            this._display(false);
        }
    }

    _display(player) {
        if (player) {
            const newDisplay = this._timeToString(this._myTime);
            if (newDisplay !== this._myDisplay) {
                this._myDisplay = newDisplay;
                $("#playerClock").html(this._myDisplay);
            }
        } else {
            const newDisplay = this._timeToString(this._theirTime);
            if (newDisplay !== this._theirDisplay) {
                this._theirDisplay = newDisplay;
                $("#opponentClock").html(this._theirDisplay);
            }
        }
    }

    // time (seconds) -> "mm:ss"
    _timeToString(time) {
        const seconds = time % 60;
        const minutes = (time - seconds) / 60;
        if (seconds < 10) {
            return `${minutes}:0${Math.trunc(seconds)}`;
        } else {
            return `${minutes}:${Math.trunc(seconds)}`;
        }
    }
}

Timer.TIME_CONTROL = {
    QUICK: new TimeControl(300, 5),
    STANDARD: new TimeControl(900, 15),
    SLOW: new TimeControl(2700, 45)
}