const mongoose = require("mongoose");

const sensorSchema = new mongoose.Schema(
  {
    deviceId: {
      type: String,
      required: true,
    },
    deviceType: {
      type: Number,
      required: true,
    },
    humidityAir: {
      type: Number,
      required: false,
      default: 0,
    },
    temperature: {
      type: Number,
      required: false,
      default: 0,
    },
    flameValue: {
      type: Number,
      required: false,
      default: 0,
    },
    MQ2Value: {
      type: Number,
      required: false,
      default: 0,
    },
  },
  { timestamps: true }
);

module.exports = mongoose.model("Sensor", sensorSchema);
