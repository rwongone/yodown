var express = require('express');
var router = express.Router();
var User = require('../public/models/user').User;

var yo = (function() {
  var Yo = require('yo-api');
  return new Yo(process.env.YO_API_TOKEN);
})();

/* GET home page. */
router.get('/', function(req, res) {
	res.render('yo');
});

// use unique object id for identification
router.post('/', function(req, res) {
	var sender_id = req.body._id;
	User.findOne({_id: sender_id }, function(err, user) {
		if (!err && user) {
			var user_id = user.user_id;
			console.log(user);
			User.findOne({ user_id: user_id }, function(err, user) {
				if (!err) {
					if (!user) {
						res.send("USER DOES NOT EXIST");
					} else {
						// yo the user, at this point the user must exist
						console.log("hello");
						yo.yo(user_id, function(err, yo_res) {
							if (!err) {
								res.send(user_id + " has been YO'd.");
							} else {
								res.send("ERROR IN YO'ING USER");
							}
						});
					}
				} else {
					res.send("ERROR IN RETRIEVING USER");
				}
			});
		} else {
			res.send("AUTHENTICATION ERROR");
			if (user) {
				res.send("HUH");
			}
		}
	});

});

module.exports = router;
