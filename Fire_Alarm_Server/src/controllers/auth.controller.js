const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const {
  getUserDb,
  createUserDb,
  getRefreshTokenDb,
  deleteRefreshTokenDb,
} = require("../db/user.db");
const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const { genAccessToken, genRefreshToken } = require("../utils/genToken");
const hashPassword = require("../utils/hashPassword");
const { refresh_token_secret } = require("../config/index");
const { saveRefreshTokenDb } = require("../db/user.db");
const console = require("../utils/chalk");

// Login
const login = async (req, res, next) => {
  const { email, password } = req.body;
  if (!email || !password)
    return res.status(400).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "Email and password are required.",
      })
    );

  // check email exists
  const user = await getUserDb({ email });
  if (!user) {
    return res.status(401).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "Email doesn't exist",
      })
    );
  }

  // check password
  bcrypt.compare(password, user.password, async (err, result) => {
    if (result) {
      const accessToken = genAccessToken(user);
      const refreshToken = genRefreshToken(user);
      const { fcmToken } = user._doc;
      if (accessToken && refreshToken) {
        await saveRefreshTokenDb({ refreshToken, _id: user._id });
        return res.status(200).json(
          apiResponse({
            status: APIStatus.SUCCESS,
            msg: "Login successfully!",
            data: { accessToken, refreshToken, userId: user._id, fcmToken },
          })
        );
      }
      return next(new Error("Server error!"));
    }
    if (err) {
      console.error("Error when compare password (login): " + err);
      return next(new Error("Server error!"));
    }
    return res.status(400).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "Password is wrong",
      })
    );
  });
};

// logout
const logout = async (req, res, next) => {
  // delete refresh token user
  const refreshToken = await getRefreshTokenDb({ _id: req.user._id });
  if (refreshToken) {
    const rs = await deleteRefreshTokenDb(req.user._id);
    if (rs)
      return res.status(200).json(
        apiResponse({
          status: APIStatus.SUCCESS,
          msg: "Logout successfully!",
        })
      );
  } else {
    return res.status(401).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You are not authenticated",
      })
    );
  }

  console.error("Error when logout: " + err);
  return next(new Error("Server error!"));
};

// Register
const register = async (req, res, next) => {
  const { email, password } = req.body;

  // check email exists
  const user = await getUserDb({ email });
  if (user) {
    return res.status(409).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "Email already exist!",
      })
    );
  }

  // hash password
  const hashedPw = await hashPassword(password);

  // register user
  const userForRegister = await createUserDb({
    ...req.body,
    password: hashedPw,
  });
  if (userForRegister) {
    let { password, ...infoNoPass } = userForRegister._doc;
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Register successfully!",
        data: infoNoPass,
      })
    );
  }

  console.error("Error when register - auth.controller");
  return next(new Error("Server error!"));
};

// refresh token
const refreshToken = async (req, res, next) => {
  const { refreshToken } = req.body;

  // check if the refresh belongs to the user
  try {
    const decode = jwt.verify(refreshToken, refresh_token_secret);
    const user = await getUserDb({ _id: decode._id, refreshToken });
    if (!user)
      return res
        .status(400)
        .json(
          apiResponse({ status: APIStatus.FAIL, msg: "Invalid refresh token" })
        );
    const accessToken = genAccessToken(user);
    if (accessToken)
      return res
        .status(200)
        .json(
          apiResponse({ status: APIStatus.SUCCESS, data: { accessToken } })
        );
  } catch (err) {
    console.error("refreshToken: " + err);
    return res
      .status(400)
      .json(
        apiResponse({ status: APIStatus.FAIL, msg: "Invalid refresh token" })
      );
  }
};
module.exports = {
  login,
  logout,
  register,
  refreshToken,
};
