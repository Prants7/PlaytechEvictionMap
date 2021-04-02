package implementation;

/**
 * Interface for the main functionality of this project
 * @param <Key> the type of elements used as keys in this map
 * @param <Value> the type of elements used as values in this map
 */
public interface EvictionMap<Key, Value> {

    /**
     * For adding a new key value pair into the map
     * @param keyElement keyElement that can be used to find the value while it hasn't expired
     * @param valueElement valueElement that will be stored under the key
     */
    public void put(Key keyElement, Value valueElement);

    /**
     * For asking for value stored under a key
     * @param searchKey key that will be searched for
     * @return Value stored under the key or null if there is none or the value has expired
     */
    public Value get(Key searchKey);

    /**
     * For getting the amount of keys saved, also includes expired but not deleted keys
     * @return int for the sum of all keys still saved
     */
    public int getAmountOfSavedValues();
}
