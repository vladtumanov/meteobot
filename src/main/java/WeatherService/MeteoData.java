package WeatherService;

import java.util.LinkedList;

// {"coord":{"lon":30.26,"lat":59.89},
// "weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02n"}],"base":"stations",
// "main":{"temp":288.15,"feels_like":286.65,"temp_min":288.15,"temp_max":288.15,"pressure":1001,"humidity":82},"visibility":10000,
// "wind":{"speed":3,"deg":220},"clouds":{"all":20},"dt":1593638968,
// "sys":{"type":1,"id":8926,"country":"RU","sunrise":1593650624,"sunset":1593717723},"timezone":10800,"id":498817,"name":"Saint Petersburg","cod":200}

public class MeteoData {
    private Coord coord;
    private LinkedList<Weather> weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
    private String message;

    public class Coord {
        public double lon;
        public double lat;
    }

    public class Weather {
        public int id;
        public String main;
        public String description;
        public String icon;
    }

    public class Main {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public int pressure;
        public int humidity;
    }

    public class Wind {
        public double speed;
        public int deg;
    }

    public class Clouds {
        public int all;
    }

    public class Sys {
        public int type;
        public int id;
        public String country;
        public int sunrise;
        public int sunset;
    }

    public String getCity() {
        return name;
    }

    public String getCountry() {
        return sys.country;
    }

    public double getLon() {
        return coord.lon;
    }

    public double getLat() {
        return coord.lat;
    }

    public double getMax() {
        return main.temp_max - 273;
    }

    public double getMin() {
        return main.temp_min - 273;
    }

    public double getNow() {
        return main.temp - 273;
    }

    public String getDescription() {
        return weather.getFirst().description;
    }

    public String getWindVector() {
        String result = "";

        if (wind.deg < 68 || wind.deg > 292)
            result = "N";
        if (wind.deg > 112 && wind.deg < 248)
            result = "S";
        if (wind.deg > 22 && wind.deg < 158)
            result += "E";
        if (wind.deg > 202 && wind.deg < 338)
            result += "W";
        return result;
    }

    public int getSpeed() {
        return (int)wind.speed;
    }

    public double getPressure() {
        return (main.pressure * 0.750063755419211);
    }

    public int getTimezone() { return timezone; }

    public int getCode() { return cod; }

    public String getMessage() { return message; }

    @Override
    public String toString() {
        return String.format("""
                        %s (%s)
                        Now: %s, %+.1f°C
                        Wind %s, %d m/s
                        Pressure %.0f mm.Hg""",
                getCity(), getCountry(), getDescription(), getNow(),
                getWindVector(), getSpeed(), getPressure());
    }
}
