class AddPlayFieldToTimeslot < ActiveRecord::Migration
  def change
    add_reference :timeslots, :play_field, foreign_key: true
  end
end
