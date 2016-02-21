class AddColumnsToTimeslot < ActiveRecord::Migration
  def change
    add_column :timeslots, :at_time, :time
  end
end
