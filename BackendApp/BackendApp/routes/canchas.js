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
                    //HTML response will render the index.jade file in the views/canchas folder. We are also setting "canchas" to be an accessible variable in our jade view
                    // html: function(){
                    //     res.render('canchas/index', {
                    //           title: 'Canchas List',
                    //           "canchas" : canchas
                    //       });
                    // },
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
        console.log("POSTing new Canchas");
        console.log(req.body);
        // Get values from POST request. These can be done through forms or REST calls. These rely on the "name" attributes for forms
        var name = req.body.name;
        var longitude = req.body.longitude;
        var latitude = req.body.latitude;
        var locArray = [longitude, latitude];
        var uuid = req.body.uuid;
        //call the create function for our database
        mongoose.model('cancha').create({
            name: name,
            uuid: uuid,
            location: locArray,
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
/* GET New cancha page. */
router.get('/new', function(req, res) {
    res.render('canchas/new', {
        title: 'Add New Cancha'
    });
});
// route middleware to validate :id
router.param('id', function(req, res, next, id) {
    //console.log('validating ' + id + ' exists');
    //find the ID in the Database
    mongoose.model('cancha').findById(id, function(err, cancha) {
        //if it isn't found, we are going to repond with 404
        if (err) {
            console.log(id + ' was not found');
            res.status(404)
            var err = new Error('Not Found');
            err.status = 404;
            res.format({
                html: function() {
                    next(err);
                },
                json: function() {
                    res.json({
                        message: err.status + ' ' + err
                    });
                }
            });
            //if it is found we continue on
        } else {
            //uncomment this next line if you want to see every JSON document response for every GET/PUT/DELETE call
            //console.log(blob);
            // once validation is done save the new item in the req
            req.id = id;
            // go to the next thing
            next();
        }
    });
});
router.route('/:id')
    .get(function(req, res) {
        mongoose.model('cancha').findById(req.id, function(err, cancha) {
            if (err) {
                console.log('GET Error: There was a problem retrieving: ' + err);
            } else {
                res.format({
                    // html: function(){
                    //     res.render('canchas/show', {
                    //       "cancha" : cancha
                    //     });
                    // },
                    JSON: function() {
                        res.json(cancha);
                    }
                });
            }
        });
    });
//GET the individual blob by Mongo ID
router.get('/:id/edit', function(req, res) {
    //search for the blob within Mongo
    mongoose.model('cancha').findById(req.id, function(err, cancha) {
        if (err) {
            console.log('GET Error: There was a problem retrieving: ' + err);
        } else {
            //Return the cancha
            //format the date properly for the value to show correctly in our edit form
            res.format({
                //HTML response will render the 'edit.jade' template
                html: function() {
                    res.render('canchas/edit', {
                        title: 'cancha' + cancha._id,
                        "cancha": cancha
                    });
                },
                //JSON response will return the JSON output
                json: function() {
                    res.json(cancha);
                }
            });
        }
    });
});
//PUT to update a blob by ID
router.put('/:id/edit', function(req, res) {
    console.log("PUT REQUEST");
    // Get our REST or form values. These rely on the "name" attributes
    var name = req.body.name;
    var longitude = req.body.longitude;
    var latitude = req.body.latitude;
    var loc = [longitude, latitude];
    var datetime = Date.now();
    //find the document by ID
    mongoose.model('cancha').findById(req.id, function(err, cancha) {
        //update it
        cancha.update({
            name: name,
            location: loc,
            updated_date: Date.now()
        }, function(err, canchaID) {
            if (err) {
                res.send("There was a problem updating the information to the database: " + err);
            } else {
                //HTML responds by going back to the page or you can be fancy and create a new view that shows a success page.
                res.format({
                    html: function() {
                        res.redirect("/api/canchas/" + cancha._id);
                    },
                    //JSON responds showing the updated values
                    json: function() {
                        res.json(cancha);
                    }
                });
            }
        });
    });
});
router.delete('/:id/edit', function(req, res) {
    //find blob by ID
    console.log("Delete REQUEST");
    console.log("Delete REQUEST");
    console.log("Delete     REQUEST");

    mongoose.model('cancha').findById(req.id, function(err, cancha) {
        if (err) {
            return console.error(err);
        } else {
            //remove it from Mongo
            cancha.remove(function(err, cancha) {
                if (err) {
                    return console.error(err);
                } else {
                    //Returning success messages saying it was deleted
                    console.log('DELETE removing ID: ' + cancha._id);
                    res.format({
                        //HTML returns us back to the main page, or you can create a success page
                        html: function() {
                            res.redirect("/api/canchas");
                        },
                        //JSON returns the item with the message that is has been deleted
                        json: function() {
                            res.json({
                                message: 'deleted',
                                item: cancha
                            });
                        }
                    });
                }
            });
        }
    });
});
module.exports = router;


module.exports = router;
