var express = require('express');
var router = express.Router();
var User = require('../public/models/user').User;

var yo = require('../utils').yo;

/* GET home page. */
router.get('/', function(req, res) {
	res.render('yo');
});

// use unique object id for identification
// we get sender_id and recipient_id
router.post('/', function(req, res) {
	var sender_id = req.body.sender_id;
	User.findOne({ user_id: sender_id }, function(err, sender) {
		if (!err && sender) {
			User.findOne({ user_id: req.body.recipient_id }, function(err, recipient) {
				if (!err) {
					if (!recipient) {
						res.send("RECIPIENT USER DOES NOT EXIST");
					} else {
						// yo the recipient, at this point the recipient must exist
						yo.yo(recipient.user_id, function(err, yo_res) {
							if (!err) {
								res.status(200);
								res.send(sender.user_id + " just YO'd " + recipient.user_id + ".");
							} else {
								res.send("ERROR IN YO'ING RECIPIENT");
							}
						});
					}
				} else {
					res.send("ERROR IN RETRIEVING USER");
				}
			});
		} else {
			res.send("AUTHENTICATION ERROR");
		}
	});
});

module.exports = router;
