package ege.mevzubahis.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ege.mevzubahis.R;

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
    @BindView(R.id.AcceptButton) Button AcceptButton;
    @BindView(R.id.RejectButton) Button RejectButton;

    String matchName;
    private DatabaseReference mDatabase;
    private String choice;
    private TextInputLayout coinWrapper;
    public Long coinValue;

    SharedPreferences sharedPreferences;
    private String userID;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        coinWrapper = (TextInputLayout) view.findViewById(R.id.textInputLayout);
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

        mDatabase.child("Bets")
                .child("Sports")
                .child(matchName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            String value = (String) map.get("matchname").toString();
                            matchText.setText(value);
                            String durationValue = (String) map.get("duration").toString();
                            dueText.setText("Due to: " + durationValue);

                            //coinValue = (Long) dataSnapshot.child("coin").getValue();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onClick(View v) {

    }
}
