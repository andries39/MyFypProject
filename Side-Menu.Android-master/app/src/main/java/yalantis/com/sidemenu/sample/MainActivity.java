package yalantis.com.sidemenu.sample;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.BeaconParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.sample.fragment.ContentFragment;
import yalantis.com.sidemenu.util.ViewAnimator;



public class MainActivity extends AppCompatActivity implements BeaconConsumer,ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private int res = R.drawable.content_music;
    private LinearLayout linearLayout;

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private SearchFragment searchFragment;
    private FragmentManager mFragmentMgr;
    private SignInFragment signInFragment;
    private ViewInfoFragment viewInfoFragment;
    private MapFragment mapFragment;
    private int testrssi = 0;
    private String ibeacon001_id1 = "";
    private String ibeacon002_id1 = "";
    private String ibeacon003_id1 = "";

    private String ibeacon001_id2 = "";
    private String ibeacon002_id2 = "";
    private String ibeacon003_id2 = "";

    private String ibeacon001_id3 = "";
    private String ibeacon002_id3 = "";
    private String ibeacon003_id3 = "";

    private String currentPage;
    String currentBeacon = "";
    String newBeacon = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        searchFragment = new SearchFragment();
        signInFragment = new SignInFragment();
        viewInfoFragment = new ViewInfoFragment();
        mapFragment = new MapFragment();
        mFragmentMgr = getSupportFragmentManager();

        setBeaconID("beacon001");
        setBeaconID("beacon002");
        setBeaconID("beacon003");


        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.bind(this);
        ContentFragment contentFragment = ContentFragment.newInstance(R.drawable.content_music);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .commit();
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();

            }
        });


        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, contentFragment, drawerLayout, this);

        SharedPreferences pref = getSharedPreferences("beacon", MODE_PRIVATE);
        pref.edit()
                .putString("readBeacon", "null")
                .commit();
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        //SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.MOVIE, R.drawable.icn_7);
        //list.add(menuItem7);
    }


    private void setActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition) {
        this.res = this.res == R.drawable.content_music ? R.drawable.content_films : R.drawable.content_music;
        Log.i("TEST","change");
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackground(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();
        ContentFragment contentFragment = ContentFragment.newInstance(this.res);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();
        return contentFragment;
    }


    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.i(TAG,"Beacon size:"+collection.size());


                if (collection.size() > 0) {


                    List<Beacon> beacons = new ArrayList<>();
                    for (Beacon beacon : collection) {
//
                            beacons.add(beacon);

                    }

                    if (beacons.size() > 0) {

                        //
                        Log.i(TAG,"Heloo: "+"test123~~~~~~~~~~~~~~~~~~~~: "+ibeacon001_id1);
                        Collections.sort(beacons, new Comparator<Beacon>() {
                            public int compare(Beacon arg0, Beacon arg1) {
                                return arg1.getRssi()-arg0.getRssi();
                            }
                        });
                            Beacon nearBeacon = beacons.get(0);
                            if(nearBeacon.getId1().toString().equalsIgnoreCase(ibeacon002_id1)){
                                Log.i(TAG, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                            }
                       // TextView rssi= (TextView)findViewById(R.id.rssi);
                        //rssi.setText("Rssi: "+nearBeacon.getRssi());
                        if(nearBeacon.getId1().toString().equalsIgnoreCase(ibeacon001_id1)&& nearBeacon.getId2().toString().equalsIgnoreCase(ibeacon001_id2)&& nearBeacon.getId3().toString().equalsIgnoreCase(ibeacon001_id3)){
                            searchFragment.setInfo("beacon001");
                            mapFragment.setInfo("beacon001");
                            signInFragment.setDistance(nearBeacon.getDistance());
                            newBeacon = "beacon001";
                            Log.i(TAG, "find right ibeacon001 ");
                        }else if(nearBeacon.getId1().toString().equalsIgnoreCase(ibeacon002_id1)&& nearBeacon.getId2().toString().equalsIgnoreCase(ibeacon002_id2)&& nearBeacon.getId3().toString().equalsIgnoreCase(ibeacon002_id3)){
                            newBeacon = "beacon002";
                            searchFragment.setInfo("beacon002");
                            mapFragment.setInfo("beacon002");
                            signInFragment.setDistance(nearBeacon.getDistance());
                            String readBeacon = "beacon002";
                            SharedPreferences pref = getSharedPreferences("beacon", MODE_PRIVATE);
                            pref.edit().putString("readBeacon", readBeacon).commit();

                            Log.i(TAG, "find right ibeacon002");
                        }else if(nearBeacon.getId1().toString().equalsIgnoreCase(ibeacon003_id1)&& nearBeacon.getId2().toString().equalsIgnoreCase(ibeacon003_id2)&& nearBeacon.getId3().toString().equalsIgnoreCase(ibeacon003_id3)){
                            searchFragment.setInfo("beacon003");
                            mapFragment.setInfo("beacon003");
                            signInFragment.setDistance(nearBeacon.getDistance());
                            newBeacon = "beacon003";
                            Log.i(TAG, "find right ibeacon003");
                        }else {
                            Log.i(TAG, "Can not find right ibeacon ");
                        }

                        if(currentPage.equals("Building") && !currentBeacon.equals(newBeacon)){
                            Fragment frg = null;
                            currentBeacon = newBeacon;
                            frg = getSupportFragmentManager().findFragmentByTag("searchFragment");
                            getSupportFragmentManager().beginTransaction().detach(frg).attach(frg).commit();
                        }else if(currentPage.equals("Paint")){
                            Fragment frg = null;

                            frg = getSupportFragmentManager().findFragmentByTag("signInFragment");
                            getSupportFragmentManager().beginTransaction().detach(frg).attach(frg).commit();
                        }else if(currentPage.equals("Book") && !currentBeacon.equals(newBeacon)){
                            Fragment frg = null;
                            currentBeacon = newBeacon;
                            frg = getSupportFragmentManager().findFragmentByTag("mapFragment");
                            getSupportFragmentManager().beginTransaction().detach(frg).attach(frg).commit();
                        }


                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }



    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {

        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:
                return screenShotable;
            default:
                //return replaceFragment(screenShotable, position)
                currentPage = slideMenuItem.getName();
                setcurrentPage(slideMenuItem.getName());
                if(slideMenuItem.getName().equals("Building")){
                    beaconManager.bind(this);
                    onBeaconServiceConnect();
                    mFragmentMgr.beginTransaction().replace(R.id.frameLay,searchFragment,"searchFragment").addToBackStack(null).commit();
                    String readB = getSharedPreferences("beacon", MODE_PRIVATE)
                            .getString("readBeacon", "");
                    Log.i("Share","Share: "+readB );
                }else if(slideMenuItem.getName().equals("Book")){
                    beaconManager.bind(this);
                    mFragmentMgr.beginTransaction().replace(R.id.frameLay,mapFragment,"mapFragment").addToBackStack(null).commit();
                   // beaconManager.unbind(this);
                }else if(slideMenuItem.getName().equals("Paint")){
                    beaconManager.unbind(this);
                    mFragmentMgr.beginTransaction().replace(R.id.frameLay,signInFragment,"signInFragment").addToBackStack(null).commit();

                }else if(slideMenuItem.getName().equals("Case")){
                    beaconManager.unbind(this);
                    mFragmentMgr.beginTransaction().replace(R.id.frameLay,viewInfoFragment,"viewInfoFragment").addToBackStack(null).commit();
                }
                Log.i("Screen","hello "+slideMenuItem.getName());


                return screenShotable;
        }
    }


    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
    public void setcurrentPage(String page){
        currentPage = page;
    }
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    public void setBeaconID(final String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("ibeacon").document(name);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot ds) {
                CourseInfo courseInfo = ds.toObject(CourseInfo.class);
                if(name=="beacon001"){
                    Log.i("setBeaconID:","beacon001");

                    setIbeacon001_id1(courseInfo.getId1());
                    setIbeacon001_id2(courseInfo.getId2());
                    setIbeacon001_id3(courseInfo.getId3());

                }else if(name=="beacon002"){
                    Log.i("setBeaconID:","beacon002");
                    setIbeacon002_id1(courseInfo.getId1());
                    setIbeacon002_id2(courseInfo.getId2());
                    setIbeacon002_id3(courseInfo.getId3());

                }else if(name=="beacon003"){
                    Log.i("setBeaconID:","beacon003");
                    setIbeacon003_id1(courseInfo.getId1());
                    setIbeacon003_id2(courseInfo.getId2());
                    setIbeacon003_id3(courseInfo.getId3());
                }

            }
        });
    }

    public void setIbeacon001_id1(String ibeacon001_id1) {
        this.ibeacon001_id1 = ibeacon001_id1;
    }

    public void setIbeacon002_id1(String ibeacon002_id1) {
        this.ibeacon002_id1 = ibeacon002_id1;
    }

    public void setIbeacon003_id1(String ibeacon003_id1) {
        this.ibeacon003_id1 = ibeacon003_id1;
    }

    public void setIbeacon001_id2(String ibeacon001_id2) {
        this.ibeacon001_id2 = ibeacon001_id2;
    }

    public void setIbeacon002_id2(String ibeacon002_id2) {
        this.ibeacon002_id2 = ibeacon002_id2;
    }

    public void setIbeacon003_id2(String ibeacon003_id2) {
        this.ibeacon003_id2 = ibeacon003_id2;
    }

    public void setIbeacon001_id3(String ibeacon001_id3) {
        this.ibeacon001_id3 = ibeacon001_id3;
    }

    public void setIbeacon002_id3(String ibeacon002_id3) {
        this.ibeacon002_id3 = ibeacon002_id3;
    }

    public void setIbeacon003_id3(String ibeacon003_id3) {
        this.ibeacon003_id3 = ibeacon003_id3;
    }
}
