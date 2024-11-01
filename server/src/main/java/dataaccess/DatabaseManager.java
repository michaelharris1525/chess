package dataaccess;

import model.CreateTablesException;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager{
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);

//                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    //added stuff starts here
    // Method to create the users table
    // Method to create the users table

//    public static void createTables(Statement stmt) throws DataAccessException, SQLException {
//        System.out.println("Creating tables...");
//        String createUsersTableSQL = """
//            CREATE TABLE IF NOT EXISTS users (
//                username VARCHAR(255) PRIMARY KEY,
//                password VARCHAR(255) NOT NULL,
//                email VARCHAR(255) UNIQUE NOT NULL
//            );
//            """;
//            stmt.executeUpdate(createUsersTableSQL);
//
//    }





//    public static void initializeDatabase() throws DataAccessException {
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", USER, PASSWORD);
//             Statement stmt = conn.createStatement()) {
//
//            // Create the database if it doesn't exist
//            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
//            // Switch to the created database
//            conn.setCatalog(DATABASE_NAME);
//            createDatabase();
//            // Create tables
//            createTables(stmt);
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Failed to initialize the database: " + e.getMessage());
//        }
//    }

//    static void createTables() throws DataAccessException {
//        String sql = "CREATE TABLE IF NOT EXISTS users (" +
//                "username VARCHAR(255) PRIMARY KEY, " +
//                "password VARCHAR(255) NOT NULL, " +
//                "email VARCHAR(255) NOT NULL" +
//                ")";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new DataAccessException("Error creating users table: " + e.getMessage());
//        }
//    }
//    public void initializeDatabase() throws DataAccessException {
//        createDatabase();
//        createTables();
//    }


//    public static void createTables() throws DataAccessException {
//        String createUsersTableSQL = """
//            CREATE TABLE IF NOT EXISTS users (
//                id INT AUTO_INCREMENT PRIMARY KEY,
//                username VARCHAR(255) UNIQUE NOT NULL,
//                password VARCHAR(255) NOT NULL,
//                email VARCHAR(255) UNIQUE NOT NULL
//            );
//            """;
//
//        try (Connection conn = getConnection();
//             Statement stmt = conn.createStatement()) {
//            stmt.executeUpdate(createUsersTableSQL);
//            System.out.println("Users table created or already exists.");
//        } catch (SQLException e) {
//            throw new DataAccessException("Error creating users table: " + e.getMessage());
//        }
//    }
}
