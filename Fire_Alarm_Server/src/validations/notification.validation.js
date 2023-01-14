const { Joi } = require("express-validation");
const notificationStatus = require("../constants/notification.status");
const createNotificationValidation = {
  body: Joi.object({
    device: Joi.string().required(),
    content: Joi.string().required(),
    statusSeen: Joi.string().valid(
      notificationStatus.SEEN,
      notificationStatus.UNSEEN
    ),
    dateTime: Joi.string(),
  }),
};

const editNotificationValidation = {
  body: Joi.object({
    device: Joi.string(),
    statusSeen: Joi.string().valid(
      notificationStatus.SEEN,
      notificationStatus.UNSEEN
    ),
    content: Joi.string(),
    dateTime: Joi.string(),
  }),
};

module.exports = {
  createNotificationValidation,
  editNotificationValidation,
};
