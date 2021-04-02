package dataCleaner;

import java.time.LocalDateTime;

public class ExpireNotice<Key> {
    private Key expireKey;
    private LocalDateTime expireTime;

    public ExpireNotice(Key expireKey, LocalDateTime expireTime) {
        this.expireKey = expireKey;
        this.expireTime = expireTime;
    }

    public Key getExpireKey() {
        return this.expireKey;
    }

    public LocalDateTime getExpireTime() {
        return this.expireTime;
    }
}
