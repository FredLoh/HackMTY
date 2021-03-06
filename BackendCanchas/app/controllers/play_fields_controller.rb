require "net/http"
require "uri"


class PlayFieldsController < ApplicationController
  before_action :set_play_field, only: [:show, :edit, :update, :destroy]
  before_action :authenticate_user!, except: [:getAvailableHours, :reserveSpot, :getAvailableFields]

  # GET /play_fields
  # GET /play_fields.json
  def index
    @play_fields = PlayField.where(user: current_user)
    # @play_fields = PlayField.all
  end

  # GET /play_fields/1
  # GET /play_fields/1.json
  def show
  end

  # GET /play_fields/new
  def new
    @play_field = PlayField.new
    @play_field.geoloc = "25.650637336352496,-100.28975058746335"
  end

  # GET /play_fields/1/edit
  def edit
    # binding.pry
  end

  # POST /play_fields
  # POST /play_fields.json
  def create
    @play_field = PlayField.new(play_field_params)
    @play_field.user = current_user
    respond_to do |format|
      if @play_field.save
        for i in 0..23
          # binding.pry
          if params[:times] && params[:times][i.to_s]
            avail = true
          else
            avail = false
          end
          timeslot = Timeslot.new( { play_field: @play_field, at_hour: i, available: avail })
          timeslot.save
        end
        format.html { redirect_to @play_field, notice: 'Play field was successfully created.' }
        format.json { render :show, status: :created, location: @play_field }
      else
        format.html { render :new }
        format.json { render json: @play_field.errors, status: :unprocessable_entity }
      end
    end

    uri = URI.parse("http://45.55.30.36/api/canchas")

    # Shortcut
    response = Net::HTTP.post_form(uri, { "uuid" => @play_field.id, "name" => @play_field.name, "latitude" => @play_field.geoloc.split(',')[0], "longitude" => @play_field.geoloc.split(',')[1] } )

    puts response
    # Full control
    # http = Net::HTTP.new(uri.host, uri.port)

    # request = Net::HTTP::Post.new(uri.request_uri)
    # request.set_form_data({"q" => "My query", "per_page" => "50"})

    # Tweak headers, removing this will default to application/x-www-form-urlencoded 
    # request["Content-Type"] = "application/json"

    # response = http.request(request)
  end

  # PATCH/PUT /play_fields/1
  # PATCH/PUT /play_fields/1.json
  def update
    respond_to do |format|
      # binding.pry
      if @play_field.update(play_field_params)
        time = Hash.new
        for i in 0..23
          if params[:time] && params[:times][i.to_s]
           time[i] = {available: true}
          else
            time[i] = {available: false}
          end
          # timeslot = Timeslot.update( { play_field: @play_field, at_hour: i, available: avail })
          # timeslot.save
        end
        Timeslot.update(Timeslot.where(play_field: @play_field).order("created_at ASC").pluck(:id), time)
        format.html { redirect_to @play_field, notice: 'Play field was successfully updated.' }
        format.json { render :show, status: :ok, location: @play_field }
      else
        format.html { render :edit }
        format.json { render json: @play_field.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /play_fields/1
  # DELETE /play_fields/1.json
  def destroy
    @play_field.destroy
    respond_to do |format|
      format.html { redirect_to play_fields_url, notice: 'Play field was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  def getAvailableHours
    q1 = Timeslot.where(play_field_id: params[:id]).joins(:reservations).where( "at_date = DATE(?)", params[:time]).where(available: true)
    data = Timeslot.where(play_field_id: params[:id]).where(available: true).where.not(id: q1.pluck(:id))
    # binding.pry
    render :json => {:data => data }
  end

  def getAvailableFields
    q1 = Timeslot.where(play_field_id: params[:id]).where(at_hour: Range.new(params[:start], params[:end])).where(available: true)
    q1 = q1.where.not(id: q1.joins(:reservations).where("at_date = Date(?)", params[:date]).pluck(:id))
    # binding.pry
    render :json => {:data => q1 }
  end

  def reserveSpot
    rs = Reservation.new(at_date: params[:date], confirmation_code: SecureRandom.hex, timeslot_id: params[:id])
    rs.save
    render :json => { :confirmation_code => rs.confirmation_code }
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_play_field
      @play_field = PlayField.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def play_field_params
      # params.fetch(:play_field, {:name, })
      params.require(:play_field).permit( :name, :field_type, :geoloc)
    end
end
