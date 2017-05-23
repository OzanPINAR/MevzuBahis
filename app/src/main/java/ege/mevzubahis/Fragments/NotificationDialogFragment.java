package ege.mevzubahis.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ege.mevzubahis.Activities.FriendActivity;
import ege.mevzubahis.MainActivity;
import ege.mevzubahis.R;
import ege.mevzubahis.Utils.BetResult;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.matchText) TextView matchText;
    @BindView(R.id.dueText) TextView dueText;
    @BindView(R.id.CoinAmount1) TextView CoinAmount1;
    @BindView(R.id.radioButtonHome) RadioButton radioButtonHome;
    @BindView(R.id.radioButtonDraw) RadioButton radioButtonDraw;
    @BindView(R.id.radioButtonAway) RadioButton radioButtonAway;
    @BindView(R.id.AcceptButton) Button AcceptButton;
    @BindView(R.id.RejectButton) Button RejectButton;

    String matchName;
    String dealKey;
    private DatabaseReference mDatabase;
    private String choice;
    String coin;
    String totalCoin;

    public Long coinValue;

    SharedPreferences sharedPreferences;
    private String userID;
    private String userName;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificationDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationDialogFragment newInstance(String param1, String param2) {
        NotificationDialogFragment fragment = new NotificationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_dialog, null);
        matchName = getArguments().getString("betNameInPosition");
        dealKey = getArguments().getString("dealKeyInPosition");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userID=sharedPreferences.getString("userIDKey",null);


        mDatabase.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                coinValue = (Long) dataSnapshot.child("coin").getValue();

                Log.e("coin value: ", coinValue.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        });

        mDatabase.child("Deals")
                .child(dealKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            String value = (String) map.get("matchName").toString();
                            matchText.setText(value);
                            String durationValue = (String) map.get("duration").toString();
                            dueText.setText("Due to: " + durationValue);
                            coin = (String) map.get("coin").toString();
                            totalCoin = map.get("totalCoin").toString();
                            CoinAmount1.setText(""+coin);

                        } catch (Throwable t) {
                            Log.e("trycatchFAIL", "b");
                        }
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {

                    }
                });

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({ R.id.radioButtonHome, R.id.radioButtonDraw, R.id.radioButtonAway})
    public void onClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioButtonHome:
                choice = "Home";
                Toast.makeText(getApplicationContext(), "choice: Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonDraw:
                choice = "Draw";
                Toast.makeText(getApplicationContext(), "choice: Draw", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButtonAway:
                choice = "Away";
                Toast.makeText(getApplicationContext(), "choice: Away", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    //ACCEPT button
    @OnClick(R.id.AcceptButton)
    public void onClickAcceptButton(View view){
        dealKey = getArguments().getString("dealKeyInPosition");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName=sharedPreferences.getString("nameKey",null);
        int totCoin = Integer.valueOf(coin)+ Integer.valueOf(totalCoin);

        mDatabase.child("Deals").child(dealKey).child("receiver").child(userName).setValue("accepted");
        mDatabase.child("Deals").child(dealKey).child("totalCoin").setValue(totCoin);
        mDatabase.child("Deals").child(dealKey).child(choice).child(userName).setValue("true");
        BetResult betResult=new BetResult();
        betResult.loseCond(coin);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

        }

    //REJECT button
    @OnClick(R.id.RejectButton)
    public void onClickRejectButton(View view){
        dealKey = getArguments().getString("dealKeyInPosition");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName=sharedPreferences.getString("nameKey",null);

        mDatabase.child("Deals").child(dealKey).child("receiver").child(userName).setValue("rejected");
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }


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
