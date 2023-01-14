const apiResponse = ({ status, msg, data }) => {
  return {
    status,
    message: msg,
    data,
  };
};

module.exports = apiResponse;
