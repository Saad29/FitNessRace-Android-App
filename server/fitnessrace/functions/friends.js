const user = require('../models/user');

exports.findFriends = (email) => 

	new Promise((resolve, reject) => {
user.findOne(
      {
        email: email
      },
      function(err, user) {
        if (err) throw err;

        if (!user) {
          resolve({ success: false, message: "No User found." });
        } else if (user) {
          resolve({status:200, 
            success: true,
            message: "get user by email",
            data: user
          });
        }
      }
    );
	});

exports.friendList = (email) => 

  new Promise((resolve, reject) => {
user.findOne(
      {
        email: email
      },
      function(err, user) {
        if (err) throw err;

        if (!user) {
          resolve({ success: false, message: "No User found." });
        } else if (user) {
          resolve({status:200, 
            success: true,
            message: "get user by email",
            data: user.friend_list
          });
        }
      }
    );
  });

  exports.makeFriend = (id, email) => 

  new Promise((resolve, reject) => {
user.findOne(
      {
        email: id
      },
      (err, result) => {
        if (err) throw err;
        else {
          user.findOne(
            {
              email: email
            },
      function(err, user) {
        if (err) throw err;

        if (!user) {
          resolve({ success: false, message: "No User found." });
        } else if (user) {

          var list = result.friend_list;
                list.push({
                  name: user.name,
                  email: user.email
                });
                result.friend_list = list;
                result.save();
               
          resolve({ 
            success: true,
            message: "Friend Added"
          });
        }
      }
    );
        }
  })
});