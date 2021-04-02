package implementation;

public interface EvictionMap<Key, Value> {

    public void put(Key keyElement, Value valueElement);

    public Value get(Key searchKey);

    public int getAmoundOfSavedValues();
}
