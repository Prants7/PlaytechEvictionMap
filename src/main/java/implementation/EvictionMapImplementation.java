package implementation;

import dataCleaner.DataCleaner;

import java.time.LocalDateTime;

/**
 * Implementation of the EvictionMap interface, this is the main api object of this project
 * @param <Key> Type of keys used in this map
 * @param <Value> Type of values used in this map
 */
public class EvictionMapImplementation<Key, Value> implements EvictionMap<Key, Value> {
    private long timeOutSeconds;
    private StorageElement<Key, Value> storageElement;
    private TimeBox wrappedTimeBox;
    private DataCleaner<Key> addedDataCleaner;

    /**
     * Constructor for a version that doesn't delete expired values, but simply limits access
     * @param timeOutSeconds the amount of seconds that a new key value is accessible
     */
    public EvictionMapImplementation(long timeOutSeconds) {
        setUpStandardValues(timeOutSeconds);
    }

    /**
     * Construtor that also adds a cleaner module, that will deal with removal of inaccessible values from memory
     * @param timeOutSeconds the amount of seconds that a new key value is accessible
     * @param newDataCleaner the DataCleaner module that new Object should use
     */
    public EvictionMapImplementation(long timeOutSeconds, DataCleaner<Key> newDataCleaner) {
        setUpStandardValues(timeOutSeconds);
        this.connectDataCleaner(newDataCleaner);
    }

    /**
     * Method for setting up used elements in all EvictionMapImplementations
     * @param timeOutSeconds the amount of seconds that a new key value is accessible
     */
    private void setUpStandardValues(long timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
        this.storageElement = new StorageElement<>();
        this.wrappedTimeBox = new TimeBox();
    }

    /**
     * Method for setting up DataCleaner that was given to this object
     * @param newDataCleaner new Data Cleaner object that this EvictionMap will use
     */
    private void connectDataCleaner(DataCleaner<Key> newDataCleaner) {
        this.addedDataCleaner = newDataCleaner;
        this.addedDataCleaner.insertStorageElement(this.storageElement);
        this.addedDataCleaner.insertCurrentlyUsedTimeBox(this.wrappedTimeBox);
    }

    @Override
    public void put(Key keyElement, Value valueElement) {
        this.putScript(keyElement, valueElement);
    }

    /**
     * Script that will be called by the interfaces public put method
     * @param keyElement keyElement to be saved
     * @param valueElement valueElement that will be saved with the key
     */
    private void putScript(Key keyElement, Value valueElement) {
        ValueAndExpirationPair<Value> newPair = this.makeAndGetNewExpirationPair(valueElement);
        this.storageElement.put(keyElement, newPair);
        if(this.hasDataCleaner()) {
            this.addedDataCleaner.announceNewElement(keyElement, newPair.getExpirationMoment());
        }
    }

    /**
     * For getting a new ExpirationPair, this is an object that attaches a Value element to an expiration date
     * @param insertedValue Value element saved into the ExpirationPair
     * @return a new object that has inserted Value and correct expiration time from the moment of call
     */
    private ValueAndExpirationPair<Value> makeAndGetNewExpirationPair(Value insertedValue) {
        return new ValueAndExpirationPair<>(insertedValue, this.giveNewTimeOutMoment());
    }

    /**
     * Method for getting a LocalDateTime in the future by this EvictionMaps expiration time
     * @return New LocalDateTimeElement set in the future by expiration time
     */
    private LocalDateTime giveNewTimeOutMoment() {
        return this.wrappedTimeBox.getTimeOutInFutureBySeconds(this.timeOutSeconds);
    }

    /**
     * Method for getting current time as LocalDateTime
     * @return current moment
     */
    private LocalDateTime getCurrentTime() {
        return this.wrappedTimeBox.getCurrentTimeMoment();
    }

    /**
     * Method that checks if a ValueAndExpiration pair is still fresh
     * @param elementToCheck element that is checked
     * @return true if its fresh, false if it has expired
     */
    private boolean isValueAndExpirationPairFresh(ValueAndExpirationPair<Value> elementToCheck) {
        return this.getCurrentTime().isBefore(elementToCheck.getExpirationMoment());
    }

    @Override
    public Value get(Key searchKey) {
        return this.getScript(searchKey);
    }

    /**
     * Script that will be called by the interfaces public get method
     * @param searchKey key that will be searched for
     * @return Value stored under the key or null if there is none or the value has expired
     */
    private Value getScript(Key searchKey) {
        ValueAndExpirationPair<Value> searchResult = this.storageElement.get(searchKey);
        if(searchResult != null) {
            if(isValueAndExpirationPairFresh(searchResult)) {
                return searchResult.getValue();
            }
        }
        return null;
    }

    /**
     * Checks if this object has a DataCleaner
     * @return true if it does, false if not
     */
    private boolean hasDataCleaner() {
        return this.addedDataCleaner != null;
    }

    @Override
    public int getAmountOfSavedValues() {
        return this.storageElement.getStorageSize();
    }
}
