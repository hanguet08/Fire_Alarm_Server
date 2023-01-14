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

module.exports = {
  getSensorDb,
  insertDataSensorDb,
};
