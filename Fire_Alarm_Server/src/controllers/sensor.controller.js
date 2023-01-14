const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const { getSensorDb, insertDataSensorDb } = require("../db/sensor.db");

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

module.exports = {
  getSensor,
  getDataSensor,
};
