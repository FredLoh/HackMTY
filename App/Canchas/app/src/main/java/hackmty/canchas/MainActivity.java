package hackmty.canchas;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    int s_year, s_month, s_day, s_hour, s_min, endtime;

    String lat = "25.652061";
    String lng = "-100.286438";

    String urlend = "";

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void pickDate(View v) {

        //set date

        final Calendar c = Calendar.getInstance();
        final int def_year = c.get(Calendar.YEAR);
        final int def_month = c.get(Calendar.MONTH);
        final int def_day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                view.setSpinnersShown(true);


                pickTime(year, monthOfYear, dayOfMonth);


            }


        }, def_year, def_month, def_day);

        d.getDatePicker().setMinDate(c.getTimeInMillis());
        d.getDatePicker().setMaxDate(c.getTimeInMillis() + 1209600000L);

        d.show();


    }


    public void pickTime(final int y, final int m, final int dd) {

        //set time

        final Calendar c = Calendar.getInstance();
        int def_hour = c.get(Calendar.HOUR_OF_DAY);
        int def_minute = c.get(Calendar.MINUTE);


        TimePickerDialog d = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



                if(minute % 30 != 0){

                    if(minute < 15){

                        minute = 0;

                    } else if(minute >= 45){

                        hourOfDay += 1;
                        minute = 0;

                    } else {

                        minute = 30;
                    }

                }

                setDateTime(y, m, dd, hourOfDay, minute);

            }


        }, def_hour, def_minute, false);


        d.show();


    }

    public void setDateTime(int y, int mm, int d, int h, int m) {

        //sets parameters from pickers

        s_year = y;
        s_month = mm;
        s_day = d;
        s_hour = h;
        s_min = m;

        String str_min;

        if(m == 0){
            str_min = "00";
        } else {
            str_min = "" + m;
        }

        String formattedDate = d + "/" + mm + "/" + y + " a las " + h + ":" + str_min;

        TextView tv = (TextView) findViewById(R.id.datetime_tv);
        tv.setText(formattedDate);

        tv = (TextView) findViewById(R.id.timeslots_tv);
        tv.setVisibility(View.VISIBLE);

        NumberPicker np = (NumberPicker) findViewById(R.id.timeslots_hr_picker);
        np.setMaxValue(3);
        np.setMinValue(0);
        np.setValue(1);

        np = (NumberPicker) findViewById(R.id.timeslots_min_picker);
        np.setMaxValue(1);
        np.setMinValue(0);
        np.setValue(0);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {

                if (value == 0) {
                    return "00";
                }

                return "30";
            }
        });

        try {
            Method method = np.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(np, true);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.timeslots_time_picker);
        ll.setVisibility(View.VISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }

    public void search(View v) {

        int dur = 0;

        NumberPicker np = (NumberPicker) findViewById(R.id.timeslots_hr_picker);

        dur += np.getValue() * 60;

        np = (NumberPicker) findViewById(R.id.timeslots_min_picker);

        dur += np.getValue() * 30;



        urlend = "&year=" + s_year + "&month=" + (s_month+1) + "&day=" + s_day ;


        endtime = s_hour;

        if((s_min + dur)%60 == 0 ){

            endtime += (s_min + dur)/60 - 1;

        } else {

            endtime += (s_min + dur)/60 ;

        }


        date = s_year + "-" + (s_month+1) + "-" + s_day;


        urlend += "&start_time=" + s_hour + "&end_time=" + endtime;

        String urler = "http://45.55.30.36/api/cancha?lat=" + lat + "&long=" + lng + urlend;



        Log.i("requesturl", urler);

        new SearchAsync().execute(urler);


    }

    private class SearchAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Buscando canchas disponibles...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return downloadUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();

            if (s == null) {
                Log.e("REQUEST ERROR", "empty response");
            } else {


                Log.i("REQUEST SUCCESS", s);

                ArrayList <Cancha> canchas = new ArrayList<>();

                try {

                    JSONArray arr = new JSONArray(s);

                    Location loc = new Location("");
                    loc.setLatitude(Double.parseDouble(lat));
                    loc.setLongitude(Double.parseDouble(lng));

                    Log.i("main_loc1", loc.toString());

                    for (int i = 0; i < arr.length(); i++){



                        JSONObject obj = arr.getJSONObject(i);

                        String o_id = obj.getString("_id");
                        String o_name = obj.getString("name");
                        String o_lng = obj.getJSONArray("location").get(0).toString();
                        String o_lat = obj.getJSONArray("location").get(1).toString();

                        JSONArray jarr = obj.getJSONArray("rentas");



                        Location loc2 = new Location("");
                        loc2.setLongitude((double) obj.getJSONArray("location").get(0));
                        loc2.setLatitude((double) obj.getJSONArray("location").get(1));

                        Log.i("main_loc2", loc2.toString());

                        double dist = loc.distanceTo(loc2);


                        Log.i("main_dist", dist + "");


                        if(jarr.length() != 0) {
                            Cancha cancha = new Cancha(o_id, o_name, o_lng, o_lat);
                            cancha.setDist(dist);
                            cancha.setTimes(s_hour, endtime);

                            cancha.setTimeslots(jarr);

                            canchas.add(cancha);

                        }



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //OPEN MAP
                Intent i = new Intent("hackmty.canchas.MAP");

                int cnt = 0;
                for (Cancha c : canchas){


                    i.putExtra("id" + cnt, c.getId());
                    i.putExtra("name" + cnt, c.getName());
                    i.putExtra("lat" + cnt, c.getLat());
                    i.putExtra("lng" + cnt, c.getLng());
                    i.putExtra("dist" + cnt, c.getDist());
                    i.putExtra("times" + cnt, c.getTimeslots().toString());


                    cnt++;

                }

                i.putExtra("num", cnt);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                i.putExtra("urlend", urlend);
                i.putExtra("start", s_hour);
                i.putExtra("end", endtime);
                i.putExtra("date", date);

                startActivity(i);


//                i.putExtra("")

            }

        }
    }


    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.

        try {
            URL url = new URL(myurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);

            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readIt(InputStream stream) throws IOException {



        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        return responseStrBuilder.toString();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

        if (!mResolvingError && result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, 1001);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else if(!mResolvingError) {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }

    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt("dialog_error", errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");

    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt("dialog_error");
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, 1001);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = (String.valueOf(mLastLocation.getLatitude()));
            lng = (String.valueOf(mLastLocation.getLongitude()));
        }

    }


    @Override
    protected void onStart() {

        if(!mResolvingError) {
            mGoogleApiClient.connect();
        }
        super.onStart();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }
}
