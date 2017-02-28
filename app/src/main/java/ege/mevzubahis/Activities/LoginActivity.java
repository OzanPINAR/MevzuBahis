package ege.mevzubahis.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.haha.perflib.Main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import ege.mevzubahis.MainActivity;
import ege.mevzubahis.R;

public class LoginActivity extends AppCompatActivity {
  LoginButton LoginButton;
  CallbackManager callbackManager;
  public static String Name;
  public static String FEmail;
  public static String userId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    setContentView(R.layout.activity_login);
    LoginButton = (LoginButton) findViewById(R.id.fb_login_button);
    callbackManager = CallbackManager.Factory.create();
    LoginButton.setReadPermissions(Arrays.asList("email"));

    LoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        Profile profile = Profile.getCurrentProfile();

        userId = loginResult.getAccessToken().getUserId();


        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
              @Override public void onCompleted(JSONObject object, GraphResponse response) {
                Log.v("LoginActivity Response ", response.toString());

                try {

                  Name = object.getString("name");
                  Log.v("Name = ", " " + Name);
                  FEmail = object.getString("email");
                  Log.v("Email = ", " " + FEmail);
                  MainActivity.txtName.setText(Name);
                  MainActivity.txtWebsite.setText(FEmail);

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
        goMainScreen();
      }

      @Override public void onCancel() {
        Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
      }

      @Override public void onError(FacebookException error) {
        Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void goMainScreen() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
