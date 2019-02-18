let AWS = require('aws-sdk');
let mysql = require('mysql');

let connection = mysql.createConnection({
    host: process.env.HOST,
    user: process.env.USER,
    password: process.env.PASSWORD,
    database: process.env.DATABASE
});
connection.connect();

let numberMap = function (value, in_min, in_max, out_min, out_max) {
    return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
};

exports.handler = function(event, context, callback) {
    context.callbackWaitsForEmptyEventLoop = false;
    console.log("Event", event);

    let sql = "SELECT id, value, timestamp FROM weight_data WHERE value > " + parseFloat(process.env.REASONABLE_MIN).toString() + " AND value < " + parseFloat(process.env.REASONABLE_MAX).toString() + " ORDER BY timestamp DESC LIMIT 1";
    connection.query(sql, function(err, result) {
        if (err) {
            console.error("Query failed", err);
            callback("query_failed", null);
        } else {
            console.error("Query successful");
            if (result !== undefined && result.length >= 1) {
                callback(null, {
                    data: {
                        id: result[0]["id"],
                        timestamp: result[0]["timestamp"],
                        value: result[0]["value"]
                    },
                    percentage: parseInt(Math.max(0, Math.min(100, numberMap(result[0]["value"], parseFloat(process.env.ZERO_PERCENT), parseFloat(process.env.MAX_PERCENT), 0, 100))))
                })
            } else {
                callback("no_data_yet", null);
            }
        }
    });
};
