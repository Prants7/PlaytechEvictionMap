package dataCleaner;

import implementation.StorageElement;
import implementation.TimeBox;

import java.time.LocalDateTime;

public abstract class DataCleanerBase<Key> implements DataCleaner<Key> {
    private TimeBox insertedTimeBox;
    private StorageElement insertedStorageElement;

    public void announceNewElement(Key addedKey, LocalDateTime momentItExpires) {
        this.dealWithNewAddedElement(addedKey, momentItExpires);
    }

    protected abstract void dealWithNewAddedElement(Key addedKey, LocalDateTime momentItExpires);

    public void insertCurrentlyUsedTimeBox(TimeBox insertedTimeBox) {
        this.insertedTimeBox = insertedTimeBox;
    }

    public void addAccessToStorageElement(StorageElement insertedStorageElement) {
        this.insertedStorageElement = insertedStorageElement;
    }

    protected LocalDateTime getCurrentMoment() {
        if(this.insertedTimeBox != null) {
            return this.insertedTimeBox.getCurrentTimeMoment();
        }
        return null;
    }

    protected boolean callForCleanUpOnKey(Key keyToCallOn) {
        if(this.insertedStorageElement != null) {
            return this.insertedStorageElement.callForCheckCleanOnKeyValue(keyToCallOn, this.getCurrentMoment());
        }
        return false;
    }

    protected boolean callForFullCleanUp() {
        if(this.insertedStorageElement != null) {
            this.insertedStorageElement.performCleanUp(this.getCurrentMoment());
            return true;
        }
        return false;
    }
}
