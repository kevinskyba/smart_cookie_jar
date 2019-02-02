let AWS = require('aws-sdk');
let mysql = require('mysql');

let connection = mysql.createConnection({
    host: process.env.HOST,
    user: process.env.USER,
    password: process.env.PASSWORD,
    database: process.env.DATABASE
});
connection.connect();

exports.handler = function(event, context, callback) {
    context.callbackWaitsForEmptyEventLoop = false;
    console.log("Event", event);

    let sql = "SELECT id, value, timestamp FROM weight_data ORDER BY timestamp DESC LIMIT 1";
    connection.query(sql, function(err, result) {
        if (err) {
            console.error("Query failed", err);
            callback("query_failed", null);
        } else {
            console.error("Query successful");
            if (result !== undefined && result.length >= 1) {
                callback(null, {
                    id: result[0]["id"],
                    timestamp: result[0]["timestamp"],
                    value: result[0]["value"]
                })
            } else {
                callback("no_data_yet", null);
            }
        }
    });
};
