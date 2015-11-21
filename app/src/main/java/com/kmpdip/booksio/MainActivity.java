package com.kmpdip.booksio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import com.kmpdip.booksio.cards.CardWrapper;
import com.kmpdip.booksio.cards.MyExpandCard;
import com.kmpdip.booksio.classification.Constants;
import com.kmpdip.booksio.classification.FeatureGenerator;
import com.kmpdip.booksio.classification.J48Wrapper;
import com.kmpdip.booksio.classification.WekaWrapper;
import com.kmpdip.booksio.data.database.DBCDatabase;
import com.kmpdip.booksio.data.structure.FacebookUserData;
import com.kmpdip.booksio.data.structure.LibraryBook;
import com.kmpdip.booksio.data.structure.Recommendation;
import com.kmpdip.booksio.data.structure.UserToClassify;
import com.kmpdip.booksio.facebookoperations.FacebookOperations;
import com.kmpdip.booksio.fragments.FragmentAdapter;
import com.kmpdip.booksio.fragments.LibraryFragment;
import com.kmpdip.booksio.fragments.RecommendationsFragment;
import com.kmpdip.booksio.onlineoperations.BookFromXml;
import com.kmpdip.booksio.parseoperations.ParseOperations;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardListView;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecommendationsFragment.RecommendationsFragmentListener, LibraryFragment.LibraryFragmentListener {
    private static MainActivity mainActivity;
    private static String DB_PATH = "/data/data/com.kmpdip.booksio/database/";
    private static String DB_NAME = "/dbcdatabase.db";
    public Context context;
    public String userClass;
    public DatabaseTask task;
    ParseUser currentUser;
    FacebookUserData fbUser;
    BookFromXml bookFromXml = BookFromXml.getInstance();
    List<Recommendation> recommendations = new ArrayList<>();
    List<LibraryBook> libraryBooks = new ArrayList<>();
    private static List<Card> myRecCardlist = new ArrayList<>();

    // Initialize fragment resources
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initLibraryCards();
        }
    };
    Random random;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ParseOperations mParseOperations;
    FacebookOperations mFacebookOperations;
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
    UserToClassify userToClassify;
    //this is the class that the user belongs to
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FragmentAdapter adapter;
    CardArrayAdapter mCardArrayAdapterRec;
    public static MainActivity getInstance() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainActivity = this;
        random = new Random();
        currentUser = ParseUser.getCurrentUser();
        mParseOperations = ParseOperations.getInstance(currentUser);
        mFacebookOperations = FacebookOperations.getInstance(currentUser);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        mCardArrayAdapterRec = new CardArrayAdapter(this, myRecCardlist);

        Log.i("Shared", "" + String.valueOf(preferences.getBoolean(Constants.HAS_LOGGED_IN_AGAIN, false)));
        // If it is the first time the user logs in the application we save the data in shared preferences and extract all the data we need from the facebook account
        //We emulate the extraction of these data since we needed to take permission to use the book information of each user so it needed more time than we had.
        if (preferences.getBoolean(Constants.HAS_LOGGED_IN_AGAIN, false)) {
            userToClassify = mParseOperations.getUserFeatures();
        } else {
            // If it is not the first time he logs in the Parse user object should contain all the information needed in order to classify the user
            fbUser = mFacebookOperations.getFbUserWithData();
            saveAllUserDataToSharedPreferences(fbUser);
            mParseOperations.updateParseUserOnline(fbUser);
            userToClassify = mParseOperations.getUserFeatures();
            editor.putBoolean(Constants.HAS_LOGGED_IN_AGAIN, true);
            editor.commit();
        }

        userClass = getPredictUserClass(userToClassify);

        setSupportActionBar(toolbar);


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

        createFragments();

        File dbFile = new File(DB_PATH + DB_NAME);
        if (dbFile.exists() == false) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mTextView = (TextView) findViewById(R.id.text_view_1);

        task = new DatabaseTask();
        task.execute();
        Log.i("Async", String.valueOf(task.getStatus()));
    }


    private void saveAllUserDataToSharedPreferences(FacebookUserData fbUser) {
        editor.putFloat(Constants.AGE, fbUser.getAge());
        editor.putFloat(Constants.ARTS_SPORTS, fbUser.getArts_sports());
        editor.putFloat(Constants.GENDER, fbUser.getGender());
        editor.putFloat(Constants.GEOGRAPHY, fbUser.getGeography());
        editor.putFloat(Constants.HISTORY, fbUser.getHistory());
        editor.putFloat(Constants.LITERATURE, fbUser.getLiterature());
        editor.putFloat(Constants.MATH_SCIENCES, fbUser.getMath_science());
        editor.putFloat(Constants.NUMBER_LOANS, fbUser.getNumber_loans());
        editor.putFloat(Constants.RELIGION, fbUser.getReligion());
        editor.putFloat(Constants.SOCIAL_SCIENCES, fbUser.getSocial_science());
        editor.putFloat(Constants.SCIENCE_RESEARCH, fbUser.getScience_research());
        editor.putFloat(Constants.PRACTICAL, fbUser.getPractical());
        editor.apply();
    }


    public void createFragments() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new FragmentAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
    }

    //methods for creating database in android storage
    private void copyDataBase() throws IOException {
        InputStream mInput = getApplicationContext().getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;

        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
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

    @Override
    public void initRecommendationCard() {

        for (Recommendation rec : recommendations) {
            myRecCardlist.add(createRecommendationCard(rec));
        }

        //Set the arrayAdapter
        CardListView cardListView = (CardListView) this.findViewById(R.id.myList);

        //animCardArrayAdapter.setInitialDelayMillis(500);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapterRec);
        }
    }

    @Override
    public Card createRecommendationCard(Recommendation book) {
        CardWrapper cardWrapper = new CardWrapper(this, book);
        MyExpandCard cardInside = new MyExpandCard(this, book);
        cardWrapper.addCardExpand(cardInside);

        //Add a viewToClickExpand to enable click on whole card
        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .highlightView(false)
                        .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);
        cardWrapper.setViewToClickToExpand(viewToClickToExpand);
        cardWrapper.setSwipeable(true);
        return cardWrapper;
    }


    @Override
    public void loadLibraryBooksFromDatabase() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DBCDatabase db = new DBCDatabase(MainActivity.getInstance().getApplicationContext());
                libraryBooks = db.getBooksDetails(1);
                db.close();
                mHandler.sendEmptyMessage(0);
            }
        };

        Thread mThread = new Thread(runnable);
        mThread.start();

    }

    @Override
    public Card createLibraryCard(LibraryBook book) {

        CardWrapper cardWrapper = new CardWrapper(this, book);
        MyExpandCard cardInside = new MyExpandCard(this, book);
        cardWrapper.addCardExpand(cardInside);

        //Add a viewToClickExpand to enable click on whole card
        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .highlightView(false)
                        .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);
        cardWrapper.setViewToClickToExpand(viewToClickToExpand);
        cardWrapper.setSwipeable(true);

        return cardWrapper;

    }

    @Override
    public void initLibraryCards() {
        List<Card> myCardlist = new ArrayList<>();

        for (LibraryBook lb : libraryBooks) {
            myCardlist.add(createLibraryCard(lb));
        }

        //Set the arrayAdapter
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, myCardlist);
        CardListView cardListView = (CardListView) this.findViewById(R.id.myListLibrary);

        //animCardArrayAdapter.setInitialDelayMillis(500);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }
    }

    private String getPredictUserClass(UserToClassify user) {
        String predictedClass = "";
        String[] featureHeader = Constants.LIST_FEATURES;
        Instances userInstance = FeatureGenerator.createEmptyInstances(featureHeader, false); // makeClassLabel);
// Calculate features (without class label)
        HashMap<String, Float> featureMapUser =
                FeatureGenerator.processUser(user);

        // Aggregate features in single Weka instance
        int attributeSize = featureMapUser.size() + 1;
        Instance instance = new Instance(attributeSize); // including class classLabel

        // Filling features for accelerometer
        for (String feature : featureMapUser.keySet()) {
            float value = featureMapUser.get(feature);
            Attribute attr = userInstance.attribute(feature);
            instance.setValue(attr, value);
        }

        instance.setDataset(userInstance);
        WekaWrapper mJ48 = new J48Wrapper();
        predictedClass = mJ48.predict(instance);
        Log.i("Prediction", "" + String.valueOf(predictedClass));
        return predictedClass;
    }


    public class DatabaseTask extends AsyncTask {

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
            MainActivity.getInstance().createFragments();
            initRecommendationCard();
            MainActivity.getInstance().loadLibraryBooksFromDatabase();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Object[] params) {
            DBCDatabase db = new DBCDatabase(MainActivity.getInstance().getApplicationContext());

            ArrayList books = db.getBooks(userClass, randomBooks);
            List<HashMap<String, String>> response = new ArrayList<>();
            for (int j = 0; j < books.size(); j++) {
                response.add(bookFromXml.consumeWebService((String) books.get(j)));
                Recommendation book = bookFromXml.createBookFromXMLResponse(response.get(j));
                recommendations.add(book);
                Log.i("BOOK", (String) books.get(j));
            }

            return response;
        }
    }
    public void removeCardFromList(Card usedCard) {
        myRecCardlist.remove(usedCard);
        mCardArrayAdapterRec.notifyDataSetChanged();
    }
}
