var mongoose = require('mongoose');

var UserSchema = new mongoose.Schema({
	user_id: { type: String, unique: true },
	password: String,
	salt: String,
	location: { latitude: Number, longitude: Number },
	lastTimeActive: { type: Date, default: Date.now }
});

var User = mongoose.model('User', UserSchema);

exports.User = User;