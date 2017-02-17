package ege.mevzubahis;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import ege.mevzubahis.Activities.AboutActivity;
import ege.mevzubahis.Activities.DefaultIntro;
import ege.mevzubahis.Activities.LoginActivity;
import ege.mevzubahis.Fragments.GetCoinFragment;
import ege.mevzubahis.Fragments.HomeFragment;
import ege.mevzubahis.Fragments.NotificationsFragment;
import ege.mevzubahis.Fragments.SettingsFragment;
import ege.mevzubahis.Fragments.StatsFragment;
import ege.mevzubahis.Managers.Config;
import ege.mevzubahis.Utils.CircleTransform;

public class MainActivity extends AppCompatActivity {

  private NavigationView navigationView;
  private DrawerLayout drawer;
  private View navHeader;
  private ImageView imgNavHeaderBg, imgProfile;
  private TextView txtName, txtWebsite;
  private Toolbar toolbar;
  private FloatingActionButton fab;


  private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
  private static final String urlProfileImg = "https://avatars3.githubusercontent.com/u/10755037?v=3&s=460";


  public static int navItemIndex = 0;


  private static final String TAG_HOME = "home";
  private static final String TAG_PHOTOS = "photos";
  private static final String TAG_MOVIES = "movies";
  private static final String TAG_NOTIFICATIONS = "notifications";
  private static final String TAG_SETTINGS = "settings";
  public static String CURRENT_TAG = TAG_HOME;


  private String[] activityTitles;


  private boolean shouldLoadHomeFragOnBackPress = true;
  private Handler mHandler;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    setContentView(R.layout.activity_main);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Thread t = new Thread(new Runnable() {
      @Override public void run() {

        SharedPreferences sharedPreferences =
                getSharedPreferences(Config.FLAG, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(Config.FLAG, true)) {

          startActivity(new Intent(MainActivity.this, DefaultIntro.class));

          SharedPreferences.Editor e = sharedPreferences.edit();

          e.putBoolean(Config.FLAG, false);

          e.apply();
        }
      }
    });
    t.start();

    mHandler = new Handler();

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    fab = (FloatingActionButton) findViewById(R.id.fab);


    navHeader = navigationView.getHeaderView(0);
    txtName = (TextView) navHeader.findViewById(R.id.name);
    txtWebsite = (TextView) navHeader.findViewById(R.id.website);
    imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
    imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


    activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Buraya createNewBetActivity gelcek", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }
    });

    // load nav menu header data
    loadNavHeader();

    // initializing navigation menu
    setUpNavigationView();

    if (savedInstanceState == null) {
      navItemIndex = 0;
      CURRENT_TAG = TAG_HOME;
      loadHomeFragment();
    }

    if(AccessToken.getCurrentAccessToken()==null){
      goLoginScreen();
    }
  }

  private void goLoginScreen() {
    Intent intent = new Intent(this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }
  public void logout(View view){
    LoginManager.getInstance().logOut();
    goLoginScreen();
  }


  private void loadNavHeader() {

    txtName.setText("Ege Kuzubasioglu");
    txtWebsite.setText("example@example.com");


    Glide.with(this).load(urlNavHeaderBg)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgNavHeaderBg);


    Glide.with(this).load(urlProfileImg)
            .crossFade()
            .thumbnail(0.5f)
            .bitmapTransform(new CircleTransform(this))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgProfile);


    navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
  }


  private void loadHomeFragment() {

    selectNavMenu();


    setToolbarTitle();


    if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
      drawer.closeDrawers();


      toggleFab();
      return;
    }


    Runnable mPendingRunnable = new Runnable() {
      @Override
      public void run() {

        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
      }
    };

    if (mPendingRunnable != null) {
      mHandler.post(mPendingRunnable);
    }


    toggleFab();


    drawer.closeDrawers();

    // refresh toolbar menu
    invalidateOptionsMenu();
  }

  private Fragment getHomeFragment() {
    switch (navItemIndex) {
      case 0:
        // home
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
      case 1:
        // sta
        StatsFragment statsFragment = new StatsFragment();
        return statsFragment;
      case 2:
        // coins
        GetCoinFragment getcoinFragment = new GetCoinFragment();
        return getcoinFragment;
      case 3:
        // notifications fragment
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        return notificationsFragment;

      case 4:
        // settings fragment
        SettingsFragment settingsFragment = new SettingsFragment();
        return settingsFragment;
      default:
        return new HomeFragment();
    }
  }

  private void setToolbarTitle() {
    getSupportActionBar().setTitle(activityTitles[navItemIndex]);
  }

  private void selectNavMenu() {
    navigationView.getMenu().getItem(navItemIndex).setChecked(true);
  }

  private void setUpNavigationView() {
    //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

      // This method will trigger on item Click of navigation menu
      @Override
      public boolean onNavigationItemSelected(MenuItem menuItem) {

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
          //Replacing the main content with ContentFragment Which is our Inbox View;
          case R.id.nav_home:
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            break;
          case R.id.nav_photos:
            navItemIndex = 1;
            CURRENT_TAG = TAG_PHOTOS;
            break;
          case R.id.nav_movies:
            navItemIndex = 2;
            CURRENT_TAG = TAG_MOVIES;
            break;
          case R.id.nav_notifications:
            navItemIndex = 3;
            CURRENT_TAG = TAG_NOTIFICATIONS;
            break;
          case R.id.nav_settings:
            navItemIndex = 4;
            CURRENT_TAG = TAG_SETTINGS;
            break;
          case R.id.nav_about_us:
            // launch new intent instead of loading fragment
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            drawer.closeDrawers();
            return true;
          case R.id.nav_privacy_policy:
            // launch new intent instead of loading fragment
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            drawer.closeDrawers();
            return true;
          default:
            navItemIndex = 0;
        }

        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) {
          menuItem.setChecked(false);
        } else {
          menuItem.setChecked(true);
        }
        menuItem.setChecked(true);

        loadHomeFragment();

        return true;
      }
    });


    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

      @Override
      public void onDrawerClosed(View drawerView) {
        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
        super.onDrawerClosed(drawerView);
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
        super.onDrawerOpened(drawerView);
      }
    };

    //Setting the actionbarToggle to drawer layout
    drawer.setDrawerListener(actionBarDrawerToggle);

    //calling sync state is necessary or else your hamburger icon wont show up
    actionBarDrawerToggle.syncState();
  }

  @Override
  public void onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawers();
      return;
    }

    // This code loads home fragment when back key is pressed
    // when user is in other fragment than home
    if (shouldLoadHomeFragOnBackPress) {
      // checking if user is on other navigation menu
      // rather than home
      if (navItemIndex != 0) {
        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;
        loadHomeFragment();
        return;
      }
    }

    super.onBackPressed();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.

    // show menu only when home fragment is selected
    if (navItemIndex == 0) {
      getMenuInflater().inflate(R.menu.main, menu);
    }

    // when fragment is notifications, load the menu created for notifications
    if (navItemIndex == 3) {
      getMenuInflater().inflate(R.menu.notifications, menu);
    }
    return true;
  }



  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_logout) {
      Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
      logout();
      return true;
    }

    // user is in notifications fragment
    // and selected 'Mark all as Read'
    if (id == R.id.action_mark_all_read) {
      Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
    }

    // user is in notifications fragment
    // and selected 'Clear All'
    if (id == R.id.action_clear_notifications) {
      Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
    }

    return super.onOptionsItemSelected(item);
  }

  private void logout() {
    LoginManager.getInstance().logOut();
    goLoginScreen();
  }

  // show or hide the fab
  private void toggleFab() {
    if (navItemIndex == 0)
      fab.show();
    else
      fab.hide();
  }



}