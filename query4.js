// Query 4
// Find user pairs (A,B) that meet the following constraints:
// i) user A is male and user B is female
// ii) their Year_Of_Birth difference is less than year_diff
// iii) user A and B are not friends
// iv) user A and B are from the same hometown city
// The following is the schema for output pairs:
// [
//      [user_id1, user_id2],
//      [user_id1, user_id3],
//      [user_id4, user_id2],
//      ...
//  ]
// user_id is the field from the users collection. Do not use the _id field in users.
// Return an array of arrays.

function suggest_friends(year_diff, dbname) {
    db = db.getSiblingDB(dbname);

    let pairs = [];
    // TODO: implement suggest friends

    // get all users in an array
    let users = db.users.find({}).toArray();

    // Iterate over all pairs
    for (let i = 0; i < users.length; i++) {
        let A = users[i];

        // A has to be a male
        if (A.gender !== "male") continue;
        
        for (let j = 0; j < users.length; j++) {
            let B = users[j];
            
            // B has to be a female
            if (B.gender !== "female") continue;
            
            // continue if not from same hometown city
            if (A.hometown.city !== B.hometown.city) continue;
            
            // if difference is bigger than year_diff, continue
            if (Math.abs(A.YOB - B.YOB) >= year_diff) continue;

            //finds pairs of NOT friends
            //if they are friends, then continue
            if ((A.friends && A.friends.indexOf(B.user_id) !== -1) || (B.friends && B.friends.indexOf(A.user_id) !== -1)) continue;
            
            // if we haven't yet continued, valid pair...push
            pairs.push([A.user_id, B.user_id]);
        }
    }
    return pairs;
}