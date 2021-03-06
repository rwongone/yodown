var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();
var User = require('../public/models/user').User;
var Place = require('../public/models/Place').Place;

/* GET users listing. */
router.get('/', function(req, res) {
	res.render('places');
});

/* POST updated user data. */
router.post('/', function(req, res) {
	var latlong = { latitude: req.body.latitude, longitude: req.body.longitude};
	var user_id = req.body.user_id;
	console.log("Get a post request from " + user_id + " at " +latlong.latitude + " and "+ latlong.longitude);
	User.findOne({user_id: user_id}, function(err, user) {
		if (!err) {
			if (!user) {
				res.write("User Not Found.");
				res.end();
			} else {
				user.location = latlong;
				user.lastTimeActive = Date.now();
				user.save( function(err) {
					if (!err) {
						res.status(200);
						// console.log("user " + user.user_id + " is at " + user.location.latitude + ", " + user.location.longitude + ".");
					} else {
						console.log("ERROR updating user " + user.user_id + ".");
					}

					var place = new Place;
					place.user_id = user_id;
					place.place = latlong;
					place.timestamp = Date.now();
					place.save( function(err) {
						if (!err) {
							console.log("user " + place.user_id + " is at " + place.place.latitude + ", " + place.place.longitude + ".");
						} else {
							console.log("error updating place " + place.user_id + "." + err);
						}
					});

					var range = 1000;
					var a = Number(latlong.latitude)-Number(range);
					var b = Number(latlong.latitude)+Number(range);
					var c = Number(latlong.longitude)-Number(range);
					var d = Number(latlong.longitude)+Number(range);
					console.log(a+""+b+""+c+""+d+"");
					console.log("^range");
					/*********************Start Fetching Nearby Users**********************/
					User.find({
						'location.latitude': { $gte: a},
						'location.latitude': { $lte: b},
						'location.longitude': { $gte: c},
						'location.longitude': { $lte: d},
						user_id: {$ne: user_id},
						'dead':  false
						}, function (err, users) {
							console.log("users:");
							console.log(users);
							res.setHeader('Content-Type', 'application/json');
					  	res.write(JSON.stringify(users));
					  	res.end();
					});
					/*********************Fetching Nearby Users Ends**********************/
				});
			}
		}
	});
});

// for testing purposes only
router.get('/places', function(req, res) {
	res.render('places');
});

router.get('/nearby', function(req, res) {
	var userList = {};

});

module.exports = router;
