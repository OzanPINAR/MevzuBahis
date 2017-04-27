package ege.mevzubahis.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Method;

import ege.mevzubahis.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FriendActivity extends AppCompatActivity {
  private TextView choiceText;
  private TextView coinText;
  private ListView friendList;
  String choice;
  String coin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friend);
    choiceText = (TextView) findViewById(R.id.choiceText);
    coinText = (TextView) findViewById(R.id.coinText);
    friendList = (ListView) findViewById(R.id.friendList);

    Bundle b = getIntent().getExtras();

    choice = b.getString("choice");
    coin = b.getString("coin");

    coinText.setText(coin);
    choiceText.setText(choice);
      AccessToken accessToken = AccessToken.getCurrentAccessToken();
      GraphRequestBatch batch = new GraphRequestBatch(
              GraphRequest.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {
                          @Override
                          public void onCompleted(
                                  JSONArray jsonArray,
                                  GraphResponse response) {
                              // Application code for users friends
                              Log.e("getsData completed" , jsonArray.toString());
                              Log.e("getData" , response.toString());
                              try {
                                  JSONObject jsonObject = response.getJSONObject();
                                  Log.e("deneme3" , jsonObject.toString());
                                  JSONObject summary = jsonObject.getJSONObject("summary");
                                  Log.e("summary total" , summary.getString("total_count"));
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      })

      );
      batch.addCallback(new GraphRequestBatch.Callback() {
          @Override
          public void onBatchCompleted(GraphRequestBatch graphRequests) {
              // Application code for when the batch finishes
          }
      });
      batch.executeAsync();

      Bundle parameters = new Bundle();
      parameters.putString("fields", "id,name,link,picture");
  }
}

