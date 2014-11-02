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
	//var place = null;
	//var place = req.body.location;
	var latlong = { latitude: req.body.latitude, longitude: req.body.longitude};
	var user_id = req.body.user_id;
	res.setHeader('Content-Type', 'application/json');
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
						console.log("user " + user.user_id + " is at " + user.location.latitude + ", " + user.location.longitude + ".");		
					} else {
						console.log("ERROR updating user " + user.user_id + ".");
					}
					console.log("YO, User updated.");

					Place.findOne({user_id: user_id, timestamp: Date.now()}, function(err, place) {
						if (!err) {
							if (!place) {
								place = new Place;
								place.user_id = user_id;
								place.place = latlong;
								place.timestamp = Date.now();
								place.save( function(err) {
									if (!err) {
										console.log("user " + place.user_id + " is at " + place.place.latitude + ", " + place.place.longitude + ".");		

									} else {
										console.log("error updating place " + place.user_id + "." + err);
									}
									res.write("YO, Places updated.");
								});


								/*********************Start Fecthing Nearby Users**********************/
								User.find(function (err, userList) {
								  // if (err) return console.error(err);
								  // console.log(userList)
								  userList.forEach(function(userList){
								  	if (Math.abs(userList.location.latitude - latlong.latitude < 10) && Math.abs(userList.location.longitude - latlong.longitude < 10) && (Date.now()-userList.lastTimeActive)< 600000 && user_id!= userList.user_id){
								  		res.write(JSON.stringify(userList));
								  		console.log("HERE!");
								  		console.log(userList);
								  	}
								  });
								});


								/*********************Fecthing Nearby Users Ends**********************/

								res.write(JSON.stringify(place));
								console.log(JSON.stringify(place));
								res.end();
							} else {
								res.write("Error: multiple post requests.");
							}
							
						}else{
							res.write("ERROR");
						}
					});
				});
				//res.end();
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
