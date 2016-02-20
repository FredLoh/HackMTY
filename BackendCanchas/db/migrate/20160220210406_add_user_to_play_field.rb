class AddUserToPlayField < ActiveRecord::Migration
  def change
    add_reference :play_fields, :user, foreign_key: true 
  end
end
