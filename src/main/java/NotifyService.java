import DatabaseService.Command;
import DatabaseService.Subscriber;
import WeatherService.DailyMeteoData;
import WeatherService.DailyOpenWeatherMap;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class NotifyService extends Thread {
    private final Bot bot;
    /**
     * Поле, указывающеее, в котором часу отправлять уведомления.
     */
    private final int NOTIFY_HOUR = 7;
    /**
     * Поле, указывающее через сколько часов ещё можно отправить уведомление, если по каким-то причиноам это не удалось.
     */
    private final int NOTIFY_AFTER = 2;
    /**
     * Интервал времени, в который осуществляется рассылка уведомлений
     */
    private final int SLEEP = 5 * 60 * 1000;

    public NotifyService(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(5000); //Time for initialize bot
            while (Thread.currentThread().isAlive()) {
                sendNotify();
                Thread.sleep(SLEEP);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Рассылка уведомлений подписчикам.
     */
    private void sendNotify() {
        try {
            for (var sub : getSubscribers()) {
                DailyMeteoData md = DailyOpenWeatherMap.getMeteoData(sub.getLat(), sub.getLon());
                if (md.getCode() == 0) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(sub.getChatId());
                    sendMessage.setText(String.format("Good morning!%nWeather today in %s%n%s", sub.getCity(), md.toString()));
                    bot.execute(sendMessage);
                    Command.updateLastMessage(sub);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение списка подписчиков, которым пришло время отправить уведомление.
     * @return Спикок подписчиков
     */
    private List<Subscriber> getSubscribers() {
        List<Subscriber> subscribers = Command.getAllActive();
        subscribers.removeIf((subscriber -> {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.ofTotalSeconds(subscriber.getUtc()));
            boolean isMorning = localDateTime.getHour() >= NOTIFY_HOUR &&
                    localDateTime.getHour() <= NOTIFY_HOUR + NOTIFY_AFTER;
            boolean notifyToday = ChronoUnit.DAYS.between(subscriber.getLastMessage(),
                    LocalDateTime.now()) > 0;
            return !(isMorning && notifyToday);
        }));
        return subscribers;
    }
}
