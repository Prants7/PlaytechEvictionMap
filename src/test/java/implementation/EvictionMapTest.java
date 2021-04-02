package implementation;

import dataCleaner.DataCleaner;
import dataCleaner.InstantExpiringValueCleaner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Main test class for whatever implementation of Eviction map is setup in the private methods
 */
class EvictionMapTest {

    /**
     * For assigning the specific EvictionMap instance for testing
     * @param seconds expiration seconds assigned to the new object
     * @return instance of EvictionMap that will be tested
     */
    private EvictionMap<String, String> getTestableElementStringValues(long seconds) {
        return new EvictionMapImplementation<String, String>(seconds);
    }

    /**
     * For assigning the specific EvictionMap instance for testing
     * @param seconds expiration seconds assigned to the new object
     * @param insertDataCleaner DataCleaner object assigned to the new map
     * @return instance of EvictionMap for testing
     */
    private EvictionMap<String, String> getTestableElementStringValues(long seconds, DataCleaner<String> insertDataCleaner) {
        return new EvictionMapImplementation<>(seconds, insertDataCleaner);
    }

    /**
     * Used in a test that checks if different key elements still work
     * @param seconds expiration seconds assigned to the new object
     * @return instance of EvictionMap for testing
     */
    private EvictionMap<Integer, String>  getTestableElementWithADifferentKeyType(long seconds) {
        return new EvictionMapImplementation<>(seconds);
    }

    /**
     * Used in a test that checks if different value types still work
     * @param seconds expiration seconds assigned to the new object
     * @return instance of EvictionMap for testing
     */
    private EvictionMap<String, Integer>  getTestableElementWithADifferentValueTyoe(long seconds) {
        return new EvictionMapImplementation<>(seconds);
    }

    /**
     * Getter for InstantDataCleaner type of DataCleaner used in a test
     * @return InstantDataCleaner for String type of keys
     */
    private InstantExpiringValueCleaner<String> getTestableInstantDataCleaner() {
        return new InstantExpiringValueCleaner<>();
    }

    @Test
    public void canPutAndReturnElementsFromMap() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(30);
        element.put("key", "value");
        assertEquals("value", element.get("key"));
    }

    @Test
    public void canMakeAndUseMapsWithDifferentKeyType() {
        EvictionMap<Integer, String> elementIntegerKey  = this.getTestableElementWithADifferentKeyType(30);
        elementIntegerKey.put(5, "value");
        assertEquals("value", elementIntegerKey.get(5));
    }

    @Test
    public void canMakeAndUseMapsWithDifferentValueType() {
        EvictionMap<String, Integer> elementIntegerKey  = this.getTestableElementWithADifferentValueTyoe(30);
        elementIntegerKey.put("key", 5);
        assertEquals(5, elementIntegerKey.get("key"));
    }

    @Test
    public void canPutMultipleElementsIntoMap() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(30);
        element.put("key", "value");
        element.put("key2", "value2");
        assertEquals("value", element.get("key"));
        assertEquals("value2", element.get("key2"));
    }

    @Test
    public void canChangeKeysValue() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(30);
        element.put("key", "value");
        element.put("key", "value2");
        assertEquals("value2", element.get("key"));
    }

    @Test
    public void elementsExpireAfterTime() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(1);
        element.put("key", "value");
        try {
            Thread.sleep(1100);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }

        assertNull(element.get("key"));
    }

    @Test
    public void elementValueChangingResetsExpirationTime() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(1);
        element.put("key", "value");
        try {
            Thread.sleep(700);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }
        element.put("key", "value2");

        try {
            Thread.sleep(500);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }
        assertEquals("value2", element.get("key"));
    }

    @Test
    public void elementsCleanedAfterExpireWithInstantCleaner() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(3, this.getTestableInstantDataCleaner());
        element.put("key", "value");
        assertEquals(1, element.getAmountOfSavedValues());
        try {
            Thread.sleep(4100);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }
        assertNull(element.get("key"));
        assertEquals(0, element.getAmountOfSavedValues());
    }

    @Test
    public void elementsCleanedAfterExpireWithInstantCleanerSeveralEntriesVersion() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(3, this.getTestableInstantDataCleaner());
        element.put("key1", "value1");
        try {
            Thread.sleep(2000);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }
        element.put("key2", "value2");
        try {
            Thread.sleep(4100);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }
        assertEquals(0, element.getAmountOfSavedValues());
    }

}