package util;

import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Util {

    private static PropertiesUtil properties = PropertiesUtil.getInstance();
    private static final String h2Driver = properties.getValue("h2.driver");
    private static final String h2ConnectionUrl = properties.getValue("h2.connection.url");
    private static final String h2User = properties.getValue("h2.user");
    private static final String h2Password = properties.getValue("h2.password");

    private static final Logger log = LoggerFactory.getLogger(H2Util.class);

    public static Connection getConnection() {
        try {
            Class.forName(h2Driver);
            return DriverManager.getConnection(h2ConnectionUrl, h2User, h2Password);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error while connecting to DB: {}", e.getMessage());
            return null;
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            return null;
        }
    }

    public void createSchema() {
        Connection connection = null;
        try {
            connection = getConnection();
            RunScript.execute(connection, new FileReader("src/main/resources/schema.sql"));
        } catch (SQLException | FileNotFoundException e) {
            log.error("Could not run schema.sql!");
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
