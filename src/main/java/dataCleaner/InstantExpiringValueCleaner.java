package dataCleaner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * DataCleaner class that attempts to delete expired key value pairs from memory soon after they have expired
 * @param <Key> Key type used in target EvictionMap
 */
public class InstantExpiringValueCleaner<Key> extends DataCleanerBase<Key> {
    private List<ExpireNotice<Key>> listOfNotices;
    private ScheduledExecutorService executor;
    private boolean hasScehduleTask = false;

    /**
     * Constructor
     */
    public InstantExpiringValueCleaner() {
        this.listOfNotices = new ArrayList<>();
        this.setUpExecutorPool();
    }

    /**
     * Method for setting up the thread pool used for delete calls
     */
    private void setUpExecutorPool() {
        this.executor = Executors.newScheduledThreadPool(2);
    }

    @Override
    protected void dealWithNewAddedElement(Key addedKey, LocalDateTime momentItExpires) {
        this.addNewNoticeToStack(this.makeNewNoticeWithNewValues(addedKey, momentItExpires));
    }

    /**
     * For making a new Expire notice object
     * @param keyValue Key stored in the notice
     * @param expireTime TimeStamp stored in the notice
     * @return new expire notice type element
     */
    private ExpireNotice<Key> makeNewNoticeWithNewValues(Key keyValue, LocalDateTime expireTime) {
        return new ExpireNotice<>(keyValue, expireTime);
    }

    /**
     * script called for adding a new Expire notice
     * @param newNotice New notice added about possible next expiring key value
     */
    private void addNewNoticeToStack(ExpireNotice<Key> newNotice) {
        this.listOfNotices.add(newNotice);
        this.tryToCallExecutableTaskOnLastElement();
    }

    /**
     * Method that attempts to setup a scheduled task for the removal of a key in memory,
     *  will fail if there is no need for a new schedules task
     */
    private void tryToCallExecutableTaskOnLastElement() {
        if(this.listOfNotices.isEmpty()) {
            return;
        }
        if(this.hasScehduleTask) {
            return;
        }
        this.callExecutableTaskOnLastElement();
    }

    /**
     * Method that sets up a scheduled task,
     * this task will make a key removal call with the oldest ExpireNotice in the object
     */
    private void callExecutableTaskOnLastElement() {
        this.hasScehduleTask = true;
        this.executor.schedule(
                oneDeleteAttempt,
                getSecondsDifferenceWithNow(this.getOldestNotice().getExpireTime())+1,
                TimeUnit.SECONDS);
    }

    /**
     * Runnable taske that will make a key removal call with the oldest expire notice in the object
     */
    Runnable oneDeleteAttempt = () -> {
        this.callForCleanUpOnKey(this.getOldestNotice().getExpireKey());
        this.removeOldestExpireNotice();
        this.hasScehduleTask = false;
        this.tryToCallExecutableTaskOnLastElement();
    };

    /**
     * Gets the oldest ExpireNotice stored here, or null if there are none
     * @return oldest expire notice in listOfNotices
     */
    private ExpireNotice<Key> getOldestNotice() {
        return this.listOfNotices.get(0);
    }

    /**
     * Deletes the oldest ExpireNotice stored here
     */
    private void removeOldestExpireNotice() {
        this.listOfNotices.remove(0);
    }

    /**
     * Method for getting second difference between current moment and input moment
     * @param thenMoment moment compared to current moment
     * @return if then moment is in the future returns value of many seconds there are till then, if its in the past
     *          returns 0;
     */
    private long getSecondsDifferenceWithNow(LocalDateTime thenMoment) {
        long calculatedMoment = Duration.between(this.getCurrentMoment(), thenMoment).getSeconds();
        if(calculatedMoment < 0) {
            calculatedMoment = 0;
        }
        return calculatedMoment;
    }
}
