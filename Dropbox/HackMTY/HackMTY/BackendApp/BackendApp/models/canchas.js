var mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');
var Schema = mongoose.Schema;

var LocationSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true
    },
    uuid: {
        type: String,
        required: true,
        unique: true
    },
    location: {
        type: [Number],
        required: true,
        index: '2dsphere'
    },
    rentas: {
        type: [Number],
    },

    update_date: Date
}, {
    versionKey: false
});

LocationSchema.plugin(uniqueValidator);
mongoose.model('cancha', LocationSchema);
