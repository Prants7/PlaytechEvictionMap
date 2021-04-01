package implementation;

import java.time.LocalTime;

public class ValueAndExpirationPair<Value> {
    private Value savedValue;
    private LocalTime timeOutMoment;

    public ValueAndExpirationPair(Value savedValue, LocalTime expirationMoment) {
        this.savedValue = savedValue;
        this.timeOutMoment = expirationMoment;
    }

    public Value getValue() {
        return this.savedValue;
    }

    public LocalTime getExpirationMoment() {
        return this.timeOutMoment;
    }
}
