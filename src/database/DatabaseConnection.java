package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class DatabaseConnection {
	
	public static final String SQL_PORT = "3306",
            SQL_URL = "jdbc:mysql://localhost:3306/date_in?autoReconnect=true",
            SQL_USER = "root",
            SQL_PASSWORD = "";
	
	private static final ThreadLocal<Connection> con = new DatabaseConnection.ThreadLocalConnection();
    public static final int CLOSE_CURRENT_RESULT = 1;
    /**
     * The constant indicating that the current <code>ResultSet</code> object
     * should not be closed when calling <code>getMoreResults</code>.
     *
     * @since 1.4
     */
    public static final int KEEP_CURRENT_RESULT = 2;
    /**
     * The constant indicating that all <code>ResultSet</code> objects that have
     * previously been kept open should be closed when calling
     * <code>getMoreResults</code>.
     *
     * @since 1.4
     */
    public static final int CLOSE_ALL_RESULTS = 3;
    /**
     * The constant indicating that a batch statement executed successfully but
     * that no count of the number of rows it affected is available.
     *
     * @since 1.4
     */
    public static final int SUCCESS_NO_INFO = -2;
    /**
     * The constant indicating that an error occured while executing a batch
     * statement.
     *
     * @since 1.4
     */
    public static final int EXECUTE_FAILED = -3;
    /**
     * The constant indicating that generated keys should be made available for
     * retrieval.
     *
     * @since 1.4
     */
    public static final int RETURN_GENERATED_KEYS = 1;
    /**
     * The constant indicating that generated keys should not be made available
     * for retrieval.
     *
     * @since 1.4
     */
    public static final int NO_GENERATED_KEYS = 2;

    public static Connection getConnection() {
        return con.get();
    }

    public static void closeAll() throws SQLException {
        for (final Connection connection : DatabaseConnection.ThreadLocalConnection.allConnections) {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static final class ThreadLocalConnection extends ThreadLocal<Connection> {

        public static final Collection<Connection> allConnections = new LinkedList<>();

        @Override
        protected final Connection initialValue() {
            try {
                Class.forName("com.mysql.jdbc.Driver"); // Check if the Driver file exists;
            } catch (final ClassNotFoundException e) {
                System.err.println("ERROR :\n" + e);
            }
            
            // Try connecting to the database..
            try {
                final Connection con = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWORD);
                allConnections.add(con);
                return con; // returns the connection back to the function that call;
            } catch (SQLException e) {
                System.err.println("ERROR :\n" + e);
                return null; // returns null if no connections;
            }
        }
    }
}
