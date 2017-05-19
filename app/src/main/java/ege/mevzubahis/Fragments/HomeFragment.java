package ege.mevzubahis.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ege.mevzubahis.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;
    String senderID;
    String senderName;
    String dealKey;
    String matchName;
    String coin;
    String duration;
    String userName;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        senderID = sharedPreferences.getString("userIDKey", null);
        senderName = sharedPreferences.getString("nameKey", null);
        userName = sharedPreferences.getString("nameKey", null);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView fragmentBetsListview;
        final ArrayList<String> betsList = new ArrayList<>();
        final ArrayList<String> dealKeyLis = new ArrayList<>();
        final ArrayAdapter arrayAdapter;

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        fragmentBetsListview = (ListView) rootView.findViewById(R.id.betList);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final ArrayAdapter arrayAdapter;

                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, betsList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
                fragmentBetsListview.setAdapter(arrayAdapter);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference();
                final DatabaseReference dealsRef = database.getReference("Deals");

                Query myQuery = reference.child("Deals");


                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            //buranın üstünde çalışıyorum

                            if (child.child("sender").getValue().toString().equals(userName)) {
                                dealKey = child.getKey();

                                String betsItem = String.valueOf(child.child("matchName").getValue());
                                betsList.add(betsItem);
                                dealKeyLis.add(dealKey);
                                arrayAdapter.notifyDataSetChanged();
                            }

                            try {

                                if (child.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                    dealKey = child.getKey();

                                    String betsItem = String.valueOf(child.child("matchName").getValue());
                                    betsList.add(betsItem);
                                    dealKeyLis.add(dealKey);
                                    arrayAdapter.notifyDataSetChanged();
                                }

                            }
                            catch (NullPointerException e){
                                Context context = getApplicationContext();
                                CharSequence text = "null";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                swipeLayout.setRefreshing(false);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();
        final DatabaseReference dealsRef = database.getReference("Deals");
        fragmentBetsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String betNameInPosition = fragmentBetsListview.getItemAtPosition(position).toString();
                final String dealKeyInPosition = dealKeyLis.get(position);
                reference.child(betNameInPosition).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        //String duration = (String) map.get("duration").toString();

                        Bundle args = new Bundle();
                        args.putString("betNameInPosition", betNameInPosition);
                        args.putString("dealKeyInPosition", dealKeyInPosition);

                        Log.e("betNameInPos", betNameInPosition);
                        Log.e("dealKeyInPos", dealKeyInPosition);

            /*FragmentManager myManager = getFragmentManager();
            BetViewFragment betsDialog=new BetViewFragment();
            betsDialog.setArguments(args);
            betsDialog.show(myManager,"BetViewFragment");*/
                        dealsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                senderName = dataSnapshot.child(dealKeyInPosition).child("sender").getValue().toString();
                                matchName = dataSnapshot.child(dealKeyInPosition).child("matchName").getValue().toString();
                                coin = dataSnapshot.child(dealKeyInPosition).child("coin").getValue().toString();
                                duration = dataSnapshot.child(dealKeyInPosition).child("duration").getValue().toString();
                                Log.e("matchname: ", matchName);
                                Log.e("sender", senderName);

                                SweetAlertDialog sd = new SweetAlertDialog(getContext());
                                sd.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                sd.setTitleText("" + matchName);
                                sd.setContentText("Due to:" + duration + "\n\nSent by:" + senderName + "\n\nCoin:" + coin);
                                sd.setCancelable(true);
                                sd.setCanceledOnTouchOutside(true);
                                sd.show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

 /* @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }
  }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
