import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.json.JSONObject;
import org.json.JSONArray;

public class Main {

    static String dataType = "PUBLIC";
    static String oracleUserName = "lukemcg"; // replace with your uniqname
    static String password = "perpetua"; // replace with your Oracle password (default: eecsclass)

    public static void main(String[] args) {

        try (Connection conn = getConnection()) {
            GetData fbwz = new GetData(dataType, conn);
            // function
            JSONArray users_info = fbwz.toJSON();
            fbwz.writeJSON(users_info);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:oracle:thin:@forktail.dsc.umich.edu:1521:COURSEDB", oracleUserName,
                password);
    }
}
