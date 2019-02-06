let alexa = require("alexa-app");
let app = new alexa.app("Smart Cookie Jar");
let AWS = require('aws-sdk');
let lambda = new AWS.Lambda();

let AmazonSpeech = require('ssml-builder/amazon_speech');


app.launch(function(request, response) {
    response.say("Hallo, ich bin deine Keksdose. Was kann ich für dich tun?");
});

app.intent("GetCookieState", {},
    function(request, response) {
        return new Promise(function(resolve, reject) {
            lambda.invoke({
                FunctionName: process.env.GET_WEIGHT_FUNCTION_NAME
            }, function(err, data) {
                if (err != undefined || data == undefined || data["Payload"] == undefined || JSON.parse(data["Payload"]) == undefined) {
                    response.say("Ich kann gerade nicht nachsehen, ob es Kekse gibt. Versuche es noch einmal.");
                } else {
                    let payload = JSON.parse(data["Payload"]);
                    let value = payload["value"];

                    if (value < process.env.WEIGHT_NO_COOKIES) {
                        response.say("Ich habe leider keine Kekse für dich.");
                    } else if (value < process.env.WEIGHT_MAYBE_COOKIES) {
                        response.say("Es könnten noch Kekse vorhanden sein. Beeil dich!");
                    } else if (value < process.env.WEIGHT_COOKIES) {
                        response.say("Es sind Kekse vorhanden.");
                    } else {
                        response.say("Es sind reichlich Kekse vorhanden!")
                    }
                }
                resolve();
            });
        });
    }
);

app.intent("GetBatteryState",
    function(request, response) {
        return new Promise(function(resolve, reject) {
            return lambda.invoke({
                FunctionName: process.env.GET_BATTERY_FUNCTION_NAME
            }, function(err, data) {
                if (err != undefined || data == undefined || data["Payload"] == undefined || JSON.parse(data["Payload"]) == undefined) {
                    response.say("Ich kann gerade nicht nachsehen, wie der Ladestand ist. Versuche es noch einmal.");
                } else {
                    let payload = JSON.parse(data["Payload"]);
                    let value = payload["value"];
                    let speech = new AmazonSpeech()
                        .say("Die Spannung beträgt")
                        .sayAs({
                            word: value.toString().replace('.', ','),
                            interpret: 'number'
                        })
                        .say(" Volt.");

                    let speechOut = speech.ssml();
                    response.say(speechOut);
                }
                resolve();
            });
        });
    }
);

app.intent("AMAZON.FallbackIntent", {},
    function(request, response) {
        response.say("Ich weiß leider nicht, was du meinst. Ich bin hier nur für die Kekse zuständig.")
    }
);

app.intent("AMAZON.HelpIntent", {},
    function(request, response) {
        response.say("Du kannst mich fragen, ob ich noch Kekse für dich habe. Außerdem kannst du von mir den aktuellen Batterie-Status erfahren.")
    }
);

// connect the alexa-app to AWS Lambda
exports.handler = app.lambda();