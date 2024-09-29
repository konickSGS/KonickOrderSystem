package gs.konick.sql;

import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

public abstract class SqlQuery {
    /**
     * rollback connection
     * @param connection connection
     */
    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            LogManager.getLogger(SqlQuery.class.getName()).error(e);
        }
    }

    public static void rollback(Connection connection, Savepoint savepoint) {
        try {
            connection.rollback(savepoint);
        } catch (SQLException e) {
            LogManager.getLogger(SqlQuery.class.getName()).error(e);
        }
    }
}
