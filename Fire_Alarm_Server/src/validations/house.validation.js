const { Joi } = require("express-validation");

const createHouseValidation = {
  body: Joi.object({
    houseName: Joi.string().trim().required(),
    address: Joi.string().required(),
    floor: Joi.number().required().min(1),
    members: Joi.number().required().min(0),
    area: Joi.number().min(1),
  }),
};

const editHouseValidation = {
  body: Joi.object({
    houseName: Joi.string(),
    address: Joi.string(),
    floor: Joi.number().min(1),
    members: Joi.number().min(0),
    area: Joi.number().min(1),
  }),
};

module.exports = {
  createHouseValidation,
  editHouseValidation,
};
