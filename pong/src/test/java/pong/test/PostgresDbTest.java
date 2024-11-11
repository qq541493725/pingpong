package pong.test;

import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spock.lang.Ignore;

import java.sql.*;

@Ignore
public class PostgresDbTest {
    private static Connection conn;

    @BeforeAll
    static void init() throws SQLException {
        conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "root");
    }

    @AfterAll
    static void after() throws SQLException {
        conn.close();
    }

    @Test
    void test() throws SQLException {
        @Cleanup Statement stmt = conn.createStatement();
        @Cleanup ResultSet result = stmt.executeQuery("select id,name from testtable");
        while (result.next()) {
            System.out.println(result.getInt(1) + " " + result.getString(2));
        }
    }
}
