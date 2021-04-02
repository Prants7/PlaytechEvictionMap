package implementation;

import dataCleaner.DataCleaner;

import java.time.LocalDateTime;

public class EvictionMapImplementation<Key, Value> implements EvictionMap<Key, Value> {
    private long timeOutSeconds;
    private StorageElement<Key, Value> storageElement;
    private TimeBox wrappedTimeBox;
    private DataCleaner<Key> addedDataCleaner;

    public EvictionMapImplementation(long timeOutSeconds) {
        setUpStandardValues(timeOutSeconds);
    }

    public EvictionMapImplementation(long timeOutSeconds, DataCleaner<Key> newDataCleaner) {
        setUpStandardValues(timeOutSeconds);
        this.connectDataCleaner(newDataCleaner);
    }

    private void setUpStandardValues(long timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
        this.storageElement = new StorageElement<>();
        this.wrappedTimeBox = new TimeBox();
    }

    private void connectDataCleaner(DataCleaner<Key> newDataCleaner) {
        this.addedDataCleaner = newDataCleaner;
        this.addedDataCleaner.addAccessToStorageElement(this.storageElement);
        this.addedDataCleaner.insertCurrentlyUsedTimeBox(this.wrappedTimeBox);
    }

    @Override
    public void put(Key keyElement, Value valueElement) {
        this.putScript(keyElement, valueElement);
    }

    private void putScript(Key keyElement, Value valueElement) {
        ValueAndExpirationPair<Value> newPair = this.makeAndGetNewExpirationPair(valueElement);
        this.storageElement.put(keyElement, newPair);
        if(this.hasDataCleaner()) {
            this.addedDataCleaner.announceNewElement(keyElement, newPair.getExpirationMoment());
        }
    }

    private ValueAndExpirationPair<Value> makeAndGetNewExpirationPair(Value insertedValue) {
        return new ValueAndExpirationPair<>(insertedValue, this.giveNewTimeOutMoment());
    }

    private LocalDateTime giveNewTimeOutMoment() {
        return this.wrappedTimeBox.getTimeOutInFutureBySeconds(this.timeOutSeconds);
    }

    private LocalDateTime getCurrentTime() {
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

    private boolean hasDataCleaner() {
        return this.addedDataCleaner != null;
    }

    public int getAmoundOfSavedValues() {
        return this.storageElement.getStorageSize();
    }
}
