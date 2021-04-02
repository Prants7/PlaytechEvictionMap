package dataCleaner;

import implementation.StorageElement;
import implementation.TimeBox;

import java.time.LocalDateTime;

/**
 * Base class for DataCleaner objects that deals with connecting to an EvictionMap type object
 * @param <Key> the type of key used in the target EvictionMap
 */
public abstract class DataCleanerBase<Key> implements DataCleaner<Key> {
    private TimeBox insertedTimeBox;
    private StorageElement insertedStorageElement;

    @Override
    public void announceNewElement(Key addedKey, LocalDateTime momentItExpires) {
        this.dealWithNewAddedElement(addedKey, momentItExpires);
    }

    /**
     * abstract script for child objects, passes on announceNewElement method call
     * @param addedKey The key that put call was made with
     * @param momentItExpires LocalDateTime value of when the new put call will expire, unless extended
     */
    protected abstract void dealWithNewAddedElement(Key addedKey, LocalDateTime momentItExpires);

    @Override
    public void insertCurrentlyUsedTimeBox(TimeBox insertedTimeBox) {
        this.insertedTimeBox = insertedTimeBox;
    }

    @Override
    public void insertStorageElement(StorageElement insertedStorageElement) {
        this.insertedStorageElement = insertedStorageElement;
    }

    /**
     * For child object to gain current moment as LocalDateTime
     * @return LocalDateTime object for now or null, if TimeBox wasn't inserted
     */
    protected LocalDateTime getCurrentMoment() {
        if(this.insertedTimeBox != null) {
            return this.insertedTimeBox.getCurrentTimeMoment();
        }
        return null;
    }

    /**
     * For child objects to call for the cleanup of a specif key value in saved data
     * @param keyToCallOn Key that will be deleted if its value has expired
     * @return true if the element was deleted, false if it wasn't
     *         (has been extended or there is no insertedStorageElement)
     */
    protected boolean callForCleanUpOnKey(Key keyToCallOn) {
        if(this.insertedStorageElement != null) {
            return this.insertedStorageElement.callForCheckCleanOnKeyValue(keyToCallOn, this.getCurrentMoment());
        }
        return false;
    }

    /**
     * For child objects to call for a full cleanup of saved data
     * @return true if a successful call for cleanup was issued, false is there was no insertedStorageElement
     */
    protected boolean callForFullCleanUp() {
        if(this.insertedStorageElement != null) {
            this.insertedStorageElement.performCleanUp(this.getCurrentMoment());
            return true;
        }
        return false;
    }
}
