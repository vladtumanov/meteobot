package WeatherService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DailyOpenWeatherMap extends WeatherService {

    private static volatile DailyOpenWeatherMap instance;
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=hourly&appid=%s";
    private static final String API_KEY;

    static {
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("src/main/resources/local.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        API_KEY = properties.getProperty("weather_token");
    }

    private DailyOpenWeatherMap() {
        super(API_URL, API_KEY);
    }

    private static DailyOpenWeatherMap getInstance() {
        DailyOpenWeatherMap currentInstance;
        if (instance == null) {
            synchronized (DailyOpenWeatherMap.class) {
                if (instance == null) {
                    instance = new DailyOpenWeatherMap();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public synchronized static DailyMeteoData getMeteoData(double lat, double lon) {
        return getInstance().getWeather(lat, lon, DailyMeteoData.class);
    }
}
