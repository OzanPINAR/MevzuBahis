package ege.mevzubahis.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import ege.mevzubahis.Activities.FriendActivity;
import ege.mevzubahis.R;

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

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.custom_dialog, null);

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReference("Bets").child("Sports");

    reference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()){
          String betsItem = child.getKey();

          tv_match.setText(betsItem);

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
        break;
      case R.id.radioButton5:
        break;
      case R.id.radioButton6:
        break;
      case R.id.button2:

        //TODO: coin amount control
        Intent intent = new Intent(getActivity(), FriendActivity.class);
        startActivity(intent);

        break;
    }
  }
}
