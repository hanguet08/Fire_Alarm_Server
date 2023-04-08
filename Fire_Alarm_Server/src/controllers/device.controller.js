const moment = require("moment");
const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const {
  getAllDeviceInRoomDb,
  getDeviceDb,
  createDeviceDb,
  editDeviceDb,
  deleteDeviceDb,
  controlDeviceDb,
} = require("../db/device.db");
const { getRoomDb } = require("../db/room.db");
const { getHouseDb } = require("../db/house.db");
const { checkRoomOfUser } = require("./room.controller");
const console = require("../utils/chalk");
const {
  mqttClient,
  smart_home_control_device,
} = require("../services/mqtt.service");

// Get all device of one room
const getAllDevicesInRoom = async (req, res, next) => {
  const { roomId } = req.query;
  const isRoomExist = await checkRoomOfUser(roomId, req.user._id);
  if (!isRoomExist) {
    return res.status(400).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this room",
      })
    );
  }
  const devices = await getAllDeviceInRoomDb({ roomId });
  if (devices)
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: devices }));

  console.error("Error when getAllDevicesInRoom");
  return next(new Error("Server error!"));
};

// Get one device
const getDevice = async (req, res, next) => {
  const _id = req.params.id;
  const device = await getDeviceDb({ _id });
  if (device) {
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: device }));
  } else
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this device",
      })
    );
};

// Insert new device
const createDevice = async (req, res, next) => {
  const { deviceName, roomId } = req.body,
    userId = req.user._id;

  // check room (roomId) exist
  const isRoomExist = await checkRoomOfUser(roomId, userId);
  if (!isRoomExist)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this room",
      })
    );

  // check device exist
  const data = await getDeviceDb({ deviceName, roomId });
  if (data)
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You already have this device",
      })
    );

  // insert device
  const device = await createDeviceDb({
    ...req.body,
  });
  if (device)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Insert device success",
        data: device,
      })
    );

  console.error("Error when createDevice");
  return next(new Error("Server error!"));
};

// Edit device
const editDevice = async (req, res, next) => {
  const _id = req.params.id,
    { deviceName } = req.body,
    userId = req.user._id;

  // check device exist
  const isDeviceExist = await checkDeviceOfUser(_id, userId);
  if (!isDeviceExist)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this device",
      })
    );

  const deviceForEdit = await getDeviceDb({ _id });
  var device = await getDeviceDb({ deviceName, roomId: deviceForEdit.roomId });
  if (device && device._id != _id) {
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You already have this device. Update device failure",
      })
    );
  }

  // edit device
  device = await editDeviceDb({ ...req.body, deviceForEdit });
  if (device) {
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Device updated successfully",
        data: device,
      })
    );
  }

  console.error("Error when editDevice");
  return next(new Error("Server error!"));
};

// Delete one device
const deleteDevice = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;

  // check deviceId
  const isDeviceExist = await checkDeviceOfUser(_id, userId);
  if (!isDeviceExist)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this device",
      })
    );

  // delete device
  const rs = await deleteDeviceDb({ _id });
  if (rs)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Device deleted successfully",
      })
    );

  console.error("Error when deleteDevice");
  return next(new Error("Server error!"));
};

// check if the device belongs to the user
const checkDeviceOfUser = async (deviceId, userId) => {
  try {
    const device = await getDeviceDb({ _id: deviceId }),
      roomId = device.roomId,
      room = await getRoomDb({ _id: roomId }),
      houseId = room.houseId,
      house = await getHouseDb({ _id: houseId }),
      userId1 = house.userId;
    if (userId1 == userId) {
      return true;
    } else return false;
  } catch (error) {
    console.error("checkDeviceOfUser: " + error);
  }
};

// control device
const controlDevice = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;

  // check device exist
  const isDeviceExist = await checkDeviceOfUser(_id, userId);
  if (!isDeviceExist)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this device",
      })
    );

  console.log("control device:", req.body);
  // edit status device
  let device = await controlDeviceDb({ ...req.body, _id });
  if (device) {
    let message = "Control device success";
    const messageControl = {
      deviceId: device._id,
      status: device.status,
      mode: device.mode,
    };
    // publish to mqtt
    mqttClient.publish(
      smart_home_control_device,
      JSON.stringify(messageControl)
    );
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: message,
        data: device,
      })
    );
  }

  console.error("Error when controlDevice");
  return next(new Error("Server error!"));
};

module.exports = {
  getAllDevicesInRoom,
  getDevice,
  createDevice,
  editDevice,
  deleteDevice,
  checkDeviceOfUser,
  controlDevice,
};
