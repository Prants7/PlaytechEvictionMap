package implementation;

import java.time.LocalDateTime;

/**
 * For adding an expiration date to a value
 * @param <Value> the type of values used in specific EvictionMap
 */
public class ValueAndExpirationPair<Value> {
    private Value savedValue;
    private LocalDateTime timeOutMoment;

    /**
     * constructor
     * @param savedValue value to store
     * @param expirationMoment designated expiration moment
     */
    public ValueAndExpirationPair(Value savedValue, LocalDateTime expirationMoment) {
        this.savedValue = savedValue;
        this.timeOutMoment = expirationMoment;
    }

    /**
     * Getter for value
     * @return saved value
     */
    public Value getValue() {
        return this.savedValue;
    }

    /**
     * Getter for expiration moment
     * @return saved expiration moment
     */
    public LocalDateTime getExpirationMoment() {
        return this.timeOutMoment;
    }
}
