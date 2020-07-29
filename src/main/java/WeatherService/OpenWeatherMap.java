package WeatherService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OpenWeatherMap extends WeatherService {

    private static volatile OpenWeatherMap instance;
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
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

    private OpenWeatherMap() {
        super(API_URL, API_KEY);
    }


    private static OpenWeatherMap getInstance() {
        OpenWeatherMap currentInstance;
        if (instance == null) {
            synchronized (OpenWeatherMap.class) {
                if (instance == null) {
                    instance = new OpenWeatherMap();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public synchronized static MeteoData getMeteoData(String city) {
        return getInstance().getWeather(city, MeteoData.class);
    }
}
