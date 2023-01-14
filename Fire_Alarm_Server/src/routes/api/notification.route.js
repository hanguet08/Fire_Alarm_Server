const router = require("express").Router();
const { verifyToken } = require("../../middlewares/authenticate.middleware");
const asyncWrap = require("../../utils/asyncWrap");
const { verifyRoles } = require("../../middlewares/authorize.middleware");
const ROLES_LIST = require("../../constants/roles.list");
const { validate } = require("express-validation");
const {
  createNotificationValidation,
  editNotificationValidation,
} = require("../../validations/notification.validation");
const {
  getAllNotifications,
  getNotification,
  createNotification,
  editNotification,
  deleteNotification,
} = require("../../controllers/notification.controller");

// Get all notifications
router.get(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getAllNotifications)
);
router.get(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(getNotification)
);
router.post(
  "/",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(createNotificationValidation),
  asyncWrap(createNotification)
);
router.put(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  validate(editNotificationValidation),
  asyncWrap(editNotification)
);
router.delete(
  "/:id",
  verifyToken,
  verifyRoles(ROLES_LIST.User),
  asyncWrap(deleteNotification)
);

module.exports = router;
