package ege.mevzubahis.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ege.mevzubahis.R;
import ege.mevzubahis.Utils.BetCard;
import ege.mevzubahis.Utils.BetResult;
import ege.mevzubahis.Utils.CustomCardAdapter;
import ege.mevzubahis.Utils.CustomCardAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;
import static ege.mevzubahis.R.layout.bet_cardview;

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

    public TextView durationInfoText;



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

        BetResult betResult= new BetResult();
        betResult.winCond();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView fragmentBetsListview;
        final ArrayList<String> betsList = new ArrayList<>();
        final ArrayList<String> dealKeyLis = new ArrayList<>();

        //final TextView durationInfoText = (TextView) inflater.inflate(R.layout.bet_cardview,container,false).findViewById(R.id.duration);
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        fragmentBetsListview = (ListView) rootView.findViewById(R.id.betList);

        final ArrayList<BetCard> list = new ArrayList<>();


        final CustomCardAdapter adapter = new CustomCardAdapter(getApplicationContext() ,R.layout.bet_cardview, list);
        fragmentBetsListview.setAdapter(adapter);

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
                adapter.clear();
                adapter.notifyDataSetChanged();
                arrayAdapter.notifyDataSetChanged();
                //fragmentBetsListview.setAdapter(arrayAdapter);
                fragmentBetsListview.setAdapter(adapter);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference();
                final DatabaseReference dealsRef = database.getReference("Deals");

                Query myQuery = reference.child("Deals");


                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TextView durationInfoText = (TextView) inflater.inflate(R.layout.bet_cardview,container,false).findViewById(R.id.duration);
                        for (DataSnapshot child : dataSnapshot.getChildren()) {


                            if (child.child("sender").getValue().toString().equals(userName)) {
                                dealKey = child.getKey();

                                String betsItem = String.valueOf(child.child("matchName").getValue());
                                String duration = child.child("duration").getValue().toString();
                                String coin = child.child("totalCoin").getValue().toString();
                                String sendername =child.child("sender").getValue().toString();
                                if(betsItem.equals("BJK-TS")) {
                                    list.add(new BetCard("drawable://" + R.drawable.bjkts, betsItem,duration,coin,sendername));
                                }else if(betsItem.equals("FB-GS")){
                                    list.add(new BetCard("drawable://" + R.drawable.fbgs, betsItem,duration,coin,sendername));
                                }else if(betsItem.equals("MANU-RM")){
                                    list.add(new BetCard("drawable://" + R.drawable.manurm, betsItem,duration,coin,sendername));
                                }else {
                                    list.add(new BetCard("drawawble://" +R.drawable.manurm, betsItem,duration,coin,sendername));
                                }
                                betsList.add(betsItem);
                                dealKeyLis.add(dealKey);
                                Log.e("DURATION",child.child("duration").getValue().toString());

                                //durationInfoText.setText("Bitiş Süresi: ");
                                adapter.notifyDataSetChanged();
                                arrayAdapter.notifyDataSetChanged();
                            }

                            try {

                                if (child.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                    dealKey = child.getKey();

                                    String betsItem = String.valueOf(child.child("matchName").getValue());
                                    String duration = child.child("duration").getValue().toString();
                                    String coin = child.child("totalCoin").getValue().toString();
                                    String sendername =child.child("sender").getValue().toString();
                                    if(betsItem.equals("BJK-TS")) {
                                        list.add(new BetCard("drawable://" + R.drawable.bjkts, betsItem,duration,coin,sendername));
                                    }else if(betsItem.equals("FB-GS")){
                                        list.add(new BetCard("drawable://" + R.drawable.fbgs, betsItem,duration,coin,sendername));
                                    }else if(betsItem.equals("MANU-RM")){
                                        list.add(new BetCard("drawable://" + R.drawable.manurm, betsItem,duration,coin,sendername));
                                    }else{
                                        list.add(new BetCard("drawable://"+ R.drawable.manurm, betsItem, duration,coin,sendername));
                                    }
                                    betsList.add(betsItem);
                                    dealKeyLis.add(dealKey);
                                    Log.e("DURATION",child.child("Deals").child(dealKey).child("duration").getValue().toString());
                                    durationInfoText.setText("Bitiş Süresi: ");
                                    adapter.notifyDataSetChanged();
                                    arrayAdapter.notifyDataSetChanged();
                                }

                            }
                            catch (NullPointerException e){
                                Context context = getApplicationContext();
                                CharSequence text = "null";
                                int duration = Toast.LENGTH_SHORT;

                                /*Toast toast = Toast.makeText(context, text, duration);
                                toast.show();*/
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
