const Room = require("../models/room.model.js");
const console = require("../utils/chalk");

// Get all room in house
const getAllRoomInHouseDb = async (query) => {
  try {
    const rooms = await Room.find(query);
    return rooms;
  } catch (error) {
    console.error("getAllRoomInHouseDb: " + error);
  }
};

// Get room (more)
const getRoomDb = async (query) => {
  try {
    const room = await Room.findOne(query);
    return room;
  } catch (error) {
    console.error("getRoomDb: " + error);
  }
};

// Insert one room
const createRoomDb = async (query) => {
  try {
    const room = await new Room(query).save();
    return room;
  } catch (error) {
    console.error("createRoomDb: " + error);
  }
};

// Edit one room
const editRoomDb = async (query) => {
  try {
    const { roomName, position, area, owner, roomForEdit } = query;

    roomForEdit.roomName = roomName;
    roomForEdit.position = position;
    roomForEdit.owner = owner;
    roomForEdit.area = area;

    const rs = await roomForEdit.save();
    return rs;
  } catch (error) {
    console.error("editRoomDb: " + error);
  }
};

// Delete one room
const deleteRoomDb = async (query) => {
  try {
    const rs = await Room(query).delete();
    return rs;
  } catch (error) {
    console.error("deleteRoomDb: " + error);
  }
};

module.exports = {
  getAllRoomInHouseDb,
  getRoomDb,
  createRoomDb,
  editRoomDb,
  deleteRoomDb,
};
