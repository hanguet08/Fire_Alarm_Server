const jwt = require("jsonwebtoken");
const { jwtKey } = require("../config");
const APIStatus = require("../constants/APIStatus");
const apiResponse = require("../utils/apiResponse");
const console = require("../utils/chalk");
const { getUserDb } = require("../db/user.db");

// authenticate
const verifyToken = async (req, res, next) => {
  const token = getHeaderToken(req);
  if (!token)
    return res.status(401).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You are not authenticated",
      })
    );
  try {
    const decode = jwt.verify(token, jwtKey);
    const user = await getUserDb({ _id: decode._id });
    if (!user)
      return res
        .status(400)
        .json(apiResponse({ status: APIStatus.FAIL, msg: "Invalid token" }));

    req.user = user;
    req.roles = user.roles;
    next();
  } catch (err) {
    console.error("auth: " + err);
    return res
      .status(400)
      .json(apiResponse({ status: APIStatus.FAIL, msg: "Invalid token" }));
  }
};

const getHeaderToken = (req) => {
  const originalToken =
    req.header("Authorization") || req.header("x-access-token");
  if (!originalToken) return null;
  const token = originalToken.replace("Bearer ", "");
  return token;
};

module.exports = {
  verifyToken,
};
