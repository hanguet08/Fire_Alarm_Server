const jwt = require("jsonwebtoken");
const { jwtKey, refresh_token_secret } = require("../config");
const console = require("../utils/chalk");
const genAccessToken = (user) => {
  try {
    const token = jwt.sign(
      {
        _id: user._id.toString(),
        email: user.email,
        roles: user.roles,
      },
      jwtKey,
      { expiresIn: "7d" }
    );

    return token;
  } catch (error) {
    console.error("Error when genAccessToken: " + error);
  }
};

const genRefreshToken = (user) => {
  try {
    const token = jwt.sign(
      {
        _id: user._id.toString(),
        email: user.email,
      },
      refresh_token_secret,
      { expiresIn: "10d" }
    );

    return token;
  } catch (error) {
    console.error("Error when genRefreshToken: " + error);
  }
};

module.exports = { genAccessToken, genRefreshToken };
