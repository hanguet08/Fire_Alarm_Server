//During the test the env variable is set to test
process.env.NODE_ENV = "test";
require("./room.test");
//Require the dev-dependencies
let chai = require("chai");
let chaiHttp = require("chai-http");
let server = require("../server");
let should = chai.should();
const Device = require("../models/device.model");
chai.use(chaiHttp);

//Our parent block
describe("Device route", () => {
  before((done) => {
    //Before all test we empty the database in your case
    let devices = [
      {
        deviceName: "Cảm biến nhiệt độ",
        position: "Trên trần nhà",
        deviceType: 3,
        status: "ON",
        roomId: global.roomId,
      },
      {
        deviceName: "Cảm biến khói",
        position: "Cạnh bàn học",
        deviceType: 1,
        status: "ON",
        roomId: global.roomId,
      },
    ];
    Device.deleteMany({}, (err) => {
      if (err) done(err);
    });

    Device.insertMany(devices, (err) => {
      if (err) done(err);
      else done();
    });
  });

  /*
   * Test the /GET method
   */
  describe("/GET", () => {
    it("/api/v1/devices", (done) => {
      chai
        .request(server)
        .get("/api/v1/devices")
        .query({
          roomId: global.roomId,
        })
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("array");
          res.body.data.length.should.be.eql(2); // maybe change
          done();
          global.deviceId = res.body.data[0]._id;
        });
    });

    it("/api/v1/devices/:id", (done) => {
      let id = global.deviceId;
      chai
        .request(server)
        .get(`/api/v1/devices/${id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id").eql(id);
          res.body.data.should.have.property("deviceName");
          res.body.data.should.have.property("deviceType");
          res.body.data.should.have.property("position");
          res.body.data.should.have.property("roomId");
          res.body.data.should.have.property("status");
          done();
        });
    });
  });

  /*
   * Test the /POST method
   */
  describe("/POST", () => {
    it("/api/v1/devices", (done) => {
      let deviceCreate = {
        deviceName: "Cảm biến khí ga",
        position: "Cạnh tủ lạnh",
        deviceType: 2,
        status: "ON",
        roomId: global.roomId,
      };
      chai
        .request(server)
        .post("/api/v1/devices")
        .send(deviceCreate)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("message").eql("Insert device success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id");
          res.body.data.should.have
            .property("deviceName")
            .eql(deviceCreate.deviceName);
          res.body.data.should.have
            .property("deviceType")
            .eql(deviceCreate.deviceType);
          res.body.data.should.have.property("status").eql(deviceCreate.status);
          res.body.data.should.have
            .property("position")
            .eql(deviceCreate.position);
          res.body.data.should.have.property("roomId").eql(global.roomId);
          done();
          global.device_add_edit_delete_Id = res.body.data._id;
        });
    });
  });

  /*
   * Test the /PUT method
   */
  describe("/PUT", () => {
    it("/api/v1/devices/:id", (done) => {
      let deviceEdit = {
        deviceName: "Cảm biến khí ga 1",
        position: "Cạnh tủ lạnh",
        deviceType: 2,
        status: "OFF",
      };
      chai
        .request(server)
        .put(`/api/v1/devices/${global.device_add_edit_delete_Id}`)
        .send(deviceEdit)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Device updated successfully");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id");
          res.body.data.should.have
            .property("deviceName")
            .eql(deviceEdit.deviceName);
          res.body.data.should.have
            .property("position")
            .eql(deviceEdit.position);
          res.body.data.should.have
            .property("deviceType")
            .eql(deviceEdit.deviceType);
          res.body.data.should.have.property("status").eql(deviceEdit.status);
          res.body.data.should.have.property("roomId").eql(global.roomId);
          done();
        });
    });
  });

  /*
   * Test the /DELETE method
   */
  describe("/DELETE", () => {
    it("/api/v1/devices/:id", (done) => {
      chai
        .request(server)
        .delete(`/api/v1/devices/${global.device_add_edit_delete_Id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Device deleted successfully");
          done();
        });
    });
  });
});
