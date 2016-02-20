var mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');
var Schema = mongoose.Schema;

var LocationSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true
    },
    location: {
        type: [Number],
        required: true,
        index: '2d'
    },
    open_time: {
        type: Date,
        required: true
    },
    close_time: {
        type: Date,
        required: true
    },
    update_date: Date,
    rentas: [{
        update_date: Date,
        date_and_time: {
            type: Date,
            unique: true,
            required: true
        },
        duration: {
            type: Number,
            required: true
        }
    }]
}, {
    versionKey: false
});

LocationSchema.plugin(uniqueValidator);
mongoose.model('cancha', LocationSchema);
