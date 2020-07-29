package DatabaseService;

import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.Properties;

class ConnectionService {
    private static final String connectionDriver = "org.sqlite.JDBC";
    private static final String connectionString = "jdbc:sqlite:d:/TelegramUsers.s3db";

    static Connection connect() {
        Connection connection = null;
        try {
            SQLiteConfig sqLiteConfig = new SQLiteConfig();
            Properties properties = sqLiteConfig.toProperties();
            properties.setProperty(SQLiteConfig.Pragma.DATE_STRING_FORMAT.pragmaName, "yyyy-MM-dd HH:mm:ss");
            Class.forName(connectionDriver);
            connection = DriverManager.getConnection(connectionString, properties);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
