package implementation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvictionMapTest {

    private EvictionMap<String, String> getTestableElementStringValues(long seconds) {
        return new EvictionMapImplementation<String, String>(seconds);
    }

    @Test
    public void canPutAndReturnElementsFromMap() {
        EvictionMap<String, String> element = this.getTestableElementStringValues(30);
        element.put("key", "value");
        assertEquals("value", element.get("key"));
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

}