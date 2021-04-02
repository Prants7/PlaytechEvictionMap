package dataCleaner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InstantExpiringValueCleaner<Key> extends DataCleanerBase<Key> {
    private List<ExpireNotice<Key>> listOfNotices;
    private ScheduledExecutorService executor;
    private boolean hasScehduleTask = false;

    public InstantExpiringValueCleaner() {
        this.listOfNotices = new ArrayList<>();
        this.setUpExecutorPool();
    }

    private void setUpExecutorPool() {
        this.executor = Executors.newScheduledThreadPool(2);
    }

    @Override
    protected void dealWithNewAddedElement(Key addedKey, LocalDateTime momentItExpires) {
        this.addNewNoticeToStack(this.makeNewNoticeWithNewValues(addedKey, momentItExpires));
    }

    private ExpireNotice<Key> makeNewNoticeWithNewValues(Key keyValue, LocalDateTime expireTime) {
        return new ExpireNotice<>(keyValue, expireTime);
    }

    private void addNewNoticeToStack(ExpireNotice<Key> newNotice) {
        this.listOfNotices.add(newNotice);
        this.tryToCallExecutableTaskOnLastElement();
    }

    private void tryToCallExecutableTaskOnLastElement() {
        if(this.listOfNotices.isEmpty()) {
            return;
        }
        if(this.hasScehduleTask) {
            return;
        }
        this.callExecutableTaskOnLastElement();
    }

   /* private void callExecutableTaskOnLastElement() {
        this.hasScehduleTask = true;
        this.executor.schedule(
                oneDeleteAttempt,
                this.turnSecondsToMilliSeconds(getSecondsDifferenceWithNow(this.getOldestNotice().getExpireTime()))+100,
                TimeUnit.MILLISECONDS);
    }*/

    private void callExecutableTaskOnLastElement() {
        this.hasScehduleTask = true;
        this.executor.schedule(
                oneDeleteAttempt,
                getSecondsDifferenceWithNow(this.getOldestNotice().getExpireTime())+1,
                TimeUnit.SECONDS);
    }

    Runnable oneDeleteAttempt = () -> {
        this.callForCleanUpOnKey(this.getOldestNotice().getExpireKey());
        this.removeOldestExpireNotice();
        this.hasScehduleTask = false;
        this.tryToCallExecutableTaskOnLastElement();
    };

    private ExpireNotice<Key> getOldestNotice() {
        return this.listOfNotices.get(0);
    }

    private void removeOldestExpireNotice() {
        this.listOfNotices.remove(0);
    }

    /*private long getMilliSecondsDifferenceWithNow(LocalDateTime thenMoment) {
        Duration durationElement = Duration.between(this.getCurrentMoment(), thenMoment);
        //Long calculatedMoment = Duration.between(this.getCurrentMoment(), thenMoment).getSeconds();
        Long nanoSeconds =  durationElement.getSeconds();
        /*if(calculatedMoment < 0) {
            calculatedMoment = (long) 0;
        }
        if(milliSeconds < 0) {
            milliSeconds = (long) 0;
        }
        return milliSeconds+1;
    }*/

    private long getSecondsDifferenceWithNow(LocalDateTime thenMoment) {
        long calculatedMoment = Duration.between(this.getCurrentMoment(), thenMoment).getSeconds();
        if(calculatedMoment < 0) {
            calculatedMoment = (long) 0;
        }
        return calculatedMoment;
    }

    private long turnSecondsToMilliSeconds(long seconds) {
        return seconds * 1000;
    }
}
