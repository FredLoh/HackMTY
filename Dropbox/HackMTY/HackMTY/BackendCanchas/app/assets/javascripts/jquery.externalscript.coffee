jQuery.externalScript = (url, options) ->
  # allow user to set any option except for dataType, cache, and url
  options = $.extend(options or {},
    dataType: 'script'
    cache: true
    url: url)
  # Use $.ajax() since it is more flexible than $.getScript
  # Return the jqXHR object so we can chain callbacks
  jQuery.ajax options

  #$.externalScript(ga_src).done (script, textStatus) ->
  #  console.log 'Script loading: ' + textStatus
  #    if typeof _gat != 'undefined'
  #        console.log 'Okay. GA file loaded.'
  #          else
  #              console.log 'Problem. GA file not loaded.'
  #                return
