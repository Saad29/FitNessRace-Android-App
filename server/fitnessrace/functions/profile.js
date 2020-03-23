"use strict";

const user = require("../models/user");

exports.getProfile = email =>
  new Promise((resolve, reject) => {
    user
      .find(
        { email: email },
        { name: 1, email: 1, friend_list: 1, created_at: 1, age: 1, city: 1, rewards: 1, achievements: 1, activity: 1, _id: 0 }
      )

      .then(users => resolve(users[0]))

      .catch(err =>
        reject({ status: 500, message: "Internal Server Error !" })
      );
  });

exports.deleteUser = email =>
  new Promise((resolve, reject) => {
    user.remove({ email: email }).then(user => resolve({
      status: 200,
      success: true,
      message: "user deleted successfully"
    }))
      .catch(err => reject({ success: false, message: "User not deleted" }))
  })