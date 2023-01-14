const { Joi } = require("express-validation");

const createRoomValidation = {
  body: Joi.object({
    roomName: Joi.string().required(),
    position: Joi.string().required(),
    area: Joi.number().min(1),
    houseId: Joi.string().required(),
    owner: Joi.string(),
  }),
};

const editRoomValidation = {
  body: Joi.object({
    roomName: Joi.string(),
    position: Joi.string(),
    area: Joi.number().min(1),
    houseId: Joi.string(),
    owner: Joi.string(),
  }),
};

module.exports = {
  createRoomValidation,
  editRoomValidation,
};
