class Reservation < ActiveRecord::Base
  belongs_to :timeslot
end
