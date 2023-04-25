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
