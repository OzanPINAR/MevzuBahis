package ege.mevzubahis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import ege.mevzubahis.MainActivity;
import ege.mevzubahis.Network.Bets;
import ege.mevzubahis.R;

public class BetsActivity extends AppCompatActivity {

  private SectionsPagerAdapter mSectionsPagerAdapter;

  private ViewPager mViewPager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bets);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    mViewPager = (ViewPager) findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(mViewPager);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        goMainScreen();
      }
    });
  }

  private void goMainScreen() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @Override public void onBackPressed() {

    Intent i = new Intent(BetsActivity.this, MainActivity.class);

    startActivity(i);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_bets, menu);
    return true;
  }

  public static class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.match1) TextView match1;
    @BindView(R.id.button2) Button button2;
    @BindView(R.id.button3) Button button3;
    @BindView(R.id.button4) Button button4;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_bets, container, false);

      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference reference = database.getReference("bets");

      reference.addValueEventListener(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {

          Bets msg = dataSnapshot.getValue(Bets.class);
          Log.d("Database:", msg.getResult() + " " + msg.getEvent());

          String msgText = msg.getEvent();

          match1.setText(msgText);
        }

        @Override public void onCancelled(DatabaseError databaseError) {

        }
      });

      ButterKnife.bind(this, rootView);
      return rootView;
    }

    @OnClick({ R.id.button2, R.id.button3, R.id.button4 }) public void onClick(View view) {
      switch (view.getId()) {
        case R.id.button2:
          Toast.makeText(getActivity(), "firebase e yolla", Toast.LENGTH_SHORT).show();
          break;
        case R.id.button3:
          Toast.makeText(getActivity(), "firebase e yolla", Toast.LENGTH_SHORT).show();
          break;
        case R.id.button4:
          Toast.makeText(getActivity(), "firebase e yolla", Toast.LENGTH_SHORT).show();
          break;
      }
    }
  }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {

      return PlaceholderFragment.newInstance(position + 1);
    }

    @Override public int getCount() {

      return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "Popular Sport Events";
        case 1:
          return "Custom Events";
      }
      return null;
    }
  }
}
