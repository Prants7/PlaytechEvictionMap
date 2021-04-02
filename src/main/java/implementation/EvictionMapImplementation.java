package implementation;

import java.time.LocalTime;

public class EvictionMapImplementation<Key, Value> implements EvictionMap<Key, Value> {
    private long timeOutSeconds;
    private StorageElement<Key, Value> storageElement;
    private TimeBox wrappedTimeBox;

    public EvictionMapImplementation(long timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
        this.storageElement = new StorageElement<>();
        this.wrappedTimeBox = new TimeBox();
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
        return this.wrappedTimeBox.getTimeOutInFutureBySeconds(this.timeOutSeconds);
    }

    private LocalTime getCurrentTime() {
        return this.wrappedTimeBox.getCurrentTimeMoment();
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
