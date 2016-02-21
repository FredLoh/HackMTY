require 'test_helper'

class PlayFieldsControllerTest < ActionController::TestCase
  setup do
    @play_field = play_fields(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:play_fields)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create play_field" do
    assert_difference('PlayField.count') do
      post :create, play_field: {  }
    end

    assert_redirected_to play_field_path(assigns(:play_field))
  end

  test "should show play_field" do
    get :show, id: @play_field
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @play_field
    assert_response :success
  end

  test "should update play_field" do
    patch :update, id: @play_field, play_field: {  }
    assert_redirected_to play_field_path(assigns(:play_field))
  end

  test "should destroy play_field" do
    assert_difference('PlayField.count', -1) do
      delete :destroy, id: @play_field
    end

    assert_redirected_to play_fields_path
  end
end
