import DatabaseService.Command;
import WeatherService.MeteoData;
import WeatherService.OpenWeatherMap;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Bot extends TelegramLongPollingBot {
    /**
     * Поле для хранения последнего успешного запроса
     */
    private final Map<User, MeteoData> lastRequest = new HashMap<>();

    /**
     * Метод, для получения токена. Токен хранится в файле local.properties
     *
     * @return Telegram api token
     */
    @Override
    public String getBotToken() {
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("src/main/resources/local.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("telegram_token");
    }

    public String getBotUsername() {
        return "MeteoBot";
    }

    /**
     * Метод, вызывающий обработчик входящих сообщений
     *
     * @param update
     */
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handleIncomingMessage(update.getMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Обработка входящих сообщений как команд для дальнейших действий
     *
     * @param message Входящиее сообщение
     * @throws TelegramApiException
     */
    private void handleIncomingMessage(Message message) throws TelegramApiException {
        String response = switch (message.getText()) {
            case "/start" -> "Добро пожаловать!\nВведите имя города на латинице";
            case "/subscribe" -> subscribe(message);
            case "/unsubscribe", "/stop" -> unsubscribe(message);
            default -> sendCurrent(message);
        };
        if (response != null)
            sendMsg(message.getChatId(), response);
    }

    /**
     * Метод для отправки сообщений.
     *
     * @param chatId
     * @param text   Отправляемое сообщение
     * @throws TelegramApiException
     */
    private void sendMsg(long chatId, String text) throws TelegramApiException {
        if (text.isBlank()) return;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        execute(sendMessage);
    }

    /**
     * Метод добавляет соответствующую запись о подписке в базу данных.
     *
     * @param message Сообщение того, кто отправил запрос на подписку
     * @return Возвращает сообщение о результате операции
     */
    private String subscribe(Message message) {
        String result;

        if (lastRequest.containsKey(message.getFrom())) {
            long chatId = message.getChatId();
            MeteoData last = lastRequest.get(message.getFrom());
            Command.insert(chatId, last.getCity(), last.getLat(), last.getLon(), last.getTimezone());
            result = "Подписка активирована";
        } else {
            result = "Сперва нужно отправить имя города. Подписка будет оформлена по последнему запросу";
        }
        return result;
    }

    /**
     * Метод, удаляющий вся активные подписки для ползователя.
     *
     * @param message Сообщение пользователя
     * @return Результат выполнямой операции, отправляемый обратно пользователю
     */
    private String unsubscribe(Message message) {
        String result = "Вы отключили все подписки";
        Command.delete(message.getChatId());
        return result;
    }

    /**
     * Метод, отправляющий сообщение о текущей погоде. Возвращает null для совместимости со switch-выражениями.
     *
     * @param message Сообщение пользователя
     * @return null
     */
    private String sendCurrent(Message message) {
        Thread thread = new Thread(() -> {
            try {
                sendMsg(message.getChatId(), currentWeather(message));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return null;
    }

    /**
     * Метод, для получения строкового представления погоды.
     *
     * @param message Сообщение пользователя
     * @return Возвращает строковое представление погоды
     */
    private synchronized String currentWeather(Message message) {
        MeteoData md = OpenWeatherMap.getMeteoData(message.getText());
        if (md.getCode() == 200) {
            lastRequest.put(message.getFrom(), md);
            return md.toString();
        } else {
            return md.getMessage();
        }
    }
}