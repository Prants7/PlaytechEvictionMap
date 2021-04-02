package extraTests;

import implementation.StorageElement;
import implementation.TimeBox;
import implementation.ValueAndExpirationPair;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StorageElementTest {
    private TimeBox wrappedTimeBox = new TimeBox();

    private StorageElement<String, String> getTestableStorageElement() {
        return new StorageElement<String, String>();
    }

    private ValueAndExpirationPair<String> getTestTableValueAndExpirationPair(
            String valueString,
            LocalDateTime expirationTime) {
        return new ValueAndExpirationPair<>(valueString, expirationTime);
    }



    @Test
    public void putAndGetWorkForStorageElement() {
        StorageElement<String, String> testableElement = this.getTestableStorageElement();
        ValueAndExpirationPair<String> testValueAndExpirationPair =
                this.getTestTableValueAndExpirationPair("value", this.wrappedTimeBox.getCurrentTimeMoment());
        testableElement.put("key", testValueAndExpirationPair);
        assertEquals(testValueAndExpirationPair, testableElement.get("key"));
    }

    @Test
    public void canGetStorageElementSize() {
        StorageElement<String, String>  testableElement = this.getTestableStorageElement();
        assertEquals(0, testableElement.getStorageSize());
        testableElement.put("key",
                this.getTestTableValueAndExpirationPair("value", this.wrappedTimeBox.getCurrentTimeMoment()));
        testableElement.put("key2",
                this.getTestTableValueAndExpirationPair("value2", this.wrappedTimeBox.getCurrentTimeMoment()));
        assertEquals(2, testableElement.getStorageSize());
    }

    @Test
    public void canPerformStorageCleanupThatRemovesExpiredValues() {
        StorageElement<String, String>  testableElement = this.getTestableStorageElement();
        LocalDateTime checkTime = this.wrappedTimeBox.getCurrentTimeMoment();
        LocalDateTime expiredTime = checkTime.minus(10, ChronoUnit.SECONDS);
        LocalDateTime notExpiredTime = checkTime.plus(10, ChronoUnit.SECONDS);

        testableElement.put("expiredKey",
                this.getTestTableValueAndExpirationPair("value", expiredTime));
        testableElement.put("freshKey",
                this.getTestTableValueAndExpirationPair("value", notExpiredTime));
        assertEquals(2, testableElement.getStorageSize());
        testableElement.performCleanUp(checkTime);
        assertEquals(1, testableElement.getStorageSize());
    }

    @Test
    public void willSaveAndChangeLastCleanUpTime() {
        StorageElement<String, String>  testableElement = this.getTestableStorageElement();

        assertNull(testableElement.getLastCleanUpTime());

        LocalDateTime checkTime1 = this.wrappedTimeBox.getCurrentTimeMoment();
        testableElement.performCleanUp(checkTime1);
        assertEquals(checkTime1, testableElement.getLastCleanUpTime());

        LocalDateTime checkTime2 = checkTime1.plus(10, ChronoUnit.SECONDS);
        testableElement.performCleanUp(checkTime2);
        assertEquals(checkTime2, testableElement.getLastCleanUpTime());


    }
}
