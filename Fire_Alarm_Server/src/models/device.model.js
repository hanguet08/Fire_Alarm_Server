const mongoose = require("mongoose");
const deviceStatus = require("../constants/device.status");
const deviceType = require("../constants/device.type");
const deviceMode = require("../constants/device.mode");

const deviceSchema = new mongoose.Schema(
  {
    deviceName: {
      type: String,
      required: true,
    },
    deviceType: {
      type: Number,
      enum: deviceType,
      required: true,
    },
    roomId: {
      type: String,
      required: true,
    },
    status: {
      type: String,
      enum: deviceStatus,
      default: deviceStatus.OFF,
    },
    position: {
      type: String,
      require: true,
    },
    mode: {
      type: String,
      enum: deviceMode,
      default: deviceMode.MANUAL,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("Device", deviceSchema);
