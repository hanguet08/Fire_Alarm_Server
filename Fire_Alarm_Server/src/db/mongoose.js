const mongoose = require("mongoose");
const { database_url } = require("../config");
const console = require("../utils/chalk");

async function connect() {
  try {
    await mongoose
      .connect("mongodb+srv://hanguet08:hanguet08@cluster0.8iws48a.mongodb.net/fire_alarm?retryWrites=true&w=majority", {
        useUnifiedTopology: true,
        useNewUrlParser: true,
      })
      .catch((error) => {
        console.log(error.message);
      });

    mongoose.connection.on("error", (error) => {
      console.log("MongoDB connection error");
      console.log(JSON.stringify(error));
    });

    mongoose.connection.once("open", () => {
      console.log("MongoDB connection connect successfully");
    });
  } catch (error) {
    console.error("Connect database failure");
  }
}

module.exports = { connect };
