package ege.mevzubahis.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
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

import com.bumptech.glide.load.engine.Resource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.AbstractList;
import java.util.Map;

import ege.mevzubahis.Activities.FriendActivity;
import ege.mevzubahis.R;
import okhttp3.internal.framed.Variant;

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
  private DatabaseReference mDatabase;
  private RadioGroup radioGroup;
  private String choice;
  private EditText editText;
  private TextInputLayout coinWrapper;


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.custom_dialog, null);
    matchName =getArguments().getString("betNameInPosition");
    mDatabase = FirebaseDatabase.getInstance().getReference();
    radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
    editText=(EditText)view.findViewById(R.id.editText);
    coinWrapper=(TextInputLayout)view.findViewById(R.id.textInputLayout);


    mDatabase.child("Bets").child("Sports").child(matchName).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        try {
          Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
          String value = (String) map.get("matchname").toString();
          tv_match.setText(value);
          String durationValue = (String)map.get("duration").toString();
          textView2.setText("Due to:"+durationValue);
        }catch (Throwable t){
          Log.e("trycatchFAIL","b");
        }

      }


      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    ButterKnife.bind(this, view);
    return view;


  }

  @OnClick({ R.id.radioButton4, R.id.radioButton5, R.id.radioButton6, R.id.button2 })
  public void onClick(View view) {

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
      case R.id.button2:

        //TODO: coin amount control

        String coinAmount = coinWrapper.getEditText().getText().toString();

        Intent intent = new Intent(getActivity(), FriendActivity.class);
        intent.putExtra("coin",coinAmount);
        intent.putExtra("choice",choice);
        startActivity(intent);

        break;

    }
  }

}
