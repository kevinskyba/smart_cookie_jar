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
    if (event !== undefined && event["Records"] !== undefined && event["Records"].length === 1) {

        let record = event["Records"][0];
        let body = JSON.parse(record["body"]);
        if (body === undefined || body["timestamp"] === undefined || body["error"] === undefined) {
            callback("invalid_body", null);
            return;
        }

        console.log("Body", body);

        let timestamp = body["timestamp"];
        let value = body["error"];

        let sql = "INSERT INTO error_data (timestamp, value) VALUES (" + timestamp + ", '" + value + "')";
        connection.query(sql, function(err, result) {
            if (err) {
                if (err["code"] === "ER_DUP_ENTRY") {
                    console.log("Duplicate entry, query successful");
                    callback(null, "duplicate_entry");
                } else {
                    console.error("Query failed", err);
                    callback("query_failed", null);
                }
            } else {
                console.error("Query successful");
            }
            callback(null, result);
        });

    } else {
        callback(new Error("invalid event"), null);
    }
};
