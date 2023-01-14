const port = process.env.PORT || 5000;
const jwtKey = process.env.TOKEN_SECRET || "secret_key_jwt";
// const database_url =
//   process.env.DATABASE_URL || "mongodb://localhost:27017/firm_alarm";
const database_url =
  process.env.DATABASE_URL ||
  "mongodb+srv://hanguet08:hanguet08@cluster0.8iws48a.mongodb.net/fire_alarm?retryWrites=true&w=majority";
const refresh_token_secret =
  process.env.REFRESH_TOKEN_SECRET || "refresh_token_secret";

// const emailHost = process.env.EMAIL_HOST
// const emailPort = process.env.EMAIL_PORT
// const emailUser = process.env.EMAIL_USER || 'user_name_for_email';
// const emailPassword = process.env.EMAIL_PASSWORD || 'password_for_email';

// const emailConfig = {
//   emailUser,
//   emailPassword,
// }

module.exports = {
  // emailConfig,
  port,
  database_url,
  jwtKey,
  refresh_token_secret,
};
