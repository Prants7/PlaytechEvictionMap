package implementation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeBox {

    public LocalDateTime getCurrentTimeMoment() {
        return this.getCurrentLocalTime();
    }

    public LocalDateTime getTimeOutInFutureBySeconds(long secondsIntoTheFuture) {
        return this.getCurrentLocalTime().plus(secondsIntoTheFuture, ChronoUnit.SECONDS);
    }

    private LocalDateTime getCurrentLocalTime() {
        return LocalDateTime.now();
    }
}
