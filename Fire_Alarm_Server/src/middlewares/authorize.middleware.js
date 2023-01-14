const APIStatus = require("../constants/APIStatus");
const apiResponse = require("../utils/apiResponse");
const ROLES_LIST = require("../constants/roles.list");
const console = require("../utils/chalk");

const verifyRoles = (...allowedRoles) => {
  return (req, res, next) => {
    // if account is Admin => next()
    if (req.roles.includes(ROLES_LIST.Admin)) return next();

    if (!req?.roles) {
      return res.status(403).json(
        apiResponse({
          status: APIStatus.FAIL,
          msg: "You are not authorized",
        })
      );
    }
    const rolesArray = [...allowedRoles];
    const result = req.roles
      .map((role) => rolesArray.includes(role))
      .find((val) => val === true);
    if (!result) {
      if (allowedRoles.length === 0) console.warn("Allowed Roles: ADMIN");
      else console.warn("Allowed Roles: " + allowedRoles);

      return res.status(403).json(
        apiResponse({
          status: APIStatus.FAIL,
          msg: "You are not authorized",
        })
      );
    }
    next();
  };
};

module.exports = { verifyRoles };
