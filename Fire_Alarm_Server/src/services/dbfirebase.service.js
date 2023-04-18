const moment = require("moment");
const { databaseFirebase } = require("./setup.firebase");
const insertNotificationFirebase = (device, content, userId) => {
  const userRef = databaseFirebase.ref("notifications");
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

  //   ref
  //     .once("value", (snapshot) => {
  //       console.log("snapshot", snapshot.val());
  //       if (!snapshot.val()) {
  //         addNewHaT.set({
  //           device,
  //           content,
  //           userId,
  //           minutes: datetime.getMinutes(),
  //           day: datetime.getDate(),
  //           hour: datetime.getHours(),
  //           month: datetime.getMonth() + 1,
  //           year: datetime.getFullYear(),
  //         });
  //         return;
  //       }
  //       else {
  //         console.log("Đã tồn tại data Dht");
  //       }
  //     });
  //   return;
};
module.exports = {
  insertNotificationFirebase,
};
