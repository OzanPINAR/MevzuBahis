package ege.mevzubahis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ege.mevzubahis.Activities.AboutActivity;
import ege.mevzubahis.Activities.DefaultIntro;
import ege.mevzubahis.Managers.Config;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.createBetButton) Button createBetButton;
  @BindView(R.id.activity_main) RelativeLayout activityMain;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    Thread t = new Thread(new Runnable() {
      @Override public void run() {

        SharedPreferences sharedPreferences =
            getSharedPreferences(Config.FLAG, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(Config.FLAG, true)) {

          startActivity(new Intent(MainActivity.this, DefaultIntro.class));

          SharedPreferences.Editor e = sharedPreferences.edit();

          e.putBoolean(Config.FLAG, false);

          e.apply();
        }
      }
    });
    t.start();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Take appropriate action for each action item click
    switch (item.getItemId()) {

      case R.id.about:
        Intent aboutintent = new Intent(this, AboutActivity.class);
        startActivity(aboutintent);
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @OnClick(R.id.createBetButton) public void onClick() {
    //TODO: create bets activity starts
  }
}
