package implementation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Wrapper for getting time, this is used to keep integrity between all elements that use time in this project
 */
public class TimeBox {

    /**
     * Getter for current time moment
     * @return LocalDateTime object for now
     */
    public LocalDateTime getCurrentTimeMoment() {
        return this.getCurrentLocalTime();
    }

    /**
     * Getter for a future time moment
     * @param secondsIntoTheFuture how many seconds in the future should the object be
     * @return LocalDateTime object set for future by the input seconds
     */
    public LocalDateTime getTimeOutInFutureBySeconds(long secondsIntoTheFuture) {
        return this.getCurrentLocalTime().plus(secondsIntoTheFuture, ChronoUnit.SECONDS);
    }

    /**
     * Private method for calling on LocalDateTime.now(), used by the other functions connected to now moment
     * @return LocalDateTime object for now
     */
    private LocalDateTime getCurrentLocalTime() {
        return LocalDateTime.now();
    }
}
