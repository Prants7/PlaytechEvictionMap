package implementation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeBox {

    public LocalTime getCurrentTimeMoment() {
        return this.getCurrentLocalTime();
    }

    public LocalTime getTimeOutInFutureBySeconds(long secondsIntoTheFuture) {
        return this.getCurrentLocalTime().plus(secondsIntoTheFuture, ChronoUnit.SECONDS);
    }

    private LocalTime getCurrentLocalTime() {
        return LocalTime.now();
    }
}
