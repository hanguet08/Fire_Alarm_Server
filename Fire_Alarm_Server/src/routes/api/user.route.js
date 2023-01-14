const router = require("express").Router();
const { verifyToken } = require("../../middlewares/authenticate.middleware");
const asyncWrap = require("../../utils/asyncWrap");
const { validate } = require("express-validation");
const { verifyRoles } = require("../../middlewares/authorize.middleware");
const ROLES_LIST = require("../../constants/roles.list");
const {
  updateInfoUserValidation,
  changePasswordValidation,
} = require("../../validations/user.validation");
const {
  getInfo,
  changePassword,
  updateInfo,
  saveToken,
} = require("../../controllers/user.controller");

router.get(
  "/info",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getInfo)
);

router.post(
  "/change-password",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(changePasswordValidation),
  asyncWrap(changePassword)
);
router.post(
  "/info",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(updateInfoUserValidation),
  asyncWrap(updateInfo)
);
router.post(
  "/tokenFCM",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(saveToken)
);

module.exports = router;
