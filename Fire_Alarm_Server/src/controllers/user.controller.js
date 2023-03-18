const bcrypt = require("bcryptjs/dist/bcrypt");
const APIStatus = require("../constants/APIStatus");
const hashPassword = require("../utils/hashPassword");
const apiResponse = require("../utils/apiResponse");
const console = require("../utils/chalk");
const {
  sendMessageFCM,
} = require("../services/firebase.cloud.message.service");
const {
  getUserDb,
  editUserDb,
  changePasswordDb,
  saveTokenFcmDb,
} = require("../db/user.db");

// Edit info
const updateInfo = async (req, res, next) => {
  const _id = req.user._id;
  const userUpdate = editUserDb({ _id, ...req.body });
  if (userUpdate)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Update user successfully",
      })
    );

  console.error("Error when updateInfo");
  return next(new Error("Server error!"));
};

// get info user
const getInfo = async (req, res, next) => {
  const user = req.user;
  if (!user) {
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "User not found",
      })
    );
  }
  const { password, ...info } = user._doc;
  return res
    .status(200)
    .json(apiResponse({ status: APIStatus.SUCCESS, data: info }));
};

// changePassword
const changePassword = async (req, res, next) => {
  const { oldPassword, newPassword } = req.body;
  const userId = req.user._id;
  const user = await getUserDb({ _id: userId });
  if (!user) {
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "User not found",
      })
    );
  }

  bcrypt.compare(oldPassword, user.password, async (err, result) => {
    if (result) {
      if (oldPassword === newPassword) {
        return res.status(409).json(
          apiResponse({
            status: APIStatus.FAIL,
            msg: "Old password and new password cannot be same",
          })
        );
      }
      // hash password
      const hashedPw = await hashPassword(newPassword);

      // change password
      const rs = await changePasswordDb({
        user,
        password: hashedPw,
      });
      if (rs) {
        return res.status(200).json(
          apiResponse({
            status: APIStatus.SUCCESS,
            msg: "Change password successfully!",
          })
        );
      }

      return next(new Error("Server error!"));
    }
    if (err) {
      console.error("Error when changePassword (change password): " + err);
      return next(new Error("Server error!"));
    }
    return res
      .status(400)
      .json(
        apiResponse({ status: APIStatus.FAIL, msg: "Old password is wrong" })
      );
  });
};

// save token FCM
const saveToken = async (req, res) => {
  const { fcmToken } = req.body;
  const _id = req.user._id;
  const rs = await saveTokenFcmDb({ fcmToken, _id });
  if (rs)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Update token FCM successfully!",
      })
    );

  console.error("Error when saveToken FCM");
  return next(new Error("Server error!"));
};

const postTokenFcm = async (req, res) => {
  var messageFCM = {
    title: "CẢNH BÁO NGUY HIỂM",
    body: "Phát hiện lửa tại P101",
  };
  const fcmToken =
    "fmFrCP4USSCeRPi-Ul4AgG:APA91bGxK4xnQtyldoYmtp4Bxq0VNm8XjK2Y2xrqRuDjRjCnmN-6iaXxgNOKRrueaQXZWe6Ihl3CirBfsiCoxI_2eLtdGLGrL5z_LLUmxt98NHAcT6IgDKeizpwJqD_bxyoqGbxeFtUI";
  sendMessageFCM(fcmToken, messageFCM);
};

module.exports = {
  getInfo,
  updateInfo,
  changePassword,
  saveToken,
  postTokenFcm,
};
