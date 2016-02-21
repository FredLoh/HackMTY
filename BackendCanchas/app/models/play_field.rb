class PlayField < ActiveRecord::Base
  has_many :timeslots
  belongs_to :user
  validates :name, presence: true
  validates :field_type, presence: true
end
