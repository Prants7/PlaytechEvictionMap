package implementation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EvictionMapImplementation<Key, Value> implements EvictionMap<Key, Value> {
    private long timeOutSeconds;
    private StorageElement<Key, Value> storageElement;

    public EvictionMapImplementation(long timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
        this.storageElement = new StorageElement<>();
    }

    @Override
    public void put(Key keyElement, Value valueElement) {
        this.putScript(keyElement, valueElement);
    }

    private void putScript(Key keyElement, Value valueElement) {
        this.storageElement.put(keyElement, this.makeAndGetNewExpirationPair(valueElement));
    }

    private ValueAndExpirationPair<Value> makeAndGetNewExpirationPair(Value insertedValue) {
        return new ValueAndExpirationPair<>(insertedValue, this.giveNewTimeOutMoment());
    }

    private LocalTime giveNewTimeOutMoment() {
        return this.getCurrentTime().plus(this.timeOutSeconds, ChronoUnit.SECONDS);
    }

    private LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    private boolean isValueAndExpirationPairFresh(ValueAndExpirationPair<Value> elementToCheck) {
        return this.getCurrentTime().isBefore(elementToCheck.getExpirationMoment());
    }

    @Override
    public Value get(Key searchKey) {
        return this.getScript(searchKey);
    }

    private Value getScript(Key searchKey) {
        ValueAndExpirationPair<Value> searchResult = this.storageElement.get(searchKey);
        if(searchResult != null) {
            if(isValueAndExpirationPairFresh(searchResult)) {
                return searchResult.getValue();
            }
        }
        return null;
    }
}
