var mongoose = require('mongoose');

var PlaceSchema = new mongoose.Schema({
	user_id: { type: String},
	place: { latitude: Number, longitude: Number },
	timestamp: { type: Date, default: Date.now }
});

var Place = mongoose.model('Place', PlaceSchema);

exports.Place = Place;
exports.PlaceSchema = PlaceSchema;