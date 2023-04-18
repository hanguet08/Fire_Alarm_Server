// const admin = require("firebase-admin");
// const serviceAccount = require("./serviceAccountKey.json");

// admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount),
// });

// const options = {
//   priority: "high",
//   timeToLive: 60 * 60 * 24,
// };
// async function sendMessageFCM(registrationToken, message) {
//   var payload = {
//     notification: message,
//   };

//   await admin
//     .messaging()
//     .sendToDevice(registrationToken, payload, options)
//     .then(function (response) {
//       console.log("Successfully sent message:", response);
//     })
//     .catch(function (error) {
//       console.log("Error sending message:", error);
//     });
// }

// module.exports = { sendMessageFCM };

const { sendMess } = require("./setup.firebase");

const options = {
  priority: "high",
  timeToLive: 60 * 60 * 24,
};
async function sendMessageFCM(registrationToken, message) {
  var payload = {
    notification: message,
  };

  await sendMess(registrationToken, payload, options);
}

module.exports = { sendMessageFCM };
