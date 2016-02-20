var express = require('express'),
    router = express.Router(),
    mongoose = require('mongoose'), //mongo connection
    bodyParser = require('body-parser'), //parses information from POST
    methodOverride = require('method-override'); //used to manipulate POST

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
                JSON: function() {
                    console.log("CANCHAS");
                    res.json(canchas);
                }
            });
        }
    });
})
//POST a new blob
    .post(function(req, res) {
        console.log("POSTing new Canchas");
        // Get values from POST request. These can be done through forms or REST calls. These rely on the "name" attributes for forms
        var name = req.body.name;
        var longitude = req.body.longitude;
        var latitude = req.body.latitude;
        var locArray = [longitude, latitude];
        var datetime = new Date();
        var open_time_date = req.body.open_time;
        var close_time_date = req.body.close_time;
        //call the create function for our database
        mongoose.model('cancha').create({
            name: name,
            location: locArray,
            updated_date: datetime,
            open_time: open_time_date,
            close_time: close_time_date
        }, function(err, cancha) {
            if (err) {
                console.log(err);
                res.send("There was a problem adding the information to the database.");
            } else {
                //Cancha has been created
                console.log('POST creating new cancha: ' + cancha);
                res.format({
                    //HTML response will set the location and redirect back to the home page.
                    html: function() {
                        // If it worked, set the header so the address bar doesn't still say /adduser
                        res.location("/api/canchas");
                        // And forward to success page
                        res.redirect("/api/canchas");
                    },
                    //JSON response will show the newly created blob
                    json: function() {
                        res.json(cancha);
                    }
                });
            }
        });
    });
router.get('/new', function(req, res) {
    res.render('canchas/new', {
        title: 'Add New Cancha'
    });
});
module.exports = router;
