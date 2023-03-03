const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const { spawn } = require("child_process");
const {
  getSensorDb,
  insertDataSensorDb,
  getAllSensorOfDeviceDb,
} = require("../db/sensor.db");

// Get sensor
const getSensor = async (req, res, next) => {
  return res
    .status(404)
    .json(apiResponse({ status: APIStatus.FAIL, msg: "Page not found" }));
};

// Get data sensor
const getDataSensor = async (req, res, next) => {
  return res
    .status(404)
    .json(apiResponse({ status: APIStatus.FAIL, msg: "Page not found" }));
};

const getAllSensorOfDevice = async (req, res, next) => {
  const deviceId = req.query.deviceId;
  const sensors = await getAllSensorOfDeviceDb({ deviceId });
  if (sensors) {
    var dataSensors = getTempAndHumi(sensors);
  }
  return res
    .status(200)
    .json(apiResponse({ status: APIStatus.SUCCESS, data: dataSensors }));
};

const getTempAndHumi = (sensors) => {
  var temp = [];
  var humidity = [];
  sensors.forEach((sensor) => {
    if (sensor.deviceType === 3) {
      temp.push(sensor.temperature);
      humidity.push(sensor.humidityAir);
    }
  });
  return {
    temperature: temp,
    humidityAir: humidity,
  };
};

const getTemperatureTrain = (req, res) => {
  var data = [
    22.97, 22.47, 22.89, 22.83, 22.97, 22.47, 22.89, 22.83, 22.69, 22.54, 22.43,
    22.19, 22.39, 23.49, 24.27, 22.97, 22.47, 22.89, 22.83, 22.69, 22.54, 22.43,
    22.19, 22.39,
  ];
  var modelTraning = spawn("python3", [
    "/home/ubuntu/Downloads/Fire_Alarm_System-master/Model_Train/models/test.py",
  ]);
  modelTraning.stdout.on("data", function (data) {
    console.log("has data");
    console.log(data.toString());

    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: data.toString() }));
    // res.send(data.toString());
  });
};

module.exports = {
  getSensor,
  getDataSensor,
  getAllSensorOfDevice,
  getTemperatureTrain,
};
