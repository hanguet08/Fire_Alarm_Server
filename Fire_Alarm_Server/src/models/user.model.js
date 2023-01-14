const mongoose = require("mongoose");
const Joi = require("joi");
const ROLES_LIST = require("../constants/roles.list");
const userSchema = new mongoose.Schema(
  {
    email: {
      type: String,
      required: true,
      unique: true,
    },
    password: {
      type: String,
      required: true,
    },
    fcmToken: {
      type: String,
      default: "",
      required: false,
    },
    fullName: {
      type: String,
      required: true,
    },
    phoneNumber: {
      type: String,
      default: "",
      required: false,
    },
    address: {
      type: String,
      default: "",
      required: false,
    },
    refreshToken: {
      type: String,
      default: "",
      required: false,
    },
    roles: {
      type: [
        {
          type: String,
          enum: [ROLES_LIST.User, ROLES_LIST.Manager, ROLES_LIST.Admin],
        },
      ],
      default: [ROLES_LIST.User],
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("User", userSchema);
