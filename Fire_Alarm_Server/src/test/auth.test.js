//During the test the env variable is set to test
process.env.NODE_ENV = "test";
//Require the dev-dependencies
let chai = require("chai");
let chaiHttp = require("chai-http");
let server = require("../server");
let should = chai.should();
const User = require("../models/user.model");
chai.use(chaiHttp);

//Our parent block
describe("Authentication route", () => {
  before((done) => {
    //Before all test we empty the database in your case
    global.email = "test@gmail.com";
    global.password = "1234567";
    User.deleteMany({}, (err) => {
      if (err) done(err);
      else done();
    });
  });
  /*
   * Test the /POST route
   */
  describe("/POST", () => {
    it("/auth/user/register", (done) => {
      let registerAccount = {
        email: global.email,
        password: global.password,
        fullName: "Nguyễn Văn Admin",
        address: "HN",
        phoneNumber: "0383888666",
      };
      chai
        .request(server)
        .post("/auth/user/register")
        .send(registerAccount)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have
            .property("message")
            .eql("Register successfully!");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("_id");
          res.body.data.should.have
            .property("email")
            .eql(registerAccount.email);
          res.body.data.should.have.property("fullName");
          res.body.data.should.have.property("address");
          res.body.data.should.have.property("refreshToken");
          res.body.data.should.have.property("fcmToken");
          res.body.data.should.have.property("roles").which.is.an("array");
          done();
          global.userId = res.body.data._id;
        });
    });
    it("/auth/user/login", (done) => {
      let login = {
        email: global.email,
        password: global.password,
      };
      chai
        .request(server)
        .post("/auth/user/login")
        .send(login)
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("message").eql("Login successfully!");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("refreshToken");
          res.body.data.should.have.property("accessToken");
          res.body.data.should.have.property("userId");
          res.body.data.should.have.property("fcmToken");
          done();
          global.refreshToken = res.body.data.refreshToken;
          global.accessToken = res.body.data.accessToken;
        });
    });
    it("/auth/refresh-token", (done) => {
      chai
        .request(server)
        .post("/auth/refresh-token")
        .send({ refreshToken: global.refreshToken })
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("status").eql("success");
          res.body.should.have.property("data").which.is.an("object");
          res.body.data.should.have.property("accessToken");
          done();
          global.accessToken = res.body.data.accessToken;
        });
    });
  });

  describe("/POST", () => {
    it("/auth/user/logout", (done) => {
      chai
        .request(server)
        .post("/auth/user/logout")
        .auth(global.accessToken, { type: "bearer" })
        .end((err, res) => {
          res.should.have.status(200);
          res.body.should.be.a("object");
          res.body.should.have.property("message").eql("Logout successfully!");
          res.body.should.have.property("status").eql("success");
          done();
        });
    });
  });
});
