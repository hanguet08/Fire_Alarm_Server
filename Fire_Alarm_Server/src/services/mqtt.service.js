const mqtt = require("mqtt");
const console = require("../utils/chalk");
const smart_home_hat = "smart_home_humidity_and_temperature";
const smart_home_flame_and_mq2 = "smart_home_flame_and_mq2";
const topic_subscribe = [smart_home_hat, smart_home_flame_and_mq2];
// FCM
const { sendMessageFCM } = require("./firebase.cloud.message.service");
const { insertDataSensorDb } = require("../db/sensor.db");
const { getDeviceDb } = require("../db/device.db");
const { getRoomDb } = require("../db/room.db");
const { getHouseDb } = require("../db/house.db");
const { getUserDb } = require("../db/user.db");
const { createNotificationDb } = require("../db/notification.db");
const DeviceStatus = require("../constants/device.status");

const host_mqtt = "broker.hivemq.com";
const port_mqtt = "1883";
const clientId = `8a673344-cc1c-4bfc-9f68-bab47bbbf845`;
const connectUrl = `mqtt://${host_mqtt}:${port_mqtt}`;

var messageFCM = {
  title: "CẢNH BÁO NGUY HIỂM",
  body: "",
};
// thực hiện tạo connect tới mqtt broker
var mqttClient = mqtt.connect(connectUrl, {
  clientId,
  clean: true,
  connectTimeout: 4000,
  username: "fire_alarm_system1", // tự cấu hình
  password: "1234567", // tự cấu hình
  reconnectPeriod: 1000,
});

mqttClient.once("connect", function () {
  console.log("Connect to mqtt successfully");
  mqttClient.subscribe(topic_subscribe, () => {
    console.log(`Subscribe to topic success`);
  });

  // listener message receive
  mqttClient.on("message", async (topic, msg) => {
    try {
      // message receive from MQTT
      const message = JSON.parse(msg.toString()),
        { deviceId, deviceType } = message,
        // get info fcmToken
        device = await getDeviceDb({ _id: deviceId }),
        statusDevice = device.status,
        roomId = device.roomId,
        room = await getRoomDb({ _id: roomId }),
        houseId = room.houseId,
        house = await getHouseDb({ _id: houseId }),
        userId = house.userId,
        user = await getUserDb({ _id: userId }),
        fcmToken = user.fcmToken;
      console.log(message);
      if (statusDevice == DeviceStatus.ON) {
        if (deviceType == 1) {
          // flame sensor
          const { flameValue } = message;
          if (flameValue == 0) {
            // save notification to db and notify to FCM
            let content = `Phát hiện lửa tại ${room.position} (${room.roomName}), ${house.houseName}`;
            messageFCM.body = content;
            sendMessageFCM(fcmToken, messageFCM);
            createNotificationDb({
              device: device.deviceName,
              content: content,
              userId: house.userId,
            });
          }
          // Lưu giá trị vào db
          insertDataSensorDb({ deviceId, deviceType, flameValue });
        } else if (deviceType == 2) {
          // MQ2
          const { MQ2Value } = message;
          if (MQ2Value == 0) {
            // save notification to db and notify to FCM
            let content = `Phát hiện khí ga tại ${room.position} (${room.roomName}), ${house.houseName}`;
            messageFCM.body = content;
            console.log(messageFCM);
            sendMessageFCM(fcmToken, messageFCM);
            createNotificationDb({
              device: device.deviceName,
              content: content,
              userId: house.userId,
            });
          }
          // Save data sensor to db
          insertDataSensorDb({ deviceId, deviceType, MQ2Value });
        } else {
          const { humidityAir, temperature } = message;
          // Save data sensor to db
          insertDataSensorDb({
            deviceId,
            deviceType,
            humidityAir,
            temperature,
          });
        }
      }
    } catch (error) {
      console.error("Error on MQTT service");
    }
  });
});

mqttClient.on("error", function (error) {
  console.log("Unable to connect: " + error);
  process.exit(1);
});

module.exports = mqttClient;

