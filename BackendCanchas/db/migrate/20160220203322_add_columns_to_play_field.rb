class AddColumnsToPlayField < ActiveRecord::Migration
  def change
    add_column :play_fields, :field_type, :string
  end
end
