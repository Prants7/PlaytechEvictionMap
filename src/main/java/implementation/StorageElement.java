package implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Class that only deals with storing elements in a Hashtable
 * Should never be used without a master EvictionMap
 * @param <Key> Keys used in the hashtable
 * @param <Value> type of value saved in ValueAndExpirationPairs
 */
public class StorageElement<Key, Value> {
    private Hashtable<Key, ValueAndExpirationPair<Value>> storageLocation;
    private LocalDateTime lastCleanUpTime;

    /**
     * constructor
     */
    public StorageElement() {
        this.storageLocation = new Hashtable<>();
    }

    /**
     * Method for putting a new ValueAndExpirationPair into the Hashtable
     * @param keyElement key element the value is saved under
     * @param newValuePair the actual saved data
     */
    public void put(Key keyElement, ValueAndExpirationPair<Value> newValuePair) {
        this.storageLocation.put(keyElement, newValuePair);
    }

    /**
     * Method for getting a ValueAndExpirationPair saved under a key
     * @param keyElement Key element that is searched for
     * @return found element or null if key is not in the table
     */
    public ValueAndExpirationPair<Value> get(Key keyElement) {
        return this.storageLocation.get(keyElement);
    }

    /**
     * Method for calling a full cleanup in this StorageElements HashTable
     * @param currentTime LocalDateTime that stands for now, it will delete all saved values that are marked
     *                    for a expiration time before this input value
     */
    public void performCleanUp(LocalDateTime currentTime) {
        List<Key> expiredKeys = this.getListOfKeysWithExpiredValues(currentTime);
        this.performRemovalOfExpiredValues(expiredKeys, currentTime);
        this.lastCleanUpTime = currentTime;
    }

    /**
     * Builds a list of keys that have expired values
     * @param currentTime LocalDateTime that stands for now, keys that have value stamped to expire before this moment
     *                    will be added to return list
     * @return List of keys that expire before input LocalDateTime
     */
    private List<Key> getListOfKeysWithExpiredValues(LocalDateTime currentTime) {
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

    /**
     * Method that takes list of keys to remove and attempts to remove them
     * @param expiredKeys list of expired keys
     * @param currentTime LocalDateTime object for current moment for a final check
     */
    private void performRemovalOfExpiredValues(List<Key> expiredKeys, LocalDateTime currentTime) {
        for(Key oneKey : expiredKeys) {
            performRemovalOfOneExpiredKey(oneKey, currentTime);
        }
    }

    /**
     * Tries to remove a key with an expired value from the memory, it will return true if the value is deleted
     * and false if the value is marked to expire after the input currentTime
     * @param oneKey key to remove
     * @param currentTime LocalDateTime object that will be compared to expiration time in the saved value
     * @return true if key value pair was deleted, false if it wasn't
     */
    private boolean performRemovalOfOneExpiredKey(Key oneKey, LocalDateTime currentTime) {
        if(currentTime.isBefore(this.storageLocation.get(oneKey).getExpirationMoment())) {
            return false;
        }
        this.storageLocation.remove(oneKey);
        return true;
    }

    /**
     * Getter for the amount of saved key value pairs
     * @return int sum of keys
     */
    public int getStorageSize() {
        return this.storageLocation.size();
    }

    /**
     * Getter for the LocalDateTime moment for what the last full cleanup checked against
     * @return LocalDateTime moment that last full cleanup was made with or null if there has never been a full cleanup
     */
    public LocalDateTime getLastCleanUpTime() {
        return this.lastCleanUpTime;
    }

    /**
     * Public method for calling the check and delete of one key value
     * @param oneKey Key that will be checked
     * @param currentTime the now time the values expiration is checked against
     * @return true if key was deleted from memory, false if not
     */
    public boolean callForCheckCleanOnKeyValue(Key oneKey, LocalDateTime currentTime) {
        return this.performRemovalOfOneExpiredKey(oneKey, currentTime);
    }
}
