class PlayField < ActiveRecord::Base
  has_many :timeslots
  belongs_to :user
end
