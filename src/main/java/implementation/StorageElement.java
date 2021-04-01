package implementation;

import java.util.Hashtable;

public class StorageElement<Key, Value> {
    private Hashtable<Key, ValueAndExpirationPair<Value>> storageLocation;

    public StorageElement() {
        this.storageLocation = new Hashtable<>();
    }

    public void put(Key keyElement, ValueAndExpirationPair<Value> newValuePair) {
        this.storageLocation.put(keyElement, newValuePair);
    }

    public ValueAndExpirationPair<Value> get(Key keyElement) {
        return this.storageLocation.get(keyElement);
    }
}
