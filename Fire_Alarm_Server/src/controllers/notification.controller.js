const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");
const {
  getAllNotificationDb,
  getNotificationDb,
  createNotificationDb,
  editNotificationDb,
  deleteNotificationDb,
} = require("../db/notification.db");
const console = require("../utils/chalk");

// Get all notifications of one user
const getAllNotifications = async (req, res, next) => {
  const userId = req.user._id;
  const notifications = await getAllNotificationDb({ userId });
  if (notifications) {
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: notifications }));
  }

  console.error("Error when getAllNotifications");
  return next(new Error("Server error!"));
};

// Get one notification
const getNotification = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;
  const notification = await getNotificationDb({ _id, userId });
  if (notification) {
    return res
      .status(200)
      .json(apiResponse({ status: APIStatus.SUCCESS, data: notification }));
  } else
    return res
      .status(404)
      .json(
        apiResponse({ status: APIStatus.FAIL, msg: "Notification not found" })
      );
};

// Insert new notification
const createNotification = async (req, res, next) => {
  const userId = req.user._id;
  const notification = await createNotificationDb({
    ...req.body,
    userId,
  });
  if (notification)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Insert notification success!",
        data: notification,
      })
    );

  console.error("Error when createNotification");
  return next(new Error("Server error!"));
};

// Edit notification
const editNotification = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;

  // check notification (notificationId) exists
  const notificationForEdit = await getNotificationDb({ _id, userId });
  if (!notificationForEdit)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this notification",
      })
    );

  // edit notification
  const notification = await editNotificationDb({
    notificationForEdit,
    ...req.body,
  });
  if (notification) {
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Edit success this notification",
        data: notification,
      })
    );
  }

  console.error("Error when editNotification");
  return next(new Error("Server error!"));
};

// Delete notification
const deleteNotification = async (req, res, next) => {
  const _id = req.params.id,
    userId = req.user._id;

  // check notification (notificationId) exists
  const data = await getNotificationDb({ _id, userId });
  if (!data)
    return res.status(404).json(
      apiResponse({
        status: APIStatus.FAIL,
        msg: "You don't have this notification",
      })
    );

  // delete notification
  const rs = await deleteNotificationDb({ _id });
  if (rs)
    return res.status(200).json(
      apiResponse({
        status: APIStatus.SUCCESS,
        msg: "Notification deleted successfully",
      })
    );

  console.error("Error when deleteNotification");
  return next(new Error("Server error!"));
};

module.exports = {
  getAllNotifications,
  getNotification,
  createNotification,
  editNotification,
  deleteNotification,
};
