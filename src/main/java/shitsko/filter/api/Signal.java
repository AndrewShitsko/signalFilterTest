package shitsko.filter.api;

/**
 * Created by AnriShitsko on 04.05.2016.
 */
public class Signal {
    private int id;
    private long sentTime;
    private long expiredTime;

    public Signal(int id, long sentTime, long expiredTime) {
        this.id = id;
        this.sentTime = sentTime;
        this.expiredTime = expiredTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long time) {
        this.sentTime = sentTime;
    }

    public long getExpiredTime() { return expiredTime; }

    public void setExpiredTime(long expiredTime) { this.expiredTime = expiredTime; }
}
