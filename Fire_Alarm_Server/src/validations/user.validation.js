const { Joi } = require("express-validation");

const updateInfoUserValidation = {
  body: Joi.object({
    fullName: Joi.string(),
    phoneNumber: Joi.string(),
    address: Joi.string(),
  }),
};

const changePasswordValidation = {
  body: Joi.object({
    oldPassword: Joi.string().required(),
    newPassword: Joi.string().required(),
  }),
};

module.exports = {
  updateInfoUserValidation,
  changePasswordValidation,
};
