var express = require('express');
var mongoose = require('mongoose');
var bcrypt = require('bcrypt');
var router = express.Router();
var yo = require('../utils').yo;
var User = require('../public/models/user').User;
var create_password = "http://104.236.61.102:3000/users/create_password/";

// Mimic POST from mobile app for account signup
router.get('/', function(req, res) {
	res.render('users');
});

// sign up
router.post('/', function(req, res) {
	console.log(req);
	var user_id = req.body.user_id;
	// may have to make user_id uppercase, maybe not

	if (user_id) {
		yo.yo_link(user_id, create_password + user_id, function(err, yo_res) {
			if (!err) {
				// user exists
				res.setHeader('Content-Type', 'application/json');
				res.status(200);
				res.write(JSON.stringify({response: "0"}));
			} else {
				// user does not exist
				res.send("USER NEEDS TO CREATE A YO ACCOUNT WITH THIS NAME");
			}
		});
	} else {
		res.send("USER ID IS NULL");
	}
});

router.get('/create_password/:user_id', function(req, res) {
	res.render('create_password', { user_id: req.params.user_id });
});

router.post('/create_password', function(req, res) {
	console.log(req);
	User.findOne({user_id: req.body.user_id}, function(err, user) {
		if (!user) {
			user = new User;
		}
		var salt = bcrypt.genSaltSync(27181828);
		user.user_id = req.body.user_id;
		user.password = bcrypt.hashSync(req.body.password, salt);
		user.salt = salt;
		user.save( function(err) {
			if (!err) {
				res.status(200);
				res.send("SAVE SUCCESSFUL");
			} else {
				res.send(err);
			}
			res.end();
		});
	});
});

// for testing purposes only
router.get('/login', function(req, res) {
	res.render('login');
});

router.post('/login', function(req, res) {
	console.log(req);
	var user_id = req.body.user_id;
	var password = req.body.password;
	User.findOne({user_id: user_id }, function(err, user) {
		if (!err) {
			if (!user) {
				res.write('User not found.');
			} else {
				res.setHeader('Content-Type', 'application/json');
				res.status(200);
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
