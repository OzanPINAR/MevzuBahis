package ege.mevzubahis.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;

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
      final ListView friendList;
      final ArrayList<String> friendList1 = new ArrayList<>();
      final ArrayAdapter arrayAdapter;

    coinText = (TextView) findViewById(R.id.coinText);
    friendList = (ListView) findViewById(R.id.friendList);

      arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friendList1){
          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
              View view =super.getView(position, convertView, parent);

              TextView textView=(TextView) view.findViewById(android.R.id.text1);
              textView.setTextColor(Color.BLACK);


              return view;
          }
      };
      friendList.setAdapter(arrayAdapter);



      Bundle b = getIntent().getExtras();

    choice = b.getString("choice");
    coin = b.getString("coin");

    //coinText.setText(coin);
    //choiceText.setText(choice);
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
                                      Log.e("deneme3", jsonObject.toString());
                                      JSONObject summary = jsonObject.getJSONObject("summary");
                                      Log.e("summary total", summary.getString("total_count"));

                                      friendList1.add(jsonArray.toString());
                                      arrayAdapter.notifyDataSetChanged();
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

