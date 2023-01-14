const bcrypt = require("bcryptjs");
const console = require("../utils/chalk");
const hashPassword = async (password) => {
  try {
    const hashed = await bcrypt.hash(password, 10);
    return hashed;
  } catch (error) {
    console.error("hashPassword: " + error);
  }
};

module.exports = hashPassword;
