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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.w3c.dom.Text;

import ege.mevzubahis.Activities.FriendActivity;
import ege.mevzubahis.R;
import java.util.Map;


import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Acer Bilgisayar on 18.4.2017.
 */

public class BetsDialogFragment extends DialogFragment implements View.OnClickListener {

  @BindView(R.id.tv_match) TextView tv_match;
  @BindView(R.id.textView2) TextView textView2;
  @BindView(R.id.textInputLayout) TextInputLayout textInputLayout;
  @BindView(R.id.radioButton4) RadioButton radioButton4;
  @BindView(R.id.radioButton5) RadioButton radioButton5;
  @BindView(R.id.radioButton6) RadioButton radioButton6;
  @BindView(R.id.button2) Button sendButton;

  String matchName;
  String durationValue;
  private DatabaseReference mDatabase;
  private RadioGroup radioGroup;
  private String choice;
  private EditText editText;
  private TextInputLayout coinWrapper;
  public Long coinValue;

  SharedPreferences sharedPreferences;
  private String userID;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.custom_dialog, null);
    matchName = getArguments().getString("betNameInPosition");
    mDatabase = FirebaseDatabase.getInstance().getReference();
    radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
    editText = (EditText) view.findViewById(R.id.editText);
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
              tv_match.setText(value);
              durationValue = (String) map.get("duration").toString();
              textView2.setText("Due to: " + durationValue);

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

  @OnClick({ R.id.radioButton4, R.id.radioButton5, R.id.radioButton6})
  public void onClick(View view) {
    boolean checked = ((RadioButton) view).isChecked();
    switch (view.getId()) {
      case R.id.radioButton4:
        choice = "home";
        Toast.makeText(getApplicationContext(), "choice: Home", Toast.LENGTH_SHORT).show();
        break;
      case R.id.radioButton5:
        choice = "draw";
        Toast.makeText(getApplicationContext(), "choice: Draw", Toast.LENGTH_SHORT).show();
        break;
      case R.id.radioButton6:
        choice = "away";
        Toast.makeText(getApplicationContext(), "choice: Away", Toast.LENGTH_SHORT).show();
        break;

    }
  }


  //SEND button
  @OnClick(R.id.button2)
  public void onClickSendButton(View view){

    String coinAmount = coinWrapper.getEditText().getText().toString();
    Log.e("coinAmount",coinAmount);

    //check if coin amount box is empty
    if(TextUtils.isEmpty(coinAmount)){
      coinWrapper.setError("Don't forget to put some coins");
      return;
    }


    long _coinValue;
    if (coinValue == null) {
      _coinValue = 0;
    }

    else {
      _coinValue = coinValue;
      Log.e("coinValue",Long.toString(_coinValue));
    }

    //coin check from database
    if (Long.parseLong(coinAmount) >= _coinValue) {

      Toast.makeText(getApplicationContext(), "You don't have enough coins!",
          Toast.LENGTH_SHORT).show();
    } else if(radioButton4.isChecked()==false && radioButton5.isChecked()==false && radioButton6.isChecked()==false){

      //radio button check
      Log.e("radiocheck",coinAmount);
      Toast.makeText(getApplicationContext(), "Don't forget to select something!",
              Toast.LENGTH_SHORT).show();

    } else {
      //if everything is ok, proceed

      Intent intent = new Intent(getActivity(), FriendActivity.class);
      intent.putExtra("coin", coinAmount);
      intent.putExtra("choice", choice);
      intent.putExtra("matchname",matchName);
      intent.putExtra("duration",durationValue);
      startActivity(intent);
    }


  }
}
