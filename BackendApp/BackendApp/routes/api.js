var express = require('express');
var http = require('http');
var https = require("https");
var router = express.Router();
var url = require('url');
var mongoose = require('mongoose');
var canchas = mongoose.model('cancha');


var options = {
    host: 'localhost',
    path: '/api/canchas'
};

callback = function(response) {
    var str = '';
    console.log("GOT HERE LADS");
    //another chunk of data has been recieved, so append it to `str`
    response.on('data', function (chunk) {
        str += chunk;
    });

    //the whole response has been recieved, so we just print it out here
    response.on('end', function () {
        console.log(str);

    });
};

/* GET home page. */
router.get('/cancha', function(req, res, next) {
    var query = require('url').parse(req.url,true).query;
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
    } else if (query.hor < 0 || query.hor > 24) {
        res.sendStatus(404).end();
    } else if (query.minu < 0 || query.minu > 59) {
        res.sendStatus(404).end();
    } else {
        var limit = req.query.limit || 5;
        // get the max distance or set it to 8 kilometers
        var maxDistance = req.query.dist || 30;
        maxDistance = maxDistance * 15;

        http.request(options, callback).end();
        var hora = req.query.hor;
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
                var arreglo = [];
                itemsProcessed = 0;
                locations.forEach(function(entry) {
                    console.log("UUID: " + entry.uuid);
                    http.get({
                        host: '45.55.30.36',
                        path: '/api/getAvailableHours/' + entry.uuid + '/2016-02-21',
                        port: 3000
                    }, function(response) {
                        // Continuously update stream with data
                        var body = '';
                        response.on('data', function(d) {
                            body += d;
                        });
                        response.on('end', function() {

                            var parsed = JSON.parse(body);

                            
                            for(var i = 0; i < parsed.data.length; i++) {
                                if(parsed.data[i].available == true && parsed.data[i].at_hour == hora) {
                                    //arre.push(parsed.data[i].id);
                                    console.log(parsed.data[i].id);
                                    entry['rentas'].push(parsed.data[i].id);
                                    arreglo.push(entry);
                                    console.log(arreglo);
                                }
                            }



                            itemsProcessed++;
                            if (itemsProcessed == locations.length) {
                                callback()
                            }
                        });
                    });
                });
                function callback() {
                    console.log(arreglo);
                    res.json(arreglo)
                }
            }
        });
    }
});

module.exports = router;
