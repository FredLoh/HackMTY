var express = require('express'),
    router = express.Router(),
    mongoose = require('mongoose'), //mongo connection
    bodyParser = require('body-parser'), //parses information from POST
    methodOverride = require('method-override'); //used to manipulate POST

var Bitpay = require('bitpay-api');

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
    //POST a new blob
    .post(function(req, res) {
        console.log("POSTing new Renta");
        // Get values from POST request. These can be done through forms or REST calls. These rely on the "name" attributes for forms
        var bitpay = new Bitpay("WHiK1rKAKyZbAroQPQo7iHctfCJhnJ0GSUXiD50ZKWU");
        order = {
            price: "100",
            currency: "USD"
        };
        bitpay.createInvoice(order, function test(err, data) {
            console.log(data)
        })

        //call the create function for our databas
    });

module.exports = router;