const admin = require("firebase-admin");
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://fire-alarm-system-225e7-default-rtdb.firebaseio.com/",
});

const databaseFirebase = admin.database();
const sendMess = async (registrationToken, payload, options) => {
  await admin
    .messaging()
    .sendToDevice(registrationToken, payload, options)
    .then(function (response) {
      console.log("Successfully sent message:", response);
    })
    .catch(function (error) {
      console.log("Error sending message:", error);
    });
};
module.exports = { databaseFirebase, sendMess };
