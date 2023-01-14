//During the test the env variable is set to test
process.env.NODE_ENV = "test";
require("./auth.test");
//Require the dev-dependencies
let chai = require("chai");
let chaiHttp = require("chai-http");
let server = require("../server");
let should = chai.should();
const User = require("../models/user.model");
chai.use(chaiHttp);

//Our parent block
describe("User route", () => {
  before((done) => {
    //Before all test we empty the database in your case
    done();
  });

  /*
   * Test the /GET method
   */
  describe("/GET", () => {
    it("/api/v1/users", (done) => {
      chai
        .request(server)
        .get("/api/v1/users/info")
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id").eql(global.userId);
          res.body.data.should.have.property("email").eql(global.email);
          res.body.data.should.have.property("fullName");
          res.body.data.should.have.property("address");
          res.body.data.should.have.property("refreshToken");
          res.body.data.should.have.property("fcmToken");
          res.body.data.should.have.property("roles").which.is.an("array");
          done();
        });
    });
  });

  /*
   * Test the /POST method
   */
  describe("/POST", () => {
    it("/api/v1/users/info", (done) => {
      let userEdit = {
        fullName: "Nguyễn Văn T",
        address: "Hai Bà Trưng, Hà Nội",
        phoneNumber: "0888666999",
      };
      chai
        .request(server)
        .post("/api/v1/users/info")
        .send(userEdit)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Update user successfully");
          done();
        });
    });
    it("/api/v1/users/change-password", (done) => {
      let body = {
        oldPassword: "1234567",
        newPassword: "123456",
      };
      chai
        .request(server)
        .post("/api/v1/users/change-password")
        .send(body)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Change password successfully!");
          done();
        });
    });

    it("/api/v1/users/tokenFCM", (done) => {
      let body = {
        fcmToken:
          "coJH-MckTEq7tkhCtDGkyg:APA91bH0pnoIvq58x-a0wFU2sQrMpeY6XDUOzH_otuK34Qb31l0VMCv3t8FNs6XDWsDQsGNKPEMKlooRVwaC4czNOoE85zi0RSnMLkBIJTWQijXk_B317NH-kodchknseNMDGvAiGIkE",
      };
      chai
        .request(server)
        .post("/api/v1/users/tokenFCM")
        .send(body)
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have
            .property("message")
            .eql("Update token FCM successfully!");
          done();
        });
    });
  });
});
