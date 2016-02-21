class AddAvailableToTimeslot < ActiveRecord::Migration
  def change
    add_column :timeslots, :available, :boolean
  end
end
