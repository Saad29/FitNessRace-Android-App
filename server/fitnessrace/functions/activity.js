'use strict';

const user = require('../models/user');
const config = require('../config/config.json');

exports.getUserActivity = (email) =>
    new Promise((resolve, reject) => {
        user.findOne(
            {
                email: email
            },
            function (err, user) {
                if (err) throw err;

                if (!user) {
                    resolve({ success: false, message: "No User found." });
                } else if (user) {
                    resolve({
                        status: 200,
                        success: true,
                        message: "get user activity by email",
                        data: user.activity
                    });
                }
            }
        );
    });

exports.updateUserActivity = (email, dat, walkingSteps, walkingKm, runningHr, runningKm, bikingHr, bikingKm,loc) =>

    new Promise((resolve, reject) => {

        user.find({ email: email })

            .then(users => {

                let user = users[0];
                let date = 0;
                let walking = {};
                let running = {};
                let biking = {};
                let location = {};
                date= dat;
                walking.steps = walkingSteps;
                walking.distance = walkingKm;
                running.time = runningHr;
                running.distance = runningKm;
                biking.time = bikingHr;
                biking.diistance = bikingKm;
                location = loc;

                let activity = {
                    date : dat,
                    walking: walking,
                    running: running,
                    biking: biking,
                    location: loc
                };
                user.activity.push(activity);
                return user.save();

            })

            .then(user => resolve({ status: 200, message: 'User Activity Data Updated Sucessfully !' }))

            .catch(err => reject({ status: 500, message: 'Internal Server Error !'+err }));

    });