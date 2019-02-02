let AWS = require('aws-sdk');
let mysql = require('mysql');

let sns = new AWS.SNS();

let threshold = parseFloat(process.env.THRESHOLD);
if (threshold === undefined) {
    throw Error("No threshold defined");
}

let connection = mysql.createConnection({
    host: process.env.HOST,
    user: process.env.USER,
    password: process.env.PASSWORD,
    database: process.env.DATABASE
});
connection.connect();

exports.handler = function(event, context, callback) {
    context.callbackWaitsForEmptyEventLoop = false;

    let sql = "SELECT value, timestamp FROM battery_data ORDER BY timestamp DESC LIMIT 2";
    connection.query(sql, function(err, result) {
        if (err) {
            console.error("Query failed", err);
        } else {
            console.log("Query successful", result);
            if (result !== undefined && result.length >= 2) {
                let value1 = result[0]["value"]; // Newest Value
                let timestamp1 = result[0]["timestamp"];
                let value2 = result[1]["value"]; // Value before newest

                console.log("Newest value: " + value1 + ", value before: " + value2);
                if (value1 <= threshold && value2 > threshold) {
                    console.log("Threshold triggered");
                    // Publish to SNS
                    sns.publish({
                        Message: JSON.stringify({
                            "timestamp": timestamp1,
                            "value": value1
                        }),
                        TopicArn: process.env.CRITICAL_BATTERY_TOPIC_ARN
                    }, function(err, data) {
                        if(err) {
                            console.error("Publishing sns failed", err);
                        } else {
                            console.log("SNS publish successful");
                        }
                        callback(null, null);
                    });
                } else {
                    callback(null, null);
                }
            }
        }
    });
};
