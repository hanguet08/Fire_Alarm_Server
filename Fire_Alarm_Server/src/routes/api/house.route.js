const router = require("express").Router();
const { verifyToken } = require("../../middlewares/authenticate.middleware");
const { validate } = require("express-validation");
const asyncWrap = require("../../utils/asyncWrap");
const {
  createHouseValidation,
  editHouseValidation,
} = require("../../validations/house.validation.js");
const { verifyRoles } = require("../../middlewares/authorize.middleware");
const ROLES_LIST = require("../../constants/roles.list");
const {
  getAllHouse,
  getHouse,
  createHouse,
  deleteHouse,
  editHouse,
} = require("../../controllers/house.controller");

router.get(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getAllHouse)
);
router.get(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getHouse)
);
router.post(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(createHouseValidation),
  asyncWrap(createHouse)
);
router.put(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(editHouseValidation),
  asyncWrap(editHouse)
);
router.delete(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(deleteHouse)
);

module.exports = router;
