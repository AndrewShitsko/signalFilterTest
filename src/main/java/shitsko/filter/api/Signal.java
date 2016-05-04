package shitsko.filter.api;

/**
 * Created by AnriShitsko on 04.05.2016.
 */
public class Signal {
    private int id;
    private long time;

    public Signal(int id, long time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
