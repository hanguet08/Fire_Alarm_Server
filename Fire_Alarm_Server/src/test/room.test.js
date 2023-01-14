//During the test the env variable is set to test
process.env.NODE_ENV = "test";
require("./house.test");
//Require the dev-dependencies
let chai = require("chai");
let chaiHttp = require("chai-http");
let server = require("../server");
let should = chai.should();
const Room = require("../models/room.model");
chai.use(chaiHttp);

//Our parent block
describe("Room route", () => {
  before((done) => {
    //Before all test we empty the database in your case
    let rooms = [
      {
        roomName: "P.101",
        position: "Phòng 101",
        owner: "Nguyễn Thị H",
        area: 25,
        houseId: global.houseId,
      },
      {
        roomName: "P.203",
        position: "Phòng 203",
        owner: "Trần Văn A",
        area: 25,
        houseId: global.houseId,
      },
    ];
    Room.deleteMany({}, (err) => {
      if (err) done(err);
    });

    Room.insertMany(rooms, (err) => {
      if (err) done(err);
      else done();
    });
  });

  /*
   * Test the /GET method
   */
  describe("/GET", () => {
    it("/api/v1/rooms", (done) => {
      chai
        .request(server)
        .get("/api/v1/rooms")
        .query({
          houseId: global.houseId,
        })
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("array");
          res.body.data.length.should.be.eql(2); // maybe change
          done();
          global.roomId = res.body.data[0]._id;
        });
    });

    it("/api/v1/rooms/:id", (done) => {
      let id = global.roomId;
      chai
        .request(server)
        .get(`/api/v1/rooms/${id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id").eql(id);
          res.body.data.should.have.property("roomName");
          res.body.data.should.have.property("position");
          res.body.data.should.have.property("owner");
          res.body.data.should.have.property("houseId");
          res.body.data.should.have.property("area");
          done();
        });
    });
  });

  /*
   * Test the /POST method
   */
  describe("/POST", () => {
    it("/api/v1/rooms", (done) => {
      let roomCreate = {
        roomName: "P.105",
        position: "Phòng 105",
        owner: "Nguyễn Thị N",
        area: 24,
        houseId: global.houseId,
      };
      chai
        .request(server)
        .post("/api/v1/rooms")
        .send(roomCreate)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("message").eql("Insert room success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id");
          res.body.data.should.have
            .property("roomName")
            .eql(roomCreate.roomName);
          res.body.data.should.have
            .property("position")
            .eql(roomCreate.position);
          res.body.data.should.have.property("owner").eql(roomCreate.owner);
          res.body.data.should.have.property("area").eql(roomCreate.area);
          res.body.data.should.have.property("houseId").eql(global.houseId);
          done();
          global.room_add_edit_delete_Id = res.body.data._id;
        });
    });
  });

  /*
   * Test the /PUT method
   */
  describe("/PUT", () => {
    it("/api/v1/rooms/:id", (done) => {
      let roomEdit = {
        roomName: "P.108",
        position: "Phòng 108",
        owner: "Nguyễn Thị T",
        area: 21,
      };
      chai
        .request(server)
        .put(`/api/v1/rooms/${global.room_add_edit_delete_Id}`)
        .send(roomEdit)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Room updated successfully");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have
            .property("_id")
            .eql(global.room_add_edit_delete_Id);
          res.body.data.should.have.property("roomName").eql(roomEdit.roomName);
          res.body.data.should.have.property("position").eql(roomEdit.position);
          res.body.data.should.have.property("owner").eql(roomEdit.owner);
          res.body.data.should.have.property("area").eql(roomEdit.area);
          res.body.data.should.have.property("houseId").eql(global.houseId);
          done();
        });
    });
  });

  /*
   * Test the /DELETE method
   */
  describe("/DELETE", () => {
    it("/api/v1/rooms/:id", (done) => {
      chai
        .request(server)
        .delete(`/api/v1/rooms/${global.room_add_edit_delete_Id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Room deleted successfully");
          done();
        });
    });
  });
});
