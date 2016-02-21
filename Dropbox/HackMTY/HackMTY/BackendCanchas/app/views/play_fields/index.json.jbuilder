json.array!(@play_fields) do |play_field|
  json.extract! play_field, :id
  json.url play_field_url(play_field, format: :json)
end
