var express = require('express');
var mongoose = require('mongoose');
var bcrypt = require('bcrypt');
var router = express.Router();
var User = require('../public/models/user').User;
var salt = bcrypt.genSaltSync(27181828);

/* GET users listing. */
router.get('/', function(req, res) {
	res.render('users');
});

/* POST updated user data. */
router.post('/', function(req, res) { // later should change to post?
	var location = req.body.location;
	var user_id = req.body.user_id;
	

	User.findOne({user_id: user_id}, function(err, user) {
		if (!err) {
			if (!user) {
				user = new User;
				user.user_id = user_id;
				user.password = bcrypt.hashSync(req.body.password, salt);
				user.salt = salt;
			} else {
				res.send("User already exists.");
			}
			user.location = req.body.location;
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

// for testing purposes only
router.get('/login', function(req, res) {
	res.render('login');
});

router.post('/login', function(req, res) {
	var user_id = req.body.user_id;
	var password = req.body.password;
	User.findOne({user_id: user_id }, function(err, user) {
		if (!err) {
			if (!user) {
				res.write('User not found.');
			} else {
				res.setHeader('Content-Type', 'application/json');
				var salt = user.salt;
				if (bcrypt.hashSync(password, salt) === user.password) {
					res.write(JSON.stringify(user));
					console.log(JSON.stringify(user));
				} else {
					res.write('Incorrect password.');
				}
			}
		} else {
			res.write("ERROR or something");
		}
		res.end();
	});
});

router.get('/nearby', function(req, res) {
	var userList = {};

});

module.exports = router;
