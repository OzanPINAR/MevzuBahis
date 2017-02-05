package ege.mevzubahis.Activities;

/**
 * Created by ege on 5.02.2017.
 */

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import ege.mevzubahis.Fragments.IntroSlider;
import ege.mevzubahis.MainActivity;
import ege.mevzubahis.R;

public final class DefaultIntro extends AppIntro {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Down here, we add the xml layouts into sample slide inflater.
    addSlide(IntroSlider.newInstance(R.layout.intro));
    addSlide(IntroSlider.newInstance(R.layout.intro2));
    addSlide(IntroSlider.newInstance(R.layout.intro3));
    addSlide(IntroSlider.newInstance(R.layout.intro4));


    showStatusBar(true);

    setDepthAnimation();

  }



  @Override
  public void onDonePressed(Fragment currentFragment) {
    super.onDonePressed(currentFragment);

    loadMainActivity();
  }

  private void loadMainActivity(){
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  @Override
  public void onSkipPressed(Fragment currentFragment) {
    super.onSkipPressed(currentFragment);
    loadMainActivity();
    Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
  }

  public void getStarted(View v){
    loadMainActivity();
  }
}