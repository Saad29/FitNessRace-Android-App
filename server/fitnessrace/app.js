"use strict";

const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const logger = require("morgan");
const router = express.Router();

const fileType = require("file-type");
const fs = require("fs");

const port = process.env.PORT || 8080;

//app.use('/', router)
//
// app.use((err, req, res, next) => {
//
//     if (err.code == 'ENOENT') {
//
//         res.status(404).json({message: 'Image Not Found !'})
//
//     } else {
//
//         res.status(500).json({message:err.message})
//     }
// })
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(logger("dev"));
app.use(express.static(__dirname + "/upload"));
require("./routes")(router);
app.use("/", router);

app.listen(port);

console.log(`App is Running on ${port}`);
