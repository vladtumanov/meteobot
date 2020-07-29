package WeatherService;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class WeatherService {
    private final String API_URL;
    private final String API_KEY;
    private int CONNECTION_TIMEOUT = 5000;

    WeatherService(String API_URL, String API_KEY) {
        this.API_URL = API_URL;
        this.API_KEY = API_KEY;
    }

    /**
     * .
     * @param city city for request
     * @param weatherClass class for build object from Json
     * @param <T> Type weather objects
     * @return Weather objects.
     */
    protected final <T> T getWeather(String city, Class<T> weatherClass) {
        T weatherObject = null;
        Gson gson = new Gson();

        try {
            URL url = getUrl(city);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getStream(url), StandardCharsets.UTF_8))) {
                weatherObject = gson.fromJson(reader, weatherClass);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherObject;
    }

    protected final <T> T getWeather(double lat, double lon, Class<T> weatherClass) {
        T weatherObject = null;
        Gson gson = new Gson();

        try {
            URL url = new URL(String.format(API_URL, lat, lon, API_KEY));
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getStream(url), StandardCharsets.UTF_8))) {
                weatherObject = gson.fromJson(reader, weatherClass);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherObject;
    }

    /**
     * Method returns Http InputStream from URL.
     * @param url correct url with api, api key and city
     * @return InputStream
     * @throws IOException
     */
    private InputStream getStream(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setReadTimeout(CONNECTION_TIMEOUT);

        return conn.getResponseCode() == 200 ?
                conn.getInputStream() :
                conn.getErrorStream();
    }

    /**
     * Method returns correct Url address with api, api key and city.
     * @param city
     * @return URL
     * @throws MalformedURLException
     */
    private URL getUrl(String city) throws MalformedURLException {
        return new URL(String.format(API_URL, URLEncoder.encode(city, StandardCharsets.UTF_8), API_KEY));
    }

    /**
     * Set timeout connection. Default 5000 ms.
     * @param timeout Milliseconds
     */
    public final void setTimeout(int timeout) {
        this.CONNECTION_TIMEOUT = timeout;
    }
}
