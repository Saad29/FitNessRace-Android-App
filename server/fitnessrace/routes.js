"use strict";

const auth = require("basic-auth");
const jwt = require("jsonwebtoken");
const fs = require("fs");
const multer = require("multer");
var path = require("path");
const friends = require("./functions/friends");
const register = require("./functions/register");
const login = require("./functions/login");
const profile = require("./functions/profile");
const password = require("./functions/password");
const activity = require('./functions/activity');
const config = require("./config/config.json");

const user = require("./models/user");

module.exports = router => {
  router.get("/", (req, res) => res.end("Welcome, it's running!"));

  router.post("/authenticate", (req, res) => {
    const credentials = auth(req);

    if (!credentials) {
      res.status(400).json({ message: "Invalid Request !" });
    } else {
      login
        .loginUser(credentials.name, credentials.pass)

        .then(result => {
          const token = jwt.sign(result, config.secret, { expiresIn: 1440 });

          res
            .status(result.status)
            .json({ message: result.message, token: token });
        })

        .catch(err => res.status(err.status).json({ message: err.message }));
    }
  });

  router.post("/users", (req, res) => {
    const name = req.body.name;
    const email = req.body.email;
    const password = req.body.password;
    const age = req.body.age;
    const city = req.body.city;


    if (
      !name ||
      !email ||
      !password ||
      !city ||
      !city.trim() ||
      !name.trim() ||
      !email.trim() ||
      !password.trim() ||
      !age
    ) {
      res.status(400).json({ message: "Invalid Request !" });
    } else {
      register
        .registerUser(name, email, password, age, city)

        .then(result => {
          res.setHeader("Location", "/users/" + email);
          res.status(result.status).json({ message: result.message });
        })

        .catch(err => res.status(err.status).json({ message: err.message }));
    }
  });
  router.get("/users", (req, res) => {
    user.find({}, (err, users) => {
      if (err) throw err;
      else
        res.status(200).json({
          success: true,
          message: "get all users",
          data: users
        });
    });
  });
  router.get("/users/:id", (req, res) => {
    //if (checkToken(req)) {
    profile
      .getProfile(req.params.id)

      .then(result => res.json(result))

      .catch(err => res.status(err.status).json({ message: err.message }));
    /*} else {
      res.status(401).json({ message: "Invalid Token !" });
    }*/
  });
  // deregister a user 

  router.delete("/users/:email", (req, res) => {
    profile.deleteUser(req.params.email)
      .then(result => res.json(result))
      .catch(err => res.status(err.status).json({ message: err.message }))
  })
  //user update
  router.put("/users/update/:email", (req, res) => {
    //if (checkToken(req)) {
    const newName = req.body.name;
    const newAge = req.body.age;
    const newCity = req.body.city;
    console.log("new name ", req.body.name, "  ", req.body.age, "  ", req.body.city)
    if (
      !newName ||
      !newAge ||
      !newCity ||
      !newName.trim() ||
      !newAge.trim() ||
      !newCity.trim()
    ) {
      res.status(400).json({ message: "Invalid Request !" });
    } else {
      password
        .updateUser(req.params.email, newName, newAge, newCity)

        .then(result =>
          res.status(result.status).json({ message: result.message })
        )

        .catch(err => res.status(err.status).json({ message: err.message }));
    }
    //} //else {
    //res.status(401).json({ message: "Invalid Token !" });
    //}
  });
 //update rewards and achievements
 router.put("/users/ar/:email", (req, res) => {
  //if (checkToken(req)) {
  const rewards = req.body.rewards;
  const achievements = req.body.achievements;
  //console.log("new name ", req.body.name, "  ", req.body.age, "  ", req.body.city)
  if (
    !rewards ||
    !achievements ||
    !rewards.trim() ||
    !achievements.trim()
  ) {
    res.status(400).json({ message: "Invalid Request !" });
  } else {
    password
      .updateUsersAR(req.params.email, rewards, achievements)

      .then(result =>
        res.status(result.status).json({ message: result.message })
      )

      .catch(err => res.status(err.status).json({ message: err.message }));
  }
  //} //else {
  //res.status(401).json({ message: "Invalid Token !" });
  //}
});


  router.put("/users/:id", (req, res) => {
    if (checkToken(req)) {
      const oldPassword = req.body.password;
      const newPassword = req.body.newPassword;

      if (
        !oldPassword ||
        !newPassword ||
        !oldPassword.trim() ||
        !newPassword.trim()
      ) {
        res.status(400).json({ message: "Invalid Request !" });
      } else {
        password
          .changePassword(req.params.id, oldPassword, newPassword)

          .then(result =>
            res.status(result.status).json({ message: result.message })
          )

          .catch(err => res.status(err.status).json({ message: err.message }));
      }
    } else {
      res.status(401).json({ message: "Invalid Token !" });
    }
  });

  router.post("/users/:id/password", (req, res) => {
    const email = req.params.id;
    const token = req.body.token;
    const newPassword = req.body.password;

    if (!token || !newPassword || !token.trim() || !newPassword.trim()) {
      password
        .resetPasswordInit(email)

        .then(result =>
          res.status(result.status).json({ message: result.message })
        )

        .catch(err => res.status(err.status).json({ message: err.message }));
    } else {
      password
        .resetPasswordFinish(email, token, newPassword)

        .then(result =>
          res.status(result.status).json({ message: result.message })
        )

        .catch(err => res.status(err.status).json({ message: err.message }));
    }
  });
  //find friends
  router.get("/find_friends/:email", (req, res) => {
    const email = req.params.email;
    friends.findFriends(email)
      .then(result => {
        if (result.success == false)
          res.json({ success: result.success, message: result.message });
        else
          res.json({ status: result.status, success: result.success, message: result.message, data: result.data });

      });
  });
  // friend list
  router.get("/users/:id/friend_list", (req, res) => {
    const email = req.params.id;
    friends.friendList(email)
      .then(result => {
        if (result.success == false)
          res.json({ success: result.success, message: result.message });
        else
          res.json({ status: result.status, success: result.success, message: result.message, data: result.data });

      });
  });
  // make friend
  router.put("/users/:id/make_friend/:email", (req, res) => {
    const email = req.params.email;
    const id = req.params.id;
    friends.makeFriend(id, email)
      .then(result => {
        //if(result.success==false)
        res.json({ success: result.success, message: result.message });
        //else
        //	res.json({success: result.success, message: result.message});

      });
  });

  // get activity of a user
  router.get("/users/:email/activity", (req, res) => {
    const email = req.params.email;
    activity.getUserActivity(email)
      .then(result => {
        if (result.success == false)
          res.json({ success: result.success, message: result.message });
        else
          res.json({ status: result.status, success: result.success, message: result.message, data: result.data });

      });
  })

  // update activity of a user
  router.put("/users/updateActivity/:email", (req, res) => {
    const walkingSteps = req.body.walkingSteps;
    const walkingDistance = req.body.walkingDistance;
    const runningTime = req.body.runningTime;
    const runningDistance = req.body.runningDistance;
    const bikingTime = req.body.bikingTime;
    const bikingDistance = req.body.bikingDistance;
    const date = req.body.date;
    const loc = req.body.loc;
    if (
      !date ||
      !loc ||
      !walkingSteps ||
      !walkingDistance ||
      !runningTime ||
      !runningDistance ||
      !bikingDistance ||
      !bikingTime ||
      !date.trim() ||
      !loc.trim() ||
      !walkingSteps.trim() ||
      !walkingDistance.trim() ||
      !runningTime.trim() ||
      !runningDistance.trim() ||
      !bikingTime.trim() ||
      !bikingDistance.trim()
    ) {
      res.status(400).json({ message: "Invalid Request !" });
    } else {
      activity
        .updateUserActivity(req.params.email, date, walkingSteps, walkingDistance, runningTime, runningDistance, bikingTime, bikingDistance, loc)

        .then(result =>
          res.status(result.status).json({ message: result.message })
        )

        .catch(err => res.status(err.status).json({ message: err.message }));
    }
    //} //else {
    //res.status(401).json({ message: "Invalid Token !" });
    //}
  });

  //image
  var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      console.log(__dirname);
      cb(null, __dirname + "/upload/images/");
    },
    filename: function (req, file, cb) {
      cb(null, req.params.imagename + path.extname(file.originalname));
    }
  });

  var upload = multer({ storage: storage });
  router.post(
    "/upload/images/:imagename",
    upload.single("image"),
    (req, res) => {
      res.status(200).json({
        message: "Successfully Uploaded!",
        filename: req.file.filename
      });
    }
  );

  function checkToken(req) {
    const token = req.headers["x-access-token"];

    if (token) {
      try {
        var decoded = jwt.verify(token, config.secret);

        return decoded.message === req.params.id;
      } catch (err) {
        return false;
      }
    } else {
      return false;
    }
  }
};
