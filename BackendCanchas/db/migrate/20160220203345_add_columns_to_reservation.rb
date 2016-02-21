class AddColumnsToReservation < ActiveRecord::Migration
  def change
    add_column :reservations, :at_date, :date
    add_column :reservations, :confirmation_code, :string
  end
end
