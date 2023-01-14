const authRouter = require("./auth.route");
const userRouter = require("./api/user.route");
const roomRouter = require("./api/room.route");
const houseRouter = require("./api/house.route");
const deviceRouter = require("./api/device.route");
const notificationRouter = require("./api/notification.route");
const sensorRouter = require("./api/sensor.route");

//Index of route middleware
const route = (app) => {
  // Route middleware auth
  app.use("/auth", authRouter);

  // Route user
  app.use("/api/v1/users", userRouter);

  // Route room
  app.use("/api/v1/houses", houseRouter);

  // Route room
  app.use("/api/v1/rooms", roomRouter);

  // Route device
  app.use("/api/v1/devices", deviceRouter);

  // Route notification
  app.use("/api/v1/notifications", notificationRouter);

  // Route sensor
  app.use("/api/v1/sensors", sensorRouter);
};

module.exports = route;
