const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const {
  getAllHouseDb,
  getHouseDb,
  createHouseDb,
  editHouseDb,
  deleteHouseDb,
} = require("../db/house.db");
const console = require("../utils/chalk");

// Get all house of one user
const getAllHouse = async (req, res, next) => {
  const userId = req.user._id;
  const houses = await getAllHouseDb({ userId });
  if (houses)
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: houses }));

  console.error("Error when getAllHouse");
  return next(new Error("Server error!"));
};

// Get one house
const getHouse = async (req, res, next) => {
  const _id = req.params.id;
  const house = await getHouseDb({ _id });
  if (house) {
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: house }));
  } else
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this house",
      })
    );
};

// Insert new house
const createHouse = async (req, res, next) => {
  const userId = req.user._id;
  const { houseName } = req.body;

  // check house exists
  const data = await getHouseDb({ houseName, userId });
  if (data)
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You already have this house",
      })
    );

  // insert house
  const house = await createHouseDb({
    ...req.body,
    userId,
  });
  if (house)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Insert house success",
        data: house,
      })
    );

  console.error("Error when createHouse");
  return next(new Error("Server error!"));
};

// Edit house
const editHouse = async (req, res, next) => {
  const userId = req.user._id,
    _id = req.params.id,
    { houseName } = req.body;

  // check houseForEdit exists
  const houseForEdit = await getHouseDb({ _id, userId });
  if (!houseForEdit)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this house",
      })
    );
  var house = await getHouseDb({ houseName, userId });
  if (house && _id != house._id) {
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You already have this house. Update house failure",
      })
    );
  }

  // edit house
  house = await editHouseDb({ ...req.body, houseForEdit });
  if (house)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "House updated successfully",
        data: house,
      })
    );

  console.error("Error when editHouse");
  return next(new Error("Server error!"));
};

// Delete house
const deleteHouse = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;

  // check houseId
  const houseForDelete = await getHouseDb({ _id, userId });
  if (!houseForDelete)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this house",
      })
    );

  // delete house
  const rs = await deleteHouseDb({ _id });
  if (rs)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "House deleted successfully",
      })
    );

  console.error("Error when deleteHouse");
  return next(new Error("Server error!"));
};

// check if the house belongs to the user
const checkHouseOfUser = async (houseId, userId) => {
  try {
    const house = await getHouseDb({ _id: houseId }),
      userId1 = house.userId;
    if (userId1 == userId) {
      return true;
    } else return false;
  } catch (error) {
    console.error("checkHouseOfUser: " + error);
  }
};

module.exports = {
  getAllHouse,
  getHouse,
  createHouse,
  editHouse,
  deleteHouse,
  checkHouseOfUser,
};
