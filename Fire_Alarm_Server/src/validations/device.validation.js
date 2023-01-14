const { Joi } = require("express-validation");
const deviceStatus = require("../constants/device.status");
const deviceType = require("../constants/device.type");

const createDeviceValidation = {
  body: Joi.object({
    deviceName: Joi.string().required(),
    roomId: Joi.string().required(),
    status: Joi.string().required().valid(deviceStatus.ON, deviceStatus.OFF),
    position: Joi.string().required(),
    deviceType: Joi.number()
      .required()
      .valid(
        deviceType.FLAME_SENSOR,
        deviceType.MQ2_SENSOR,
        deviceType.DHT11_SENSOR
      ),
  }),
};

const editDeviceValidation = {
  body: Joi.object({
    deviceName: Joi.string(),
    status: Joi.string().valid(deviceStatus.OFF, deviceStatus.ON),
    roomId: Joi.string(),
    position: Joi.string(),
    deviceType: Joi.number().valid(
      deviceType.FLAME_SENSOR,
      deviceType.MQ2_SENSOR,
      deviceType.DHT11_SENSOR
    ),
  }),
};

module.exports = {
  createDeviceValidation,
  editDeviceValidation,
};
