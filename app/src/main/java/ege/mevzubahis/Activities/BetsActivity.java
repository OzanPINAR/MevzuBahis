package ege.mevzubahis.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import ege.mevzubahis.MainActivity;
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

  private void showDialog(){
      final Dialog mydialog=new Dialog(this);
      mydialog.setTitle("Enter Bet");
      mydialog.setContentView(R.layout.custom_dialog);
      TextView matchName=(TextView)mydialog.findViewById(R.id.matchName);
      TextView duration=(TextView)mydialog.findViewById(R.id.duration);
      TextView dbDuration=(TextView)mydialog.findViewById(R.id.databaseDuration);
      TextView coin=(TextView)mydialog.findViewById(R.id.coin);
      EditText editText=(EditText) mydialog.findViewById(R.id.editText);
      RadioButton homeWins=(RadioButton)mydialog.findViewById(R.id.homeWins);
      RadioButton draw=(RadioButton)mydialog.findViewById(R.id.draw);
      RadioButton awayWins=(RadioButton)mydialog.findViewById(R.id.awayWins);
      Button sendbet=(Button)mydialog.findViewById(R.id.button2);
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



  public static class SportFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public SportFragment() {
    }



    public static SportFragment newInstance(int sectionNumber) {
      SportFragment fragment = new SportFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }




    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

      View rootView = inflater.inflate(R.layout.fragment_bets, container, false);

      final ListView fragmentBetsListview;
      final ArrayList<String> betsList = new ArrayList<>();
      final ArrayAdapter arrayAdapter;

      fragmentBetsListview = (ListView) rootView.findViewById(R.id.ListviewBerkay);

      arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, betsList){
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          View view =super.getView(position, convertView, parent);

          TextView textView=(TextView) view.findViewById(android.R.id.text1);
          textView.setTextColor(Color.BLACK);

          return view;
        }
      };

      fragmentBetsListview.setAdapter(arrayAdapter);

      final FirebaseDatabase database = FirebaseDatabase.getInstance();
      final DatabaseReference reference = database.getReference("Bets").child("Sports");

      reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          for (DataSnapshot child : dataSnapshot.getChildren()){
            String betsItem = child.getKey();
            Log.e("SportActivity", betsItem);
            betsList.add(betsItem);
            arrayAdapter.notifyDataSetChanged();
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
      });

      fragmentBetsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          String quizNameInPosition = fragmentBetsListview.getItemAtPosition(position).toString();
          reference.child(quizNameInPosition).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
              String duration = (String) map.get("duration");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          });
        }
      });

      ButterKnife.bind(this, rootView);
      return rootView;
    }

  }


    public static class SocialFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public SocialFragment() {
        }

        public static SocialFragment newInstance(int sectionNumber) {
            SocialFragment fragment = new SocialFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {

            View rootView1 = inflater.inflate(R.layout.custom_bets, container, false);

            final ListView fragmentBetsListview1;
            final ArrayList<String> betsList1 = new ArrayList<>();
            final ArrayAdapter arrayAdapter1;

            fragmentBetsListview1 = (ListView) rootView1.findViewById(R.id.custom_listview);

            arrayAdapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, betsList1){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView textView=(TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);

                    return view;
                }
            };

            fragmentBetsListview1.setAdapter(arrayAdapter1);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference reference1 = database.getReference("Bets").child("Social");

            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String betsItem = child.getKey();
                        Log.e("SocialActivity", betsItem);
                        betsList1.add(betsItem);
                        arrayAdapter1.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            fragmentBetsListview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String quizNameInPosition = fragmentBetsListview1.getItemAtPosition(position).toString();
                    reference1.child(quizNameInPosition).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            String duration = (String) map.get("duration");
                            Toast.makeText(getActivity(), duration , Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

            ButterKnife.bind(this, rootView1);
            return rootView1;
        }
    }

  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
        Fragment fragment =null;
        switch (position) {
            case 0:
                fragment = SportFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = SocialFragment.newInstance(position + 1);
                break;
                     }
        return fragment;


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