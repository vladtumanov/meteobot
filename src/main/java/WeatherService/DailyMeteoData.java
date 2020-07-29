package WeatherService;

import java.util.LinkedList;
import java.util.List;

public class DailyMeteoData {

    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private Current current;
    private LinkedList<Daily> daily = null;
    private int cod;
    private String message;

    @Override
    public String toString() {
        return String.format("""
                        Now: %s, %+.1f°C
                        Max. %+.1f°C. Min. %+.1f°C
                        Wind %s, %d m/s
                        Pressure %.0f mm.Hg""",
                getDescription(), getNow(),
                getMaxToday(), getMinToday(), getWindVector(), getSpeed(),
                getPressure());
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getMaxToday() {
        return daily.getFirst().temp.max - 273;
    }

    public double getMinToday() {
        return daily.getFirst().temp.min - 273;
    }

    public double getNow() {
        return current.temp - 273;
    }

    public String getDescription() {
        return daily.getFirst().weather.getFirst().description;
    }

    public String getWindVector() {
        String result = "";
        int wind = daily.getFirst().wind_deg;

        if (wind < 68 || wind > 292)
            result = "N";
        if (wind > 112 && wind < 248)
            result = "S";
        if (wind > 22 && wind < 158)
            result += "E";
        if (wind > 202 && wind < 338)
            result += "W";
        return result;
    }

    public int getSpeed() {
        return (int) daily.getFirst().wind_speed;
    }

    public double getPressure() {
        return (daily.getFirst().pressure * 0.750063755419211);
    }

    public int getTimezone() {
        return timezone_offset;
    }

    public int getCode() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    class Current {

        private int dt;
        private int sunrise;
        private int sunset;
        private double temp;
        private double feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double uvi;
        private int clouds;
        private int visibility;
        private double wind_speed;
        private int wind_deg;
        private List<Weather> weather = null;

    }

    class Daily {

        private int dt;
        private int sunrise;
        private int sunset;
        private Temp temp;
        private FeelsLike feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double wind_speed;
        private int wind_deg;
        private LinkedList<Weather> weather = null;
        private int clouds;
        private double rain;
        private double uvi;

    }

    class FeelsLike {

        private double day;
        private double night;
        private double eve;
        private double morn;

    }

    class Temp {

        private double day;
        private double min;
        private double max;
        private double night;
        private double eve;
        private double morn;

    }

    class Weather {

        private int id;
        private String main;
        private String description;
        private String icon;

    }
}

/*
{"lat":55.75,"lon":37.62,"timezone":"Europe/Moscow","timezone_offset":10800,"current":{"dt":1594570863,"sunrise":1594515724,"sunset":1594577272,"temp":294.74,"feels_like":294.83,"pressure":1014,"humidity":73,"dew_point":289.7,"uvi":6.77,"clouds":40,"visibility":10000,"wind_speed":3,"wind_deg":320,"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}]},"daily":[{"dt":1594544400,"sunrise":1594515724,"sunset":1594577272,"temp":{"day":294.74,"min":292.19,"max":294.92,"night":292.19,"eve":294.74,"morn":294.74},"feels_like":{"day":295.86,"night":292.14,"eve":295.86,"morn":295.86},"pressure":1014,"humidity":73,"dew_point":289.7,"wind_speed":1.53,"wind_deg":241,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"clouds":40,"rain":5.5,"uvi":6.77},{"dt":1594630800,"sunrise":1594602207,"sunset":1594663603,"temp":{"day":293.06,"min":290.66,"max":297.3,"night":290.66,"eve":294.27,"morn":291.04},"feels_like":{"day":292.31,"night":289.02,"eve":292.5,"morn":291.02},"pressure":1011,"humidity":74,"dew_point":288.4,"wind_speed":3.45,"wind_deg":4,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"clouds":96,"rain":5.97,"uvi":6.64},{"dt":1594717200,"sunrise":1594688691,"sunset":1594749932,"temp":{"day":291.06,"min":290.22,"max":291.06,"night":290.39,"eve":290.51,"morn":290.22},"feels_like":{"day":289.8,"night":289.76,"eve":288.15,"morn":288.06},"pressure":1007,"humidity":84,"dew_point":288.33,"wind_speed":4.2,"wind_deg":13,"weather":[{"id":502,"main":"Rain","description":"heavy intensity rain","icon":"10d"}],"clouds":100,"rain":14.98,"uvi":6.53},{"dt":1594803600,"sunrise":1594775179,"sunset":1594836257,"temp":{"day":292.63,"min":290.12,"max":294.36,"night":290.12,"eve":294.36,"morn":290.64},"feels_like":{"day":293.32,"night":287.88,"eve":295.56,"morn":292.2},"pressure":1000,"humidity":79,"dew_point":289.04,"wind_speed":1.71,"wind_deg":114,"weather":[{"id":502,"main":"Rain","description":"heavy intensity rain","icon":"10d"}],"clouds":100,"rain":19.69,"uvi":6.19},{"dt":1594890000,"sunrise":1594861668,"sunset":1594922579,"temp":{"day":289.52,"min":287.5,"max":289.89,"night":287.5,"eve":289.85,"morn":288.89},"feels_like":{"day":287.28,"night":284.61,"eve":286.85,"morn":286.56},"pressure":1003,"humidity":82,"dew_point":286.55,"wind_speed":4.67,"wind_deg":276,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":100,"rain":0.85,"uvi":5.87},{"dt":1594976400,"sunrise":1594948159,"sunset":1595008899,"temp":{"day":289.8,"min":286.54,"max":291.97,"night":289.02,"eve":291.97,"morn":286.54},"feels_like":{"day":287.1,"night":288.59,"eve":289.92,"morn":283.63},"pressure":1008,"humidity":65,"dew_point":283.39,"wind_speed":3.94,"wind_deg":276,"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"clouds":97,"uvi":6.06},{"dt":1595062800,"sunrise":1595034652,"sunset":1595095215,"temp":{"day":290.26,"min":288.16,"max":294.52,"night":289.97,"eve":294.51,"morn":288.16},"feels_like":{"day":289.54,"night":289.25,"eve":293.75,"morn":286.99},"pressure":1011,"humidity":73,"dew_point":285.48,"wind_speed":2.01,"wind_deg":32,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":100,"rain":1.76,"uvi":5.76},{"dt":1595149200,"sunrise":1595121147,"sunset":1595181529,"temp":{"day":297.75,"min":288.96,"max":299.26,"night":291.22,"eve":295.79,"morn":288.96},"feels_like":{"day":296.61,"night":291.09,"eve":294.6,"morn":288.24},"pressure":1010,"humidity":50,"dew_point":286.87,"wind_speed":3.18,"wind_deg":130,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"clouds":100,"rain":2.84,"uvi":5.72}]}
 */