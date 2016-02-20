var express = require('express');
var router = express.Router();
var url = require('url');
var mongoose = require('mongoose');
var canchas = mongoose.model('cancha');

/* GET home page. */
router.get('/cancha', function(req, res, next) {
    var query = require('url').parse(req.url,true).query;
    if (query.longitude === "" || query.latitude === "" || query.longitude === undefined || query.latitude === undefined) {
        res.sendStatus(404).end();
    } else {
        var limit = req.query.limit || 10;
        // get the max distance or set it to 8 kilometers
        var maxDistance = req.query.distance || 8;

        // we need to convert the distance to radians
        // the raduis of Earth is approximately 6371 kilometers
        maxDistance /= 6371;
        var coords = [];
        coords[0] = req.query.longitude || 0;
        coords[1] = req.query.latitude || 0;
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
