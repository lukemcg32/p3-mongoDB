// Query 7
// Use the aggregate command to create a collection called 
// countbymonth that has the following schema:
// 
// MOB: [Value between 1 and 12]
// borncount: number of users born in that month
// (It is ok if it also has an additional object identifier field _id)
// 
// You will likely find it useful to use the following 
// elements in the aggregate pipeline:
// $group
// $sort
// $addfields: to add a column called MOB
// $project: you may need it to remove _id created by group
// $out: to output the result to the new collection.


function users_born_by_month(dbname) {
    db = db.getSiblingDB(dbname);

    db.users.aggregate([
        // group users by Month of Birth and count how many per group
        { $group: {_id: "$MOB", borncount: { $sum: 1 } } },
        // add the field MOB using the _id value
        { $addFields: { MOB: "$_id" } },
        // remove _id that was created by group field
        { $project: {  _id: 0 } },
        // sort in ascending order, in accordance with spec
        { $sort: { MOB: 1 } },
        // output the result into a new collection
        { $out: "countbymonth" }
    ]);

    return;
}