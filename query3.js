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
        // group documents by current city and then get all unique ids
        { $group: { _id: "$current.city", users: { $addToSet: "$user_id"} } },
        
        // output city collection
        { $out: "cities" }
    ]);

    return;
}