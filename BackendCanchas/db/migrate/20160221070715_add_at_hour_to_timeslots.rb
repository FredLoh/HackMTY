class AddAtHourToTimeslots < ActiveRecord::Migration
  def change
    add_column :timeslots, :at_hour, :integer
  end
end
