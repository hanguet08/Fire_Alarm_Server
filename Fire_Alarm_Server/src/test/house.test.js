//During the test the env variable is set to test
process.env.NODE_ENV = "test";
require("./auth.test");
require("./user.test");
//Require the dev-dependencies
let chai = require("chai");
let chaiHttp = require("chai-http");
let server = require("../server");
let should = chai.should();
const House = require("../models/house.model");
chai.use(chaiHttp);

//Our parent block
describe("House route", () => {
  before((done) => {
    //Before all test we empty the database in your case
    let houses = [
      {
        houseName: "Nhà A1",
        address: "Hai Bà Trưng, Hà Nội",
        floor: 3,
        members: 4,
        area: 70,
        userId: global.userId,
      },
      {
        houseName: "Nhà C2",
        address: "Đống Đa, Hà Nội",
        floor: 7,
        members: 6,
        area: 56,
        userId: global.userId,
      },
    ];
    House.deleteMany({}, (err) => {
      if (err) done(err);
    });

    House.insertMany(houses, (err) => {
      if (err) done(err);
      else done();
    });
  });

  /*
   * Test the /GET method
   */
  describe("/GET", () => {
    it("/api/v1/houses", (done) => {
      chai
        .request(server)
        .get("/api/v1/houses")
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("array");
          res.body.data.length.should.be.eql(2); // maybe change
          done();
          global.houseId = res.body.data[0]._id;
        });
    });
    it("/api/v1/houses/:id", (done) => {
      let id = global.houseId;
      chai
        .request(server)
        .get(`/api/v1/houses/${id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id").eql(id);
          res.body.data.should.have.property("houseName");
          res.body.data.should.have.property("address");
          res.body.data.should.have.property("floor");
          res.body.data.should.have.property("members");
          res.body.data.should.have.property("userId");
          res.body.data.should.have.property("area");
          done();
        });
    });
  });

  /*
   * Test the /POST method
   */
  describe("/POST", () => {
    it("/api/v1/houses", (done) => {
      let houseCreate = {
        houseName: "Nhà D",
        address: "Hai Bà Trưng, Hà Nội",
        floor: 3,
        members: 4,
        area: 72,
      };
      chai
        .request(server)
        .post("/api/v1/houses")
        .send(houseCreate)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("message").eql("Insert house success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id");
          res.body.data.should.have
            .property("houseName")
            .eql(houseCreate.houseName);
          res.body.data.should.have
            .property("address")
            .eql(houseCreate.address);
          res.body.data.should.have.property("floor").eql(houseCreate.floor);
          res.body.data.should.have
            .property("members")
            .eql(houseCreate.members);
          res.body.data.should.have.property("area").eql(houseCreate.area);
          res.body.data.should.have.property("userId").eql(global.userId);
          done();
          global.house_add_edit_delete_Id = res.body.data._id;
        });
    });
  });

  /*
   * Test the /PUT method
   */
  describe("/PUT", () => {
    it("/api/v1/houses/:id", (done) => {
      let houseEdit = {
        houseName: "Nhà A2",
        address: "Hai Bà Trưng, Hà Nội",
        floor: 4,
        members: 5,
        area: 75,
      };
      chai
        .request(server)
        .put(`/api/v1/houses/${global.house_add_edit_delete_Id}`)
        .send(houseEdit)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("House updated successfully");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have
            .property("_id")
            .eql(global.house_add_edit_delete_Id);
          res.body.data.should.have
            .property("houseName")
            .eql(houseEdit.houseName);
          res.body.data.should.have.property("address").eql(houseEdit.address);
          res.body.data.should.have.property("floor").eql(houseEdit.floor);
          res.body.data.should.have.property("members").eql(houseEdit.members);
          res.body.data.should.have.property("area").eql(houseEdit.area);
          res.body.data.should.have.property("userId").eql(global.userId);
          done();
        });
    });
  });

  /*
   * Test the /DELETE method
   */
  describe("/DELETE", () => {
    it("/api/v1/houses/:id", (done) => {
      chai
        .request(server)
        .delete(`/api/v1/houses/${global.house_add_edit_delete_Id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("House deleted successfully");
          done();
        });
    });
  });
});
