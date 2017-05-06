package ege.mevzubahis.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
  public static String picture;
  private DatabaseReference mDatabase;
  SharedPreferences sharedPreferences;
  private VideoView myVideoView;
  TextView textview5;
  TextView textView6;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    setContentView(R.layout.activity_login);
    myVideoView = (VideoView) findViewById(R.id.videoView);
    Uri uri =Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.back);
    myVideoView.setVideoURI(uri);
    myVideoView.start();

   myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
     @Override
     public void onPrepared(MediaPlayer mediaPlayer) {
       mediaPlayer.setLooping(true);
     }
   });
    LoginButton = (LoginButton) findViewById(R.id.fb_login_button);
    callbackManager = CallbackManager.Factory.create();
    LoginButton.setReadPermissions(Arrays.asList("email", "public_profile","user_friends"));
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    mDatabase = FirebaseDatabase.getInstance().getReference();

    LoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        AccessToken accessToken = loginResult.getAccessToken();
        Profile profile = Profile.getCurrentProfile();

        userId = loginResult.getAccessToken().getUserId();



        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                  @Override
                  public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.v("LoginActivity Response ", response.toString());

                    try {

                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      Name = object.getString("name");
                      editor.putString("nameKey", Name);
                      Log.e("1Name = ", " " + Name);
                      FEmail = object.getString("email");
                      editor.putString("emailKey", FEmail);
                      Log.e("2Email = ", " " + FEmail);
                      editor.putString("userIDKey", userId);
                      picture="https://graph.facebook.com/" + userId+ "/picture?type=large";
                      editor.apply();

                      //database de giriş yapan kullanıcının emailiyle kıyaslama yapmak
                      mDatabase.child("Users").child(userId).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                          Log.e("onDataChange","a");
                          try {
                            String value = (String) dataSnapshot.getValue();
                            Log.e("Database ", value);
                            //eğer giriş yapan kullanıcı daha önceden kayıtlı ise
                            if (value.equals(FEmail)) {
                              Log.e("found user ", FEmail);
                              goMainScreen();

                            }
                            // burası neden çalışmıyor bilmiyorum
                            else  {
                              Log.e("ELSE", FEmail);
                            }
                            //giriş yapan kullanıcı daha önceden kayıtlı değilse
                          }catch(Throwable t){
                            Log.e("trycatchFAIL","b");
                            Log.e("Creating user", FEmail);
                            createNewUser(userId,Name,FEmail,1000,0,0,picture);
                            goMainScreen();
                          }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                          Log.e("onCancelled","");
                        }
                      });

                    } catch (JSONException e) {
                      e.printStackTrace();
                    }
                  }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();


      }

      @Override
      public void onCancel() {
        Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(FacebookException error) {
        Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void createNewUser(String userid,String name,String email,int coin,int win,int lost,String picture){
    mDatabase.child("Users").child(userid).child("name").setValue(name);
    mDatabase.child("Users").child(userid).child("email").setValue(email);
    mDatabase.child("Users").child(userid).child("coin").setValue(coin);
    mDatabase.child("Users").child(userid).child("win").setValue(win);
    mDatabase.child("Users").child(userid).child("lost").setValue(lost);
    mDatabase.child("Users").child(userid).child("picture").setValue(picture);
  }
  private void goMainScreen() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    Log.e("gomainscreen", " ");
    startActivity(intent);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
