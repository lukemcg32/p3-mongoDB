// Query 3
// Create a collection "cities" to store every user that lives in every city. Each document(city) has following schema:
// {
//   _id: city
//   users:[userids]
// }
// Return nothing.


function cities_table(dbname) {
    db = db.getSiblingDB(dbname);

   // TODO: implement cities collection here

    db.users.aggregate([
        // group documents by current city
        { 
            $group: { 
                //_id: "$user_current_city.CURRENT_CITY_ID",  // city becomes id of the document
                _id: "$city", 
                //using addToSet to make sure user ids are distinct
                users: { $addToSet: "$user_id" }    // Get all unique ids
            } 
        },
        // output city collection
        { $out: "cities" }
    ]);

    return;
}

