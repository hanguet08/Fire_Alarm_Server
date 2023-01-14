const { Joi } = require("express-validation");
const ROLES_LIST = require("../constants/roles.list");
const loginValidation = {
  body: Joi.object({
    email: Joi.string().email().trim().lowercase().required(),
    password: Joi.string().min(6).required(),
  }),
};

const signupValidation = {
  body: Joi.object({
    email: Joi.string().email().trim().lowercase().required(),
    password: Joi.string().min(6).required(),
    fullName: Joi.string().required(),
    fcmToken: Joi.string(),
    refreshToken: Joi.string(),
    phoneNumber: Joi.string(),
    address: Joi.string(),
    roles: Joi.string(),
  }),
};

module.exports = {
  loginValidation,
  signupValidation,
};
