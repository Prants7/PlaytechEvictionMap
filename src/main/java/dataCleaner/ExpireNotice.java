package dataCleaner;

import java.time.LocalDateTime;

/**
 * Notice element used by InstantExpiringValueCleaner to mark down when a key might expire, unless it is extended
 * @param <Key> the type of key used in target EvictionMap
 */
public class ExpireNotice<Key> {
    private Key expireKey;
    private LocalDateTime expireTime;

    /**
     * Constructor
     * @param expireKey key that will expire
     * @param expireTime moment when it would expire
     */
    public ExpireNotice(Key expireKey, LocalDateTime expireTime) {
        this.expireKey = expireKey;
        this.expireTime = expireTime;
    }

    /**
     * getter for the key
     * @return saved key element
     */
    public Key getExpireKey() {
        return this.expireKey;
    }

    /**
     * getter for expire time
     * @return saved expire time
     */
    public LocalDateTime getExpireTime() {
        return this.expireTime;
    }
}
