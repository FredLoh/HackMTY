class Timeslot < ActiveRecord::Base
  belongs_to :play_field
  has_many :reservations
end
