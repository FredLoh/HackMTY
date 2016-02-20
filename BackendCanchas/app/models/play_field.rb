class PlayField < ActiveRecord::Base
  has_many :Timeslots
  belongs_to :user
end
