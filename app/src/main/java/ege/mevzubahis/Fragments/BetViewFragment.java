package ege.mevzubahis.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ege.mevzubahis.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ozan on 8.05.2017.
 */

public class BetViewFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.tv_match)
    TextView tv_match;
    @BindView(R.id.textView2) TextView textView2;
    @BindView(R.id.bet_sender) TextView betSender;


    String matchName;
    String dealKey;
    private DatabaseReference mDatabase;
    private String choice;
    private TextInputLayout coinWrapper;
    public Long coinValue;

    SharedPreferences sharedPreferences;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bet_view_dialog, null);
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
                            String match = (String) map.get("matchName").toString();
                            tv_match.setText(match);
                            String durationValue = (String) map.get("duration").toString();
                            textView2.setText("Due to: " + durationValue);
                            String sender = (String) map.get("sender").toString();
                            betSender.setText(""+sender);

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


    @Override
    public void onClick(View v) {

    }
}
