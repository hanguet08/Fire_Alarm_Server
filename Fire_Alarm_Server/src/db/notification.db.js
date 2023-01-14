const Notification = require("../models/notification.model");
const moment = require("moment");

// Get all notifications
const getAllNotificationDb = async (query) => {
  try {
    const notifications = await Notification.find(query)
      .sort({ _id: -1 })
      .limit(15);
    return notifications;
  } catch (error) {
    console.error("getAllNotificationDb" + error);
  }
};

// Get one notification
const getNotificationDb = async (query) => {
  try {
    const notification = await Notification.findOne(query);
    return notification;
  } catch (error) {
    console.error("getNotificationDb" + error);
  }
};

// Create notification
const createNotificationDb = async (query) => {
  try {
    const date = moment().format("dddd, MMMM Do YYYY, ").toString();
    const time = moment().format(":mm:ss").toString();
    var hours = new Date().getHours() + 7;
    if (hours >= 24) hours = hours - 24;
    const dateTime = date + hours + time;

    const notification = await new Notification({ ...query, dateTime }).save();
    return notification;
  } catch (error) {
    console.error("createNotificationDb" + error);
  }
};

// Update info
const editNotificationDb = async (query) => {
  try {
    const { content, statusSeen, notificationForEdit } = query;

    notificationForEdit.content = content;
    notificationForEdit.statusSeen = statusSeen;

    const rs = await notificationForEdit.save();
    return rs;
  } catch (error) {
    console.error("editNotificationDb" + error);
  }
};

// Delete one house
const deleteNotificationDb = async (query) => {
  try {
    const rs = await Notification(query).delete();
    return rs;
  } catch (error) {
    console.error("deleteNotificationDb" + error);
  }
};

module.exports = {
  getAllNotificationDb,
  getNotificationDb,
  createNotificationDb,
  editNotificationDb,
  deleteNotificationDb,
};
