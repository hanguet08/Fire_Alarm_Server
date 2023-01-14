const mongoose = require("mongoose");

const houseSchema = new mongoose.Schema(
  {
    houseName: {
      type: String,
      required: true,
    },
    address: {
      type: String,
      required: true,
    },
    floor: {
      type: Number,
      min: 1,
    },
    members: {
      type: Number,
      min: 0,
    },
    userId: {
      type: String,
      required: true,
    },
    area: {
      type: Number,
      required: false,
      min: 1,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("House", houseSchema);
