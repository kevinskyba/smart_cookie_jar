let AWS = require('aws-sdk');
let mysql = require('mysql');

let sns = new AWS.SNS();

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
        if (body === undefined || body["timestamp"] === undefined || body["battery"] === undefined) {
            callback("invalid_body", null);
            return;
        }

        console.log("Body", body);

        let timestamp = body["timestamp"];
        let value = body["battery"];

        let sql = "INSERT INTO battery_data (timestamp, value) VALUES (" + timestamp + ", " + value + ")";
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
                console.log("Query successful");
                // Publish to SNS
                sns.publish({
                    Message: JSON.stringify({
                        timestamp,
                        value
                    }),
                    TopicArn: process.env.BATTERY_DATA_SAVED_TOPIC_ARN
                }, function(err, data) {
                    if(err) {
                        console.error("Publishing sns failed", err);
                    } else {
                        console.log("SNS publish successful");
                    }
                    callback(null, {});
                });
            }
        });

    } else {
        callback(new Error("invalid event"), null);
    }
};
