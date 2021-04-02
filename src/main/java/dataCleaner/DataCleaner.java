package dataCleaner;

import implementation.StorageElement;
import implementation.TimeBox;

import java.time.LocalDateTime;

/**
 * Interface for Data Cleaner objects, these objects are supposed to clean up saved data that
 * has expired and is already not available
 * @param <Key> the type of keys used in the EvictionTable this is meant for
 */
public interface DataCleaner<Key> {

    /**
     * Method used by EvictionMap to announce new calls of put to savedData
     * @param addedKey The key that put call was made with
     * @param momentItExpires LocalDateTime value of when the new put call will expire, unless extended
     */
    public void announceNewElement(Key addedKey, LocalDateTime momentItExpires);

    /**
     * For inserting time box value into this DataCleaner
     * (this should only ever be called by EvictionMap Objects and on the moment of initialising)
     * @param insertedTimeBox the inserted TimeBox
     */
    public void insertCurrentlyUsedTimeBox(TimeBox insertedTimeBox);

    /**
     * For inserting StorageElement into this DataCleaner
     * (this should only ever be called by EvictionMap objects and on the moment of initialising)
     * @param insertedStorageElement the inserted StorageElement
     */
    public void insertStorageElement(StorageElement insertedStorageElement);
}
