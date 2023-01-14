const router = require("express").Router();
const { verifyToken } = require("../../middlewares/authenticate.middleware");
const asyncWrap = require("../../utils/asyncWrap");
const { validate } = require("express-validation");
const {
  createRoomValidation,
  editRoomValidation,
} = require("../../validations/room.validation");
const { verifyRoles } = require("../../middlewares/authorize.middleware");
const ROLES_LIST = require("../../constants/roles.list");
const {
  getAllRoomInHouse,
  getRoom,
  createRoom,
  deleteRoom,
  editRoom,
} = require("../../controllers/room.controller");

router.get(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getAllRoomInHouse)
);
router.get(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getRoom)
);
router.post(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(createRoomValidation),
  asyncWrap(createRoom)
);
router.put(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(editRoomValidation),
  asyncWrap(editRoom)
);
router.delete(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(deleteRoom)
);

module.exports = router;
