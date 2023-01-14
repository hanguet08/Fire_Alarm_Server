if (process.env.NODE_ENV !== "production") {
  require("dotenv").config(require("./config/dotenv"));
}
const cors = require("cors");
const express = require("express");
// swagger config
const swaggerUi = require("swagger-ui-express");
const swaggerDocument = require("./config/swagger.config.json");
const app = express();

const db = require("./db/mongoose");
const route = require("./routes");
const { port } = require("./config");
const handleExceptions = require("./middlewares/handle.exception.middleware");
const { morganChalk } = require("./config/morgan");

// Parse body req to json
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
// Connect to mongodb database
db.connect();

// Enable cors
app.use(cors());

// Use MQTT services
require("./services/mqtt.service");

// Use morgan
if (process.env.NODE_ENV !== "test") {
  app.use(morganChalk);
}

// Route middleware
route(app);

// Handle exception
handleExceptions(app);

// setting up swagger
// https://fire-alarm12.herokuapp.com || http://localhost:5000
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

//Start an express server
app.listen(port, () => console.log(`Server Started http://localhost:${port}`));

module.exports = app;
