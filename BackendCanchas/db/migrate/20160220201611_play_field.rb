class PlayField < ActiveRecord::Migration
  def change
    create_table :play_fields do |t|
      t.timestamps null: false
    end
  end
end
