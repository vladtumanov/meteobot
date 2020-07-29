import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {

    public static void main(String[] args) {
        // Инициализация Telegram Api
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            Bot bot = new Bot();
            botsApi.registerBot(bot);
            // Создание и запуск службы уведомлений
            NotifyService notifyService = new NotifyService(bot);
            notifyService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
