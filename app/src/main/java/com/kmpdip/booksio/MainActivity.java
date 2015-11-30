package com.kmpdip.booksio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kmpdip.booksio.cards.CardWrapper;
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
import com.kmpdip.booksio.fragments.LibraryHasReadFragment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RecommendationsFragment.RecommendationsFragmentListener,
        LibraryFragment.LibraryFragmentListener{

    public static String genre = "";
    public static String genreName = "";
    private static MainActivity mainActivity;
    private static String DB_PATH = "/data/data/com.kmpdip.booksio/database/";
    private static String DB_NAME = "/dbcdatabase.db";
    private static List<Card> myRecCardlist = new ArrayList<>();
    public Context context;
    public String userClass;
    public DatabaseTask task;
    public DatabaseTaskGenres taskGenres;
    public String mode = "user_class";
    ParseUser currentUser;
    FacebookUserData fbUser;
    BookFromXml bookFromXml = BookFromXml.getInstance();
    List<Recommendation> recommendations;
    List<Recommendation> recommendationsByGenre;
    List<LibraryBook> libraryBooks = new ArrayList<>();
    // Initialize fragment resources
    Handler mHandler = new Handler();
// {
//        @Override
//        public void handleMessage(Message msg) {
//            initLibraryCards();
//            initLibraryHasReadCards();
//        }
//
//    };
    List<LibraryBook> libraryHasReadBooks = new ArrayList<>();
    Random random;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ParseOperations mParseOperations;
    FacebookOperations mFacebookOperations;
    TextView mTextView;
    UserToClassify userToClassify;
    CardArrayAdapter mCardArrayAdapterRec;
    //this is the class that the user belongs to
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FragmentAdapter adapter;

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setIcon(R.drawable.chinese3);
        navigationView.getMenu().getItem(1).setIcon(R.drawable.religion20);
        navigationView.getMenu().getItem(2).setIcon(R.drawable.society);
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ejer);
        navigationView.getMenu().getItem(4).setIcon(R.drawable.science49);
        navigationView.getMenu().getItem(5).setIcon(R.drawable.wrench108);
        navigationView.getMenu().getItem(6).setIcon(R.drawable.comedy2);
        navigationView.getMenu().getItem(7).setIcon(R.drawable.reading21);
        navigationView.getMenu().getItem(8).setIcon(R.drawable.antique25);

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
        genreName = (String) item.getTitle();
        if (id == R.id.nav_1) {
            // Handle the camera action
            genre = "1";

            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_2) {
            genre = "2";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();

        } else if (id == R.id.nav_3) {
            genre = "3";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_4) {
            genre = "4";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_5) {
            genre = "5";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_6) {
            genre = "6";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_7) {
            genre = "7";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_8) {
            genre = "8";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
        } else if (id == R.id.nav_9) {
            genre = "9";
            mode = "genre";
            taskGenres = new DatabaseTaskGenres();
            taskGenres.execute();
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
        if (mode.equals("user_class")) {
            myRecCardlist.clear();
            for (Recommendation rec : recommendations) {
                myRecCardlist.add(createRecommendationCard(rec));
            }
        } else if (mode.equals("genre")) {
            myRecCardlist.clear();
            for (Recommendation rec : recommendationsByGenre) {
                myRecCardlist.add(createRecommendationCard(rec));
            }
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
        return cardWrapper;
    }


    @Override
    public void loadLibraryBooksFromDatabase() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DBCDatabase db = new DBCDatabase(MainActivity.getInstance().getApplicationContext());
                libraryBooks = db.getBooksDetails(1);
                libraryHasReadBooks = db.getBooksDetails(2);
                Log.i("Books like","  " + libraryBooks.size());
                Log.i("Books to read", "  " + libraryHasReadBooks.size());
                db.close();
                mHandler.post((new Runnable() {
                    @Override
                    public void run() {
                        initLibraryCards();
                    }
                }));
//                        .sendEmptyMessage(0);
            }
        };

        Thread mThread = new Thread(runnable);
        mThread.start();

    }

    @Override
    public Card createLibraryCard(LibraryBook book) {

        CardWrapper cardWrapper = new CardWrapper(this, book);
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

        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }
    }

//    @Override
//    public void initLibraryHasReadCards() {
//        List<Card> mylibraryHasReadCardlist = new ArrayList<>();
//
//        for (LibraryBook lb : libraryHasReadBooks) {
//            mylibraryHasReadCardlist.add(createLibraryHasReadCard(lb));
//        }
//
//        //Set the arrayAdapter
//        CardArrayAdapter mLibraryHasReadCardArrayAdapter = new CardArrayAdapter(this, mylibraryHasReadCardlist);
//        CardListView cardLibraryHasReadListView = (CardListView) this.findViewById(R.id.my_list_library_has_read);
//
//        if (cardLibraryHasReadListView != null) {
//            cardLibraryHasReadListView.setAdapter(mLibraryHasReadCardArrayAdapter);
//        }
//    }
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

        // Filling features of use
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

    public void removeCardFromList(Card usedCard) {
        myRecCardlist.remove(usedCard);
        mCardArrayAdapterRec.remove(usedCard);
        mCardArrayAdapterRec.notifyDataSetChanged();
        checkCardList();
    }

    private void checkCardList() {
        if (myRecCardlist.size() < 3) {
            task.cancel(false);
            task = null;
            task = new DatabaseTask();
            task.execute();
        }
    }

//    @Override
//    public Card createLibraryHasReadCard(LibraryBook book) {
//        CardWrapper cardLibraryHasReadWrapper = new CardWrapper(this, book);
//        return cardLibraryHasReadWrapper;
//    }



    public class DatabaseTask extends AsyncTask {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /**
         * application context.
         */
        @Override
        protected void onPreExecute() {
            if (mCardArrayAdapterRec.isEmpty()) {
                this.dialog.setMessage("Vent venligst");
                this.dialog.show();
            }
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            MainActivity.getInstance().createFragments();
            initRecommendationCard();
            MainActivity.getInstance().loadLibraryBooksFromDatabase();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Object[] params) {
            recommendations = new ArrayList<>();
            DBCDatabase db = new DBCDatabase(MainActivity.getInstance().getApplicationContext());
            ArrayList books = db.getBooks(userClass);
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

    public class DatabaseTaskGenres extends AsyncTask {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /**
         * application context.
         */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Vent venligst");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Object response) {
            super.onPostExecute(response);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            MainActivity.getInstance().createFragments();
            initRecommendationCard();
            MainActivity.getInstance().loadLibraryBooksFromDatabase();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Object[] params) {
            recommendationsByGenre = new ArrayList<>();
            DBCDatabase db = new DBCDatabase(MainActivity.getInstance().getApplicationContext());
            ArrayList books = db.getBooksByGenre(genre);
            List<HashMap<String, String>> response = new ArrayList<>();
            for (int j = 0; j < books.size(); j++) {
                response.add(bookFromXml.consumeWebService((String) books.get(j)));
                Recommendation book = bookFromXml.createBookFromXMLResponse(response.get(j));
                recommendationsByGenre.add(book);
                Log.i("BOOKBYGENRE", (String) books.get(j));
            }

            return response;
        }
    }
}
