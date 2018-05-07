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

    start(myMove) {
        this.switch(myMove);
        this._loop = setInterval(() => {
            if (!this.myMove) {
                this._theirTime = Math.max(0, this.moveStartTimeLeft + this.moveStartTime - this._getTime());
                this._display(PLAYER.OPPONENT);
            } else {
                this._myTime = this.moveStartTimeLeft + this.moveStartTime - this._getTime();
                this._display(PLAYER.PLAYER);
                if (this._myTime <= 0) {
                    connection.lose(GAME_END_CAUSE.TIME);
                }
            }
        }, 25);
    }

    stop() {
        clearInterval(this._loop);
        $("#playerClock").css("font-weight","");
        $("#opponentClock").css("font-weight","");
    }

    switch(myMove) {
        this.myMove = myMove;
        this.moveStartTime = this._getTime();
        this.moveStartTimeLeft = myMove ? this._myTime : this._theirTime;
        if (myMove) {
            $("#playerClock").css("font-weight","Bold");
            $("#opponentClock").css("font-weight","");
        } else {
            $("#playerClock").css("font-weight","");
            $("#opponentClock").css("font-weight","Bold");
        }
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

    _getTime() {
        return new Date().getTime() / 1000;
    }
}

Timer.TIME_CONTROL = {
    0: new TimeControl(300, 5),
    1: new TimeControl(900, 15),
    2: new TimeControl(2700, 45)
}