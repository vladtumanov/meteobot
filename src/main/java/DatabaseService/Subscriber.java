package DatabaseService;

import java.time.LocalDateTime;

public class Subscriber {
    int id;
    long chat_id;
    String city;
    double lat;
    double lon;
    int utc;
    boolean is_active;
    int count;
    LocalDateTime last_message;

    public Subscriber(int id, long chat_id, String city, double lat, double lon,
                      int utc, boolean is_active, int count, LocalDateTime last_message) {
        this.id = id;
        this.chat_id = chat_id;
        this.lat = lat;
        this.lon = lon;
        this.city = city;
        this.utc = utc;
        this.is_active = is_active;
        this.count = count;
        this.last_message = last_message;
    }

    public int getId() {
        return id;
    }

    public long getChatId() {
        return chat_id;
    }

    public String getCity() {
        return city;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getUtc() {
        return utc;
    }

    public boolean isIsActive() {
        return is_active;
    }

    public int getCount() {
        return count;
    }

    public LocalDateTime getLastMessage() {
        return last_message;
    }
}
