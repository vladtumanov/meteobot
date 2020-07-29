package DatabaseService;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;

public class Command {
    /**
     * Метод, добавляющий новых подписчиков. Если подписка уже имеется в базе, то обновляется счётчик уведомлений
     * @param chatId
     * @param city
     * @param lat Координаты города
     * @param lon Координаты города
     * @param timeUtc
     */
    public static void insert(long chatId, String city, double lat, double lon, int timeUtc) {
        String sql = "INSERT INTO Subscribers (chat_id, city, lat, lon, utc) VALUES (?, ?, ?, ?, ?)";
        String sql_upd = "UPDATE Subscribers SET count = ?, last_message = ? WHERE chat_id = ? AND city = ?";
        try (Connection connection = ConnectionService.connect()) {
            PreparedStatement ps = connection.prepareStatement(sql_upd);
            ps.setInt(1, 60);
            ps.setString(2, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)).toString());
            ps.setLong(3, chatId);
            ps.setString(4, city);
            // Если ни одной строки не обновилось, то значит, что записи в базе не было
            if (ps.executeUpdate() == 0) {
                ps = connection.prepareStatement(sql);
                ps.setLong(1, chatId);
                ps.setString(2, city);
                ps.setDouble(3, lat);
                ps.setDouble(4, lon);
                ps.setInt(5, timeUtc);
                ps.execute();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // get all active subscribers as List
    public static LinkedList<Subscriber> getAllActive() {
        LinkedList<Subscriber> subscriberList = new LinkedList<>();
        String sql = String.format("SELECT * FROM Subscribers WHERE count > 0 AND is_active IS %b", true);
        try (Connection connection = ConnectionService.connect()) {
            try (ResultSet rs = connection.createStatement().executeQuery(sql)) {
                while (rs.next()) {
                    subscriberList.add(new Subscriber(
                            rs.getInt("id"),
                            rs.getLong("chat_id"),
                            rs.getString("city"),
                            rs.getDouble("lat"),
                            rs.getDouble("lon"),
                            rs.getInt("utc"),
                            rs.getBoolean("is_active"),
                            rs.getInt("count"),
                            rs.getTimestamp("last_message").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return subscriberList;
    }

    //Не используется
    private static int updateCount(long chatId, String city) {
        String sql = "UPDATE Subscribers SET count = ?, last_message = ? WHERE chat_id = ? AND city = ?";
        try (Connection connection = ConnectionService.connect()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, 60);
            ps.setString(2, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)).toString());
            ps.setLong(3, chatId);
            ps.setString(4, city);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static void updateLastMessage(Subscriber subscriber) {
        String sql = "UPDATE Subscribers SET count = ?, last_message = ? WHERE id = ?";
        try (Connection connection = ConnectionService.connect()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, subscriber.getCount() - 1);
            ps.setString(2, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)).toString());
            ps.setInt(3, subscriber.getId());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Удаляет ВСЕ подписки данного пользователя
    public static void delete(long chatId) {
        String sql = String.format("DELETE FROM Subscribers WHERE chat_id = %d", chatId);
        try (Connection connection = ConnectionService.connect()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Не используется. Таблица считается созданной.
    private static void createTable() {
        String sql = """
                CREATE TABLE [Subscribers] (
                  [id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL,
                  [chat_id] INTEGER  NOT NULL,
                  [city] VARCHAR(30)  NOT NULL,
                  [lat] REAL  NOT NULL,
                  [lon] REAL  NOT NULL,
                  [utc] INTEGER DEFAULT '0' NOT NULL,
                  [is_active] BOOLEAN DEFAULT '1' NOT NULL,
                  [count] INTEGER DEFAULT '60' NULL,
                  [last_message] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL
                  )""";
        //ConnectionService.execute(sql);
    }
}
