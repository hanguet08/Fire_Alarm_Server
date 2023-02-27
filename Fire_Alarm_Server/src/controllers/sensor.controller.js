const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
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

module.exports = {
  getSensor,
  getDataSensor,
  getAllSensorOfDevice,
};
