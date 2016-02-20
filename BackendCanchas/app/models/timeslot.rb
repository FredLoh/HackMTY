class Timeslot < ActiveRecord::Base
  belongs_to :PlayField
  has_many :reservations
end
