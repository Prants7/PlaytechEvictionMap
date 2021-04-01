package implementation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EvictionMapImplementation<Key, Value> implements EvictionMap<Key, Value> {
    private long timeOutSeconds;
    private List<StoredValue<Key, Value>> elementStorage = new ArrayList<>();

    public EvictionMapImplementation(long timeOutSeconds) {
        this.timeOutSeconds = timeOutSeconds;
    }

    @Override
    public void put(Key keyElement, Value valueElement) {
        this.putScript(keyElement, valueElement);
    }

    private void putScript(Key keyElement, Value valueElement) {
        //System.out.println("Put script, currentTime seconds: "+this.getCurrentTime().getSecond()+" set timeout: "+this.giveNewTimeOutMoment().getSecond());
        if(this.hasKey(keyElement)) {
            this.changeOldElement(keyElement, valueElement);
        }
        else {
            this.addNewElement(keyElement, valueElement);
        }
    }

    private boolean hasKey(Key searchedKey) {
        for(StoredValue<Key, Value> oneStorageUnit : this.elementStorage) {
            if(oneStorageUnit.matchesKey(searchedKey)) {
                return true;
            }
        }
        return false;
    }

    private StoredValue<Key, Value> getStoredValueWithKey(Key searchedKey) {
        for(StoredValue<Key, Value> oneStorageUnit : this.elementStorage) {
            if(oneStorageUnit.matchesKey(searchedKey)) {
                return oneStorageUnit;
            }
        }
        return null;
    }

    private void addNewElement(Key keyElement, Value valueElement) {
        this.elementStorage.add(new StoredValue<>(keyElement, valueElement, this.giveNewTimeOutMoment()));
    }

    private LocalTime giveNewTimeOutMoment() {
        return this.getCurrentTime().plus(this.timeOutSeconds, ChronoUnit.SECONDS);
    }

    private LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    private boolean isStoredValueFresh(StoredValue<Key, Value> valueToCheck) {
        return this.getCurrentTime().isBefore(valueToCheck.getTimeOutMoment());
    }

    private void changeOldElement(Key keyElement, Value newValue) {
        StoredValue<Key, Value> foundStoredElement = this.getStoredValueWithKey(keyElement);
        if(foundStoredElement == null) {
            return;
        }
        foundStoredElement.saveNewValue(newValue, this.giveNewTimeOutMoment());
    }

    @Override
    public Value get(Key searchKey) {
        StoredValue<Key, Value> foundValue = this.getStoredValueWithKey(searchKey);
        if(foundValue == null) {
            return null;
        }
        if(!isStoredValueFresh(foundValue)) {
            return null;
        }
        return foundValue.getValue();
    }
}
