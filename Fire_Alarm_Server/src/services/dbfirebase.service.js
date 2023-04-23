const moment = require("moment");
const { databaseFirebase } = require("./setup.firebase");
const insertNotificationFirebase = (device, content, userId) => {
  const userRef = databaseFirebase.ref("users/" + userId + "/notifications");
  const newUserRef = userRef.push();
  const datetime = new Date();
  newUserRef.set({
    device: device,
    content: content,
    userId: userId,
    minutes: datetime.getMinutes(),
    day: datetime.getDate(),
    hour: datetime.getHours(),
    month: datetime.getMonth() + 1,
    year: datetime.getFullYear(),
  });
  return;
};

const insertTempratureAndHumidityFirebase = (
  deviceId,
  userId,
  humidityAir,
  temperature
) => {
  const userRef = databaseFirebase.ref(
    "users/" + userId + "/dhtData/" + deviceId
  );
  const newUserRef = userRef.push();
  const datetime = new Date();
  newUserRef.set({
    temperature: temperature,
    humidityAir: humidityAir,
    minutes: datetime.getMinutes(),
    day: datetime.getDate(),
    hour: datetime.getHours(),
    month: datetime.getMonth() + 1,
    year: datetime.getFullYear(),
    time: `${datetime.getHours()}:${datetime.getMinutes()}`,
  });
  return;
};
module.exports = {
  insertNotificationFirebase,
  insertTempratureAndHumidityFirebase,
};
