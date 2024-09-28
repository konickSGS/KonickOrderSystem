package gs.konick.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtils {
    public static void executeSeveralQueries(Connection connection, String queries) throws SQLException {
        Statement statement = connection.createStatement();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(queries));

        StringBuilder query = new StringBuilder();
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().startsWith("-- ")) {
                    continue;
                }
                query.append(line).append(" ");

                if (line.trim().endsWith(";")) {
                    statement.execute(query.toString().trim());
                    // Создаем следующий query
                    query = new StringBuilder();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
