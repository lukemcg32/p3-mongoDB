// Query 2
// Unwind friends and create a collection called 'flat_users' where each document has the following schema:
// {
//   user_id:xxx
//   friends:xxx
// }
// Return nothing.

function unwind_friends(dbname) {
    db = db.getSiblingDB(dbname);

    db.users.aggregate([
        //Unwinding friends vv
        { $unwind: "$friends" },
        { $project: { _id: false, user_id: true, friends: "$friends" } }, // creates collection with req fields
        { $out: "flat_users" } // output to "flat_users"
    ]);

    return;
}

// // { $project: { <specification(s)> } }
// <field>: <1 or true> Specifies the inclusion of a field. Non-zero integers are also treated as true.

// _id: <0 or false> Specifies the suppression of the _id field. To exclude a field conditionally, use the REMOVE variable instead. For details, see Exclude Fields Conditionally.