"use strict";

const mongoose = require("mongoose");

const Schema = mongoose.Schema;

const userSchema = mongoose.Schema({
  name: String,
  email: { type: String, unique: true },
  hashed_password: String,
  created_at: String,
  temp_password: String,
  temp_password_time: String,
  friend_list: Array,
  age: Number,
  city: String,
  rewards: Number,
  achievements: Number,
  activity: Array

});

mongoose.Promise = global.Promise;
mongoose.connect(
  "mongodb://irfan.sebd:iptk2017@ds135956.mlab.com:35956/quebec",
  { useMongoClient: true }
);

module.exports = mongoose.model("user", userSchema);
