const router = require("express").Router();
const { verifyToken } = require("../../middlewares/authenticate.middleware");
const asyncWrap = require("../../utils/asyncWrap");
const { validate } = require("express-validation");
const {
  createDeviceValidation,
  editDeviceValidation,
} = require("../../validations/device.validation");
const {
  getDevice,
  getAllDevicesInRoom,
  createDevice,
  deleteDevice,
  editDevice,
} = require("../../controllers/device.controller");
const { verifyRoles } = require("../../middlewares/authorize.middleware");
const ROLES_LIST = require("../../constants/roles.list");

// Get all devices in house
router.get(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getAllDevicesInRoom)
);
router.get(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getDevice)
);
router.post(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(createDeviceValidation),
  asyncWrap(createDevice)
);
router.put(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(editDeviceValidation),
  asyncWrap(editDevice)
);
router.delete(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(deleteDevice)
);

module.exports = router;
