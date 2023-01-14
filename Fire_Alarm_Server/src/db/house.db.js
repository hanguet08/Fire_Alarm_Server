const House = require("../models/house.model");
const console = require("../utils/chalk");

// Get all houses
const getAllHouseDb = async (query) => {
  try {
    const houses = await House.find(query);
    return houses;
  } catch (error) {
    console.error("getAllHouseDb: " + error);
  }
};

// Get one house
const getHouseDb = async (query) => {
  try {
    const house = await House.findOne(query);
    return house;
  } catch (error) {
    console.error("getHouseDb: " + error);
  }
};

// Create house
const createHouseDb = async (query) => {
  try {
    const house = await new House(query).save();
    return house;
  } catch (error) {
    console.error("createHouseDb: " + error);
  }
};

// Update house
const editHouseDb = async (query) => {
  try {
    const { houseName, address, floor, members, area, houseForEdit } = query;

    houseForEdit.houseName = houseName;
    houseForEdit.address = address;
    houseForEdit.floor = floor;
    houseForEdit.members = members;
    houseForEdit.area = area;

    const rs = await houseForEdit.save();
    return rs;
  } catch (error) {
    console.error("editHouseDb: " + error);
  }
};

// Delete one house
const deleteHouseDb = async (query) => {
  try {
    const rs = await House(query).delete();
    return rs;
  } catch (error) {
    console.error("deleteHouseDb: " + error);
  }
};

module.exports = {
  getAllHouseDb,
  getHouseDb,
  createHouseDb,
  editHouseDb,
  deleteHouseDb,
};
