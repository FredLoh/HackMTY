var express = require('express'),
    router = express.Router(),
    mongoose = require('mongoose'), //mongo connection
    bodyParser = require('body-parser'), //parses information from POST
    methodOverride = require('method-override'); //used to manipulate POST
//var Bitpay = require('bitpay-api');
//var bitpay = new BitPay("GQXd8sWMRV4ZZYFeThwbvo");

router.use(bodyParser.urlencoded({
    extended: true
}));
router.use(methodOverride(function(req, res) {
    if (req.body && typeof req.body === 'object' && '_method' in req.body) {
        // look in urlencoded POST bodies and delete it
        var method = req.body._method;
        delete req.body._method;
        return method;
    }
}));

router.route('/')
    .get(function(req, res, next) {
        //retrieve all canchas from Monogo
        mongoose.model('cancha').find({}, function(err, canchas) {
            if (err) {
                return console.error(err);
            } else {
                //respond to both HTML and JSON. JSON responses require 'Accept: application/json;' in the Request Header
                res.format({
                     html: function(){
                         res.render('bitpay/index', {
                               title: 'Bitpay List'
                           });
                     },
                    //JSON response will show all blobs in JSON format
                    JSON: function() {
                        res.json(canchas);
                    }
                });
            }
        });
    })
    .post(function(req, res) {
        console.log("POSTing new Bitpay");
        // Get values from POST request. These can be done through forms or REST calls. These rely on the "name" attributes for forms

        //call the create function for our database
        });

module.exports = router;