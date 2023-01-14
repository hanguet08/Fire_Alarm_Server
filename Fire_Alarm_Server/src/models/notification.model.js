const mongoose = require("mongoose");
const notificationStatus = require("../constants/notification.status");

const notificationSchema = new mongoose.Schema(
  {
    device: {
      type: String,
      required: true,
    },
    userId: {
      type: String,
      required: true,
    },
    statusSeen: {
      type: String,
      required: false,
      enum: notificationStatus,
      default: notificationStatus.UNSEEN,
    },
    content: {
      type: String,
      require: true,
    },
    dateTime: {
      type: String,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("Notification", notificationSchema);
