package implementation;

import dataCleaner.DataCleaner;
import dataCleaner.InstantExpiringValueCleaner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvictionMapTest {

    private EvictionMap<String, String> getTestableElementStringValues(long seconds) {
        return new EvictionMapImplementation<String, String>(seconds);
    }

    private EvictionMap<String, String> getTestableElementStringValues(long seconds, DataCleaner<String> insertDataCleaner) {
        return new EvictionMapImplementation<>(seconds, insertDataCleaner);
    }

    private EvictionMap<Integer, String>  getTestableElementWithADifferentKeyType(long seconds) {
        return new EvictionMapImplementation<>(seconds);
    }

    private EvictionMap<String, Integer>  getTestableElementWithADifferentValueTyoe(long seconds) {
        return new EvictionMapImplementation<>(seconds);
    }

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
        assertEquals(1, element.getAmoundOfSavedValues());
        try {
            Thread.sleep(4100);
        }
        catch (Exception exception) {
            System.out.println("Test was interrupted");
            fail();
        }
        assertNull(element.get("key"));
        assertEquals(0, element.getAmoundOfSavedValues());
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
        assertEquals(0, element.getAmoundOfSavedValues());
    }

}