package implementation;

public class StoredValue<Key, Value> {
    private Key savedKey;
    private Value savedValue;
    private long timeOutMoment;

    public StoredValue(Key key, Value value, long timeOutMoment) {
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

    public boolean saveNewValue(Value newSavedValue, long newTimeOut) {
        this.savedValue = newSavedValue;
        this.timeOutMoment = newTimeOut;
        return true;
    }
}
