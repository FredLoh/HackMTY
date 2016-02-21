var mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');
var Schema = mongoose.Schema;

var RentSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true
    },
    players: {
        type: [String],
        required: true
    },
    hora: String,
    duration: Number,
    cancha_id: Number
}, {
    versionKey: false
});

mongoose.model('renta', RentSchema);
