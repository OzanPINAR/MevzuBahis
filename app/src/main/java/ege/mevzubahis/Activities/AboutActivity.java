package ege.mevzubahis.Activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import java.util.Calendar;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Element adsElement = new Element();
    adsElement.setTitle("Arkadaşlarınızla iddialaşın, makinelerle değil!");

    View aboutPage = new AboutPage(this)
        .isRTL(false)
        .addItem(new Element().setTitle("Versiyon 1.4"))
        .addItem(adsElement)
        .setDescription("MevzuBahis")
        .addGroup("Bize Ulaşın!")
        .addEmail("officialmevzubahis@gmail.com")
        .addFacebook("mevzubahisofficial")
        .addTwitter("mevzubahisOFC")
        .addGitHub("egek92")
        .create();

    setContentView(aboutPage);
  }



}
