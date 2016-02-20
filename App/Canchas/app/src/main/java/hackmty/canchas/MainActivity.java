package hackmty.canchas;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int s_year, s_month, s_day, s_hour, s_min;

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




    public void pickDate(View v){

        //set date

        final Calendar c = Calendar.getInstance();
        final int def_year = c.get(Calendar.YEAR);
        final int def_month = c.get(Calendar.MONTH);
        final int def_day = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog d = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {



                view.setSpinnersShown(true);
//                view.setMinDate(c.getTimeInMillis());


//                              if(year >= def_year && monthOfYear >= def_month && dayOfMonth >= def_day) {
//
//
//
//
//                } else {
//
//                    pickTime();
//
//                }

                pickTime(year, monthOfYear, dayOfMonth);


            }


        }, def_year, def_month, def_day);

        d.getDatePicker().setMinDate(c.getTimeInMillis());

        d.show();



    }



    public void pickTime(final int y, final int m, final int dd){

        //set time

        final Calendar c = Calendar.getInstance();
        int def_hour = c.get(Calendar.HOUR_OF_DAY);
        int def_minute = c.get(Calendar.MINUTE);


        TimePickerDialog d = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {




            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                if(minute % 5 != 0 && minute % 5 < 3){

                    minute -= minute % 5;

                } else if(minute % 5 != 0){

                    minute += 5 - minute % 5;

                }

                setDateTime(y, m, dd, hourOfDay, minute);

            }


        }, def_hour, def_minute, false);



        d.show();




    }

    public void setDateTime(int y, int mm, int d, int h, int m){

        //sets parameters from pickers

        s_year = y;
        s_month = mm;
        s_day = d;
        s_hour = h;
        s_min = m;




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


}
