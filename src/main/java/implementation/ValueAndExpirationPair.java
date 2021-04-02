package implementation;

import java.time.LocalDateTime;

public class ValueAndExpirationPair<Value> {
    private Value savedValue;
    private LocalDateTime timeOutMoment;

    public ValueAndExpirationPair(Value savedValue, LocalDateTime expirationMoment) {
        this.savedValue = savedValue;
        this.timeOutMoment = expirationMoment;
    }

    public Value getValue() {
        return this.savedValue;
    }

    public LocalDateTime getExpirationMoment() {
        return this.timeOutMoment;
    }
}
