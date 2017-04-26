package ege.mevzubahis.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import ege.mevzubahis.R;

public class FriendActivity extends AppCompatActivity {
  private TextView choiceText;
  private TextView coinText;
  String choice;
  String coin;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friend);
    choiceText=(TextView)findViewById(R.id.choiceText);
    coinText=(TextView)findViewById(R.id.coinText);

    Bundle b =getIntent().getExtras();

    choice=b.getString("choice");
    coin=b.getString("coin");

    coinText.setText(coin);
    choiceText.setText(choice);
  }
}
