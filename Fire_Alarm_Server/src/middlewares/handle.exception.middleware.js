const apiResponse = require("../utils/apiResponse");
const APIStatus = require("../constants/APIStatus");

module.exports = function handleExceptions(app) {
  app.use((err, req, res, next) => {
    if (err) {
      if (err.details) {
        var data = err;
      } else data = null;
      return res.status(err.statusCode || 500).json(
        apiResponse({
          status: APIStatus.FAIL,
          msg: err.message,
          data: data,
        })
      );
    }

    return res
      .status(500)
      .json(
        apiResponse({ status: APIStatus.ERROR, msg: "Internal Server error" })
      );
  });
};
