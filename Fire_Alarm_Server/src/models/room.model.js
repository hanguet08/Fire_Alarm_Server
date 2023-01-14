const mongoose = require("mongoose");

const roomSchema = new mongoose.Schema(
  {
    roomName: {
      type: String,
      required: true,
    },
    houseId: {
      type: String,
      required: true,
    },
    area: {
      type: Number,
      required: false,
      min: 1,
    },
    position: {
      type: String,
      required: true,
    },
    owner: {
      type: String,
      required: false,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("Room", roomSchema);
