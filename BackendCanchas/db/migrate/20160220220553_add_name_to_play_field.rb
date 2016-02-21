class AddNameToPlayField < ActiveRecord::Migration
  def change
    add_column :play_fields, :name, :string
  end
end
