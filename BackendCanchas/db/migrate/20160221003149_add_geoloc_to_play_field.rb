class AddGeolocToPlayField < ActiveRecord::Migration
  def change
    add_column :play_fields, :geoloc, :string
  end
end
