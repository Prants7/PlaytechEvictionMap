package implementation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class StorageElement<Key, Value> {
    private Hashtable<Key, ValueAndExpirationPair<Value>> storageLocation;
    private LocalTime lastCleanUpTime;

    public StorageElement() {
        this.storageLocation = new Hashtable<>();
    }

    public void put(Key keyElement, ValueAndExpirationPair<Value> newValuePair) {
        this.storageLocation.put(keyElement, newValuePair);
    }

    public ValueAndExpirationPair<Value> get(Key keyElement) {
        return this.storageLocation.get(keyElement);
    }

    public void performCleanUp(LocalTime currentTime) {
        List<Key> expiredKeys = this.getListOfKeysWithExpiredValues(currentTime);
        this.performRemovalOfExpiredValues(expiredKeys, currentTime);
        this.lastCleanUpTime = currentTime;
    }

    private List<Key> getListOfKeysWithExpiredValues(LocalTime currentTime) {
        List<Key> expiredKeys = new ArrayList<>();
        Enumeration<Key> keys = this.storageLocation.keys();
        while(keys.hasMoreElements()) {
            Key nextKey = keys.nextElement();
            ValueAndExpirationPair<Value> nextValue = storageLocation.get(nextKey);
            if(nextValue.getExpirationMoment().isBefore(currentTime)) {
                expiredKeys.add(nextKey);
            }
        }
        return expiredKeys;
    }

    private void performRemovalOfExpiredValues(List<Key> expiredKeys, LocalTime currentTime) {
        for(Key oneKey : expiredKeys) {
            performRemovalOfOneExpiredKey(oneKey, currentTime);
        }
    }

    private boolean performRemovalOfOneExpiredKey(Key oneKey, LocalTime currentTime) {
        if(this.storageLocation.get(oneKey).getExpirationMoment().isBefore(currentTime)) {
            this.storageLocation.remove(oneKey);
            return true;
        }
        return false;
    }

    public int getStorageSize() {
        return this.storageLocation.size();
    }

    public LocalTime getLastCleanUpTime() {
        return this.lastCleanUpTime;
    }
}