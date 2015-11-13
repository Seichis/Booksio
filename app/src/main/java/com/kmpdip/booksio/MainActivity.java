package com.kmpdip.booksio;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kmpdip.booksio.data.database.DBCDatabase;
import com.kmpdip.booksio.data.structure.Book;
import com.kmpdip.booksio.fragments.FragmentAdapter;
import com.kmpdip.booksio.fragments.RecommendationsFragment;
import com.kmpdip.booksio.onlineoperations.BookFromXml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    // Initialize fragment resources
    private final Handler handler = new Handler();
    TextView mTextView;
    List<String> randomBooks = Arrays.asList("870970-basis:27069703", "870970-basis:51039629", "870970-basis:50653463", "870970-basis:05636078", "870970-basis:28410352",
            "870970-basis:23461854",
            "870970-basis:21526231",
            "870970-basis:26527988",
            "870970-basis:51039629",
            "870970-basis:51255372",
            "870970-basis:24633489",
            "870970-basis:50653463",
            "870970-basis:51041631",
            "870970-basis:42511773");
    //this is the class that the user belongs to
    String userClass = "3";
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new FragmentAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);


        mTextView = (TextView) findViewById(R.id.text_view_1);

        DatabaseTask task = new DatabaseTask();
        task.execute();


    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        } else if (id == R.id.action_recommendations) {
            RecommendationsFragment dialog = new RecommendationsFragment();
            dialog.show(getSupportFragmentManager(), "RecommendationsFragment");
            return true;
        } else if (id == R.id.action_library) {
            RecommendationsFragment dialog = new RecommendationsFragment();
            dialog.show(getSupportFragmentManager(), "RecommendationsFragment");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class DatabaseTask extends AsyncTask {

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Object[] params) {
            Book book;
            DBCDatabase db = new DBCDatabase(MainActivity.context);
            BookFromXml bookFromXml = BookFromXml.getInstance();
            ArrayList books = db.getBooks(userClass, randomBooks);
            List<HashMap<String, String>> response = new ArrayList<HashMap<String, String>>();
            for (int j = 0; j < books.size(); j++) {
                response.add(bookFromXml.consumeWebService((String) books.get(j)));
                book = bookFromXml.createBookFromXMLResponse(response.get(j));
                Log.i("BOOK", (String) books.get(j));
            }
            for (Map<String, String> map : response) {
                Log.i("XMLRESPONSE", map.get("title"));
            }
            db.close();

            return response;
        }
    }

}
