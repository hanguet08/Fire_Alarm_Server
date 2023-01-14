const User = require("../models/user.model");
const console = require("../utils/chalk");

// Get all users
const getAllUserDb = async (query) => {
  try {
    const users = User.find(query);
    return users;
  } catch (error) {
    console.error("getAllUserDb: " + error);
  }
};

// Get one user
const getUserDb = async (query) => {
  try {
    const user = await User.findOne(query);
    return user;
  } catch (error) {
    console.error("getUserDb: " + error);
  }
};

// Create user
const createUserDb = async (query) => {
  try {
    const user = await new User(query).save();
    return user;
  } catch (error) {
    console.error("createUserDb: " + error);
  }
};

// Edit info
const editUserDb = async (query) => {
  try {
    const { _id, fullName, address, phoneNumber } = query;
    const userForEdit = await getUserDb({ _id });

    userForEdit.fullName = fullName;
    userForEdit.address = address;
    userForEdit.phoneNumber = phoneNumber;

    const rs = await userForEdit.save();
    return rs;
  } catch (error) {
    console.error("editUserDb: " + error);
  }
};

// Change password
const changePasswordDb = async (query) => {
  try {
    const { user, password } = query;
    user.password = password;

    const rs = await user.save();
    return rs;
  } catch (error) {
    console.error("changePasswordDb: " + error);
  }
};

// Save token FCM
const saveTokenFcmDb = async (query) => {
  try {
    const { fcmToken, _id } = query;
    const user = await User.findById(_id);
    user.fcmToken = fcmToken;

    const rs = await user.save();
    return rs;
  } catch (error) {
    console.error("saveTokenFcmDb: " + error);
  }
};

// Save refresh token
const saveRefreshTokenDb = async (query) => {
  try {
    const { refreshToken, _id } = query;
    const user = await User.findById(_id);
    user.refreshToken = refreshToken;

    const rs = await user.save();
    return rs;
  } catch (error) {
    console.error("saveRefreshTokenDb: " + error);
  }
};

// Save refresh token
const deleteRefreshTokenDb = async (query) => {
  try {
    const _id = query;
    const user = await User.findById(_id);
    user.refreshToken = "";

    const rs = await user.save();
    return rs;
  } catch (error) {
    console.error("deleteRefreshTokenDb: " + error);
  }
};

// Get one user
const getRefreshTokenDb = async (query) => {
  try {
    const user = await User.findOne(query);
    return user.refreshToken;
  } catch (error) {
    console.error("getUserDb: " + error);
  }
};

module.exports = {
  getAllUserDb,
  getUserDb,
  createUserDb,
  editUserDb,
  changePasswordDb,
  saveTokenFcmDb,
  saveRefreshTokenDb,
  getRefreshTokenDb,
  deleteRefreshTokenDb,
};
