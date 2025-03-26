// Query 5
// Find the oldest friend for each user who has a friend. For simplicity,
// use only year of birth to determine age, if there is a tie, use the
// one with smallest user_id. You may find query 2 and query 3 helpful.
// You can create selections if you want. Do not modify users collection.
// Return a javascript object : key is the user_id and the value is the oldest_friend id.
// You should return something like this (order does not matter):
// {user1:userx1, user2:userx2, user3:userx3,...}





function oldest_friend(dbname) {
    db = db.getSiblingDB(dbname);

    let results = {};
    // TODO: implement oldest friends

    // iterate over every user in the collection
    db.users.find({}).forEach(function(u) {

        // find a user thats either the key for friends array or in someone else's friends array
        let query = {
            $or: [
                { user_id: { $in: u.friends } },
                { friends: u.user_id }
            ]
        };

        // sort by YOB asc (gives oldest friend first), then user_id asc
        // limit(1) = FETCH FIRST 1 ROW ONLY
        // convert to array for object storing
        let candidate = db.users.find(query).sort({ YOB: 1, user_id: 1 }).limit(1).toArray();

        // there must be at least one canidate...if there is record the oldest friend
        if (candidate.length > 0) {
            results[u.user_id] = candidate[0].user_id;
        }
    });

    return results;
}