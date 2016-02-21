class AddTimeslotToReservation < ActiveRecord::Migration
  def change
    add_reference :reservations, :timeslot, index: true, foreign_key:true
  end
end
