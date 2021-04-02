package dataCleaner;

import implementation.StorageElement;
import implementation.TimeBox;

import java.time.LocalDateTime;

public interface DataCleaner<Key> {

    public void announceNewElement(Key addedKey, LocalDateTime momentItExpires);

    public void insertCurrentlyUsedTimeBox(TimeBox insertedTimeBox);

    public void addAccessToStorageElement(StorageElement insertedStorageElement);
}
