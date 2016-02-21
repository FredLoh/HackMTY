# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://coffeescript.org/

updateGeoloc = (currentLocation, radius, isMarkerDropped) ->
 $('#play_field_geoloc').val(currentLocation.latitude + ',' + currentLocation.longitude)

getAction = ->
  classes = $('body').attr('class').split(/\s+/)
  return classes[classes.length-1]



loadMaps = ->
  action = getAction()
  if action != undefined && action != null && (action == "edit" || action == "new")
    if $('#play_field_geoloc').val.length < 3
      geolocVar = [25.6523008249069,-100.28966475677487]
    else
      geolocVar = $('#play_field_geoloc').val().split(',')
    $('#editMap').locationpicker({
      # location: {latitude: parseFloat(geolocVar[0]), longitude: parseFloat(geolocVar[1])},
      # locationName: $('#locationName').val(),
      radius: 30,
      location: {latitude: parseFloat(geolocVar[0]), longitude: parseFloat(geolocVar[1])},
      zoom: 15,
      scrollwheel: true,
      enableAutocomplete: false,
      enableReverseGeocode: true,
      onchanged: updateGeoloc,
    })

$(document).on 'page:load', loadMaps
$(document).ready(loadMaps)
