class Reservation < ActiveRecord::Base
  belongs_to :Timeslot
end
