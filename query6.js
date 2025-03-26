// Query 6
// Find the average friend count per user.
// Return a decimal value as the average user friend count of all users in the users collection.


function find_average_friendcount(dbname) {
    db = db.getSiblingDB(dbname);

    // TODO: calculate the average friend count

    let result = db.users.aggregate([
        // define a new field of friendCount for later computation
        { $project: { friendCount: { $size: "$friends" } } },
        // iteratatively count total friends and total users
        { $group: { _id: null, totalFriends: { $sum: "$friendCount" }, userCount: { $sum: 1 } } }
    ]).toArray();

    // return average friends per user
    return result[0].totalFriends / result[0].userCount;
}
