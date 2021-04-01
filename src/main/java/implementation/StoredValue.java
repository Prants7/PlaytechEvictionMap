package implementation;

import java.time.LocalTime;

public class StoredValue<Key, Value> {
    private Key savedKey;
    private Value savedValue;
    private LocalTime timeOutMoment;

    public StoredValue(Key key, Value value, LocalTime timeOutMoment) {
        this.savedKey = key;
        this.savedValue = value;
        this.timeOutMoment = timeOutMoment;
    }

    public Key getKey() {
        return  this.savedKey;
    }

    public boolean matchesKey(Key searchKey) {
        return searchKey == savedKey;
    }

    public Value getValue() {
        return this.savedValue;
    }

    public boolean saveNewValue(Value newSavedValue, LocalTime newTimeOut) {
        this.savedValue = newSavedValue;
        this.timeOutMoment = newTimeOut;
        return true;
    }

    public LocalTime getTimeOutMoment() {
        return this.timeOutMoment;
    }
}
