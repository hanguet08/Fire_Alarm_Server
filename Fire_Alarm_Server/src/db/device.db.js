const Device = require("../models/device.model.js");

// Get all device of user
const getAllDeviceInRoomDb = async (query) => {
  try {
    const devices = await Device.find(query);
    return devices;
  } catch (error) {
    console.error("getAllDeviceInRoomDb: " + error);
  }
};

// Get one device
const getDeviceDb = async (query) => {
  try {
    const device = await Device.findOne(query);
    return device;
  } catch (error) {
    console.error("getDeviceDb: " + error);
  }
};

// Create one device
const createDeviceDb = async (query) => {
  try {
    const device = await new Device(query).save();
    return device;
  } catch (error) {
    console.error("createDeviceDb: " + error);
  }
};

// Edit device
const editDeviceDb = async (query) => {
  try {
    const { deviceName, position, status, deviceForEdit, deviceType } = query;

    deviceForEdit.position = position;
    deviceForEdit.deviceName = deviceName;
    deviceForEdit.deviceType = deviceType;

    const rs = await deviceForEdit.save();
    return rs;
  } catch (error) {
    console.error("editDeviceDb: " + error);
  }
};

// Delete one device
const deleteDeviceDb = async (query) => {
  try {
    const rs = await Device(query).delete();
    return rs;
  } catch (error) {
    console.error("deleteDeviceDb: " + error);
  }
};

// Control one device
const controlDeviceDb = async (query) => {
  try {
    const { mode, status, _id } = query,
      device = await getDeviceDb({ _id });
    device.status = status;
    device.mode = mode;
    const rs = await device.save();
    return rs;
  } catch (error) {
    console.error("controlDeviceDb: " + error);
  }
};

module.exports = {
  getAllDeviceInRoomDb,
  getDeviceDb,
  createDeviceDb,
  deleteDeviceDb,
  editDeviceDb,
  controlDeviceDb,
};
