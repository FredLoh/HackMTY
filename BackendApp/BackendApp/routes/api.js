var express = require('express');
var router = express.Router();
var url = require('url');
var mongoose = require('mongoose');
var canchas = mongoose.model('cancha');

/* GET home page. */
router.get('/cancha', function(req, res, next) {
    var query = require('url').parse(req.url,true).query;
    console.log(query);
    if (query.long === "" || query.lat === "" || query.long === undefined || query.lat === undefined || query.day === "" || query.day === undefined
        || query.month === "" || query.month === undefined || query.year === "" || query.year === undefined || query.hor === "" || query.hor === undefined
        || query.minu === "" || query.minu === undefined || query.dur === "" || query.dur === undefined ) {
        res.sendStatus(404).end();
    } else if (query.dur % 30 != 0) {
        res.sendStatus(404).end();
    } else if (query.day < 1 || query.day > 31) {
        res.sendStatus(404).end();
    } else if (query.month < 1 || query.month > 12) {
        res.sendStatus(404).end();
    } else if (query.year < 2016 || query.year > 2020) {
        res.sendStatus(404).end();
    } else if (query.hor < 1 || query.hor > 24) {
        res.sendStatus(404).end();
    } else if (query.minu < 0 || query.minu > 59) {
        res.sendStatus(404).end();
    } else {
        var limit = req.query.limit || 10;
        // get the max distance or set it to 8 kilometers
        var maxDistance = req.query.dist || 20;

        // we need to convert the distance to radians
        // the raduis of Earth is approximately 6371 kilometers
        maxDistance /= 6371;
        var coords = [];
        coords[0] = req.query.long || 0;
        coords[1] = req.query.lat || 0;
        var found = canchas.find({
            location: {
                $near: coords,
                $maxDistance: maxDistance
            }
        }).limit(limit).exec(function(err, locations) {
            if (err) {
                res.json({});
                res.end();
            } else {
                res.json(locations);
            }
        });
    }
});


module.exports = router;
