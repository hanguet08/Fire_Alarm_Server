const router = require("express").Router();
const { validate } = require("express-validation");
const {
  loginValidation,
  signupValidation,
} = require("../validations/auth.validation");
const asyncWrap = require("../utils/asyncWrap");
const { verifyToken } = require("../middlewares/authenticate.middleware");
const {
  login,
  logout,
  register,
  refreshToken,
} = require("../controllers/auth.controller");

router.post("/user/login", validate(loginValidation), asyncWrap(login));
router.post("/user/logout", verifyToken, asyncWrap(logout));
router.post("/user/register", validate(signupValidation), asyncWrap(register));
router.post("/refresh-token", asyncWrap(refreshToken));
module.exports = router;
