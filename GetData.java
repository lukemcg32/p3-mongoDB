import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONArray;

public class GetData {

    static String prefix = "project3.";

    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;

    // You must refer to the following variables for the corresponding 
    // tables in your database
    String userTableName = null;
    String friendsTableName = null;
    String cityTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;

    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
        super();
        String dataType = u;
        oracleConnection = c;
        userTableName = prefix + dataType + "_USERS";
        friendsTableName = prefix + dataType + "_FRIENDS";
        cityTableName = prefix + dataType + "_CITIES";
        currentCityTableName = prefix + dataType + "_USER_CURRENT_CITIES";
        hometownCityTableName = prefix + dataType + "_USER_HOMETOWN_CITIES";
    }


// SELECT * FROM project3.public_users;
// SELECT * FROM project3.public_cities; // gives city_id, city_name, state_name, country_name
// SELECT * FROM project3.public_friends; // gives USER1_ID, USER2_ID (user1_id < user2_id) 
// SELECT * FROM project3.PUBLIC_USER_CURRENT_CITIES; // gives USER_ID, CURRENT_CITY_ID
// SELECT * FROM project3.PUBLIC_USER_HOMETOWN_CITIES; // gives USER_ID, HOMETOWN_CITY_ID
// SELECT table_name FROM all_tables WHERE owner = 'PROJECT3' AND table_name LIKE 'PUBLIC_%'; // will give all tables 


    // TODO: Implement this function
    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException {

        // This is the data structure to store all users' information
        JSONArray users_info = new JSONArray();
        
        try (Statement stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            // Your implementation goes here....

        
        // start by grabbing all 800 users
        String queryUsers = "SELECT user_id, first_name, last_name, gender, YEAR_OF_BIRTH, MONTH_OF_BIRTH, DAY_OF_BIRTH FROM project3.public_users";
        ResultSet rsUsers = stmt.executeQuery(queryUsers);
        
        // iterate through
        while (rsUsers.next()) {
            int user_id = rsUsers.getInt("user_id"); // we will need to store this for the friends and both city arrays for each user

            // create the JSONObject
            JSONObject userObj = new JSONObject();

            userObj.put("user_id", user_id);
            userObj.put("first_name", rsUsers.getString("first_name"));
            userObj.put("last_name", rsUsers.getString("last_name"));
            userObj.put("gender", rsUsers.getString("gender"));
            userObj.put("YOB", rsUsers.getInt("YEAR_OF_BIRTH"));
            userObj.put("MOB", rsUsers.getInt("MONTH_OF_BIRTH"));
            userObj.put("DOB", rsUsers.getInt("DAY_OF_BIRTH"));
            

            // create the first array of strings - friends
            JSONArray friends = new JSONArray();


            try (Statement stmtFriends = oracleConnection.createStatement()) {
                String queryFriends = "SELECT user2_id FROM project3.public_friends WHERE user1_id = " + user_id; // user1_id < user2_id in friends table
                ResultSet rsFriends = stmtFriends.executeQuery(queryFriends);

                // iterate through friends result set
                while (rsFriends.next()) {
                    int friend_id = rsFriends.getInt("user2_id");
                    friends.put(friend_id); // put into the friends array
                }

            rsFriends.close(); // we can close this one now
            }
            userObj.put("friends", friends); // put into the Object
            

            // create the second array of strings - current city
            JSONObject currentCityObj = new JSONObject();

            try (Statement stmtCurrentCity = oracleConnection.createStatement()) {
                // get current city of user_id (still going through one at a time)
                String queryCurrent = "SELECT C.city_name, C.state_name, C.country_name " +
                        "FROM project3.public_cities C " +
                        "JOIN project3.public_user_current_cities CC ON CC.current_city_id = C.city_id " +
                        "WHERE CC.user_id = " + user_id;
                ResultSet rsCurrent = stmtCurrentCity.executeQuery(queryCurrent);

                // use an if because we only expect one or no values
                if (rsCurrent.next()) {

                    // add in city state and country to our array
                    currentCityObj.put("city", rsCurrent.getString("city_name"));
                    currentCityObj.put("state", rsCurrent.getString("state_name"));
                    currentCityObj.put("country", rsCurrent.getString("country_name"));
                } 
                rsCurrent.close();
            }
            // userObj.put("current", currentCityObj); // throw it in the object
            


            // create the third array of strings - hometown city
            JSONObject hometownCityObj = new JSONObject();

            try (Statement stmtHometownCity = oracleConnection.createStatement()) {
                String queryHometown = "SELECT C.city_name, C.state_name, C.country_name " +
                        "FROM project3.public_cities C " +
                        "JOIN project3.public_user_hometown_cities HC ON HC.hometown_city_id = C.city_id " +
                        "WHERE HC.user_id = " + user_id;
                ResultSet rsHometown = stmtHometownCity.executeQuery(queryHometown);

                if (rsHometown.next()) {
                    hometownCityObj.put("city", rsHometown.getString("city_name"));
                    hometownCityObj.put("state", rsHometown.getString("state_name"));
                    hometownCityObj.put("country", rsHometown.getString("country_name"));
                }
                rsHometown.close();
            }
            userObj.put("hometown", hometownCityObj); // throw it in the object

            
            userObj.put("current", currentCityObj); // put current after??
            
            // add the finished object to the users_info array
            users_info.put(userObj);
        }
        rsUsers.close();














        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return users_info;
    }

    // This outputs to a file "output.json"
    // DO NOT MODIFY this function
    public void writeJSON(JSONArray users_info) {
        try {
            FileWriter file = new FileWriter(System.getProperty("user.dir") + "/output.json");
            file.write(users_info.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
