class DropAtTimeFromTimeslots < ActiveRecord::Migration
  def change
    remove_column :timeslots, :at_time
  end
end
