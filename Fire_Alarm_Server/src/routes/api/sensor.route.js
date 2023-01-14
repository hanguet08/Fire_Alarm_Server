const router = require("express").Router();
const { verifyToken } = require("../../middlewares/authenticate.middleware");
const asyncWrap = require("../../utils/asyncWrap");
const {
  getSensor,
  getDataSensor,
} = require("../../controllers/sensor.controller");
const { verifyRoles } = require("../../middlewares/authorize.middleware");
const ROLES_LIST = require("../../constants/roles.list");

router.get(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getSensor)
);
router.post(
  "/get-data",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getDataSensor)
);

module.exports = router;
