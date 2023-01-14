const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const {
  getAllRoomInHouseDb,
  getRoomDb,
  createRoomDb,
  editRoomDb,
  deleteRoomDb,
} = require("../db/room.db");
const { getHouseDb } = require("../db/house.db");
const { checkHouseOfUser } = require("./house.controller");
const console = require("../utils/chalk");

// Get all room of one house
const getAllRoomInHouse = async (req, res, next) => {
  const houseId = req.query.houseId;
  const isHouseExist = await checkHouseOfUser(houseId, req.user._id);
  if (!isHouseExist) {
    return res.status(400).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this house",
      })
    );
  }
  const rooms = await getAllRoomInHouseDb({ houseId });
  if (rooms)
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: rooms }));

  console.error("Error when getAllRoomInHouse");
  return next(new Error("Server error!"));
};

// Get one room
const getRoom = async (req, res, next) => {
  const _id = req.params.id;
  const room = await getRoomDb({ _id });
  if (room) {
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: room }));
  } else
    return res
      .status(404)
      .json(
        apiResponse({ status: APIStatus.FAIL, msg: "You don't have this room" })
      );
};

// Insert new room
const createRoom = async (req, res, next) => {
  const { roomName, houseId } = req.body,
    userId = req.user._id;

  // check house (houseId) exists
  const isHouseExists = await checkHouseOfUser(houseId, userId);
  if (!isHouseExists) {
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this house",
      })
    );
  }

  // check room exists (when house exists)
  const data = await getRoomDb({ roomName, houseId });
  if (data)
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You already have this room",
      })
    );

  // insert room
  const room = await createRoomDb({
    ...req.body,
  });
  if (room)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Insert room success",
        data: room,
      })
    );

  console.error("Error when createRoom");
  return next(new Error("Server error!"));
};

// Edit room
const editRoom = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id,
    { roomName } = req.body;

  // check roomId exists
  const isRoomExist = await checkRoomOfUser(_id, userId);
  if (!isRoomExist)
    return res
      .status(404)
      .json(
        apiResponse({ status: APIStatus.FAIL, msg: "You don't have this room" })
      );

  const roomForEdit = await getRoomDb({ _id });
  // check roomEdit exists
  var room = await getRoomDb({ roomName, houseId: roomForEdit.houseId });
  if (room && _id != room.id) {
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You already have this room. Update room failure",
      })
    );
  }

  // edit room
  room = await editRoomDb({ ...req.body, roomForEdit });
  if (room)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Room updated successfully",
        data: room,
      })
    );

  console.error("Error when editRoom");
  return next(new Error("Server error!"));
};

// Delete one room
const deleteRoom = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;

  // check roomId exists
  const isRoomExist = await checkRoomOfUser(_id, userId);
  if (!isRoomExist)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this room",
      })
    );

  // delete room
  const rs = await deleteRoomDb({ _id });
  if (rs)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Room deleted successfully",
      })
    );

  console.error("Error when deleteRoom");
  return next(new Error("Server error!"));
};

// check if the room belongs to the user
const checkRoomOfUser = async (roomId, userId) => {
  try {
    const room = await getRoomDb({ _id: roomId }),
      houseId = room.houseId,
      house = await getHouseDb({ _id: houseId }),
      userId1 = house.userId;
    if (userId1 == userId) {
      return true;
    } else return false;
  } catch (error) {
    console.error("checkRoomOfUser: " + error);
  }
};

module.exports = {
  getAllRoomInHouse,
  getRoom,
  createRoom,
  editRoom,
  deleteRoom,
  checkRoomOfUser,
};
