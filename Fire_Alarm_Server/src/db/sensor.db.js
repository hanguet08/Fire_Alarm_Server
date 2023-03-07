const Sensor = require("../models/sensor.model.js");

// Get data from sensor
const getSensorDb = async (query) => {
  try {
    const sensors = await Sensor.findOne(query);
    return sensors;
  } catch (error) {
    console.error("getSensorDb: " + error);
  }
};

// Insert data
const insertDataSensorDb = async (query) => {
  try {
    const rs = await new Sensor(query).save();
    return rs;
  } catch (error) {
    console.error("insertDataSensorDb: " + error);
  }
};

// get all sensor in device
const getAllSensorOfDeviceDb = async (query) => {
  try {
    const sensors = await Sensor.find(query).limit(24);
    return sensors;
  } catch (error) {
    console.error("getAllSensorOfDevice: " + error);
  }
};

module.exports = {
  getSensorDb,
  insertDataSensorDb,
  getAllSensorOfDeviceDb,
};
