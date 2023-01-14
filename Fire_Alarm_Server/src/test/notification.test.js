//During the test the env variable is set to test
process.env.NODE_ENV = "test";
require("./device.test");
//Require the dev-dependencies
let chai = require("chai");
let chaiHttp = require("chai-http");
let server = require("../server");
let should = chai.should();
const Notification = require("../models/notification.model");
chai.use(chaiHttp);

//Our parent block
describe("Notification route", () => {
  before((done) => {
    //Before all test we empty the database in your case
    let notifications = [
      {
        device: "Cảm biến khí ga",
        userId: global.userId,
        statusSeen: "NO",
        content: "Cảnh báo khí ga tại tầng 1 phòng 102, nhà D1",
        dateTime: "Wednesday, September 14th 2022, 4:35:05",
      },
      {
        device: "Cảm biến nhiệt độ",
        userId: global.userId,
        statusSeen: "NO",
        content: "Phát hiện lửa tại Tầng 4 (phòng ăn), nhà ăn D10",
        dateTime: "Friday, September 16th 2022, 0:17:37",
      },
    ];
    Notification.deleteMany({}, (err) => {
      if (err) done(err);
    });

    Notification.insertMany(notifications, (err) => {
      if (err) done(err);
      else done();
    });
  });

  /*
   * Test the /GET method
   */
  describe("/GET", () => {
    it("/api/v1/notifications", (done) => {
      chai
        .request(server)
        .get("/api/v1/notifications")
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("array");
          res.body.data.length.should.be.eql(2); // maybe change
          done();
          global.notificationId = res.body.data[0]._id;
        });
    });
    it("/api/v1/notifications/:id", (done) => {
      let id = global.notificationId;
      chai
        .request(server)
        .get(`/api/v1/notifications/${id}`)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id").eql(id);
          res.body.data.should.have.property("device");
          res.body.data.should.have.property("statusSeen");
          res.body.data.should.have.property("content");
          res.body.data.should.have.property("dateTime");
          res.body.data.should.have.property("userId");
          done();
        });
    });
  });

  /*
   * Test the /POST method
   */
  describe("/POST", () => {
    it("/api/v1/notifications", (done) => {
      let notificationCreate = {
        device: "Cảm biến khí ga",
        statusSeen: "NO",
        content: "Cảnh báo khí ga tại tầng 1 phòng 103, nhà D1",
      };
      chai
        .request(server)
        .post("/api/v1/notifications")
        .send(notificationCreate)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Insert notification success!");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id");
          res.body.data.should.have
            .property("device")
            .eql(notificationCreate.device);
          res.body.data.should.have
            .property("statusSeen")
            .eql(notificationCreate.statusSeen);
          res.body.data.should.have
            .property("content")
            .eql(notificationCreate.content);
          res.body.data.should.have.property("dateTime");
          res.body.data.should.have.property("userId").eql(global.userId);
          done();
          global.notification_add_edit_delete_Id = res.body.data._id;
        });
    });
  });

  /*
   * Test the /PUT method
   */
  describe("/PUT", () => {
    it("/api/v1/notifications/:id", (done) => {
      let notificationEdit = {
        statusSeen: "YES",
        content: "Cảnh báo khí ga tại tầng 1 phòng 103, nhà D1",
      };
      chai
        .request(server)
        .put(`/api/v1/notifications/${global.notification_add_edit_delete_Id}`)
        .send(notificationEdit)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Edit success this notification");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have
            .property("_id")
            .eql(global.notification_add_edit_delete_Id);
          res.body.data.should.have.property("device");
          res.body.data.should.have
            .property("statusSeen")
            .eql(notificationEdit.statusSeen);
          res.body.data.should.have
            .property("content")
            .eql(notificationEdit.content);
          res.body.data.should.have.property("dateTime");
          res.body.data.should.have.property("userId").eql(global.userId);
          done();
        });
    });
  });

  /*
   * Test the /DELETE method
   */
  describe("/DELETE", () => {
    it("/api/v1/notifications/:id", (done) => {
      chai
        .request(server)
        .delete(
          `/api/v1/notifications/${global.notification_add_edit_delete_Id}`
        )
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Notification deleted successfully");
          done();
        });
    });
  });
});
