var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();
var user = require('../public/models/user');

/* GET users listing. */
router.get('/', function(req, res) {
  res.send('respond with a resource');
});

/* POST updated user data. */
router.get('/:user_id', function(req, res) { // later should change to post?
  // var location = req.body.location;
  var user_id = req.params.user_id;
  var User = user.User;

  User.findOne({user_id: user_id}, function(err, user) {
  	if (!err) {
  		if (!user) {
  			user = new User;
  			user.user_id = user_id;
  			user.password = "asdf";
  		}
  		user.location = { latitude: 123, longitude: 456 };
  		user.save( function(err) {
  			if (!err) {
  				console.log("user " + user.user_id + " is at " + user.location.latitude + ", " + user.location.longitude + ".");		
  			} else {
  				console.log("error updating user " + user.user_id + ".");
  			}
  			res.send("YO");
  		});
  	}
  });

});

module.exports = router;
