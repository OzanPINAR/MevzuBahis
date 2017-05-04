package ege.mevzubahis.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
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

import ege.mevzubahis.MainActivity;
import ege.mevzubahis.R;

import static android.support.constraint.R.id.parent;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FriendActivity extends AppCompatActivity {
    private TextView choiceText;
    private TextView coinText;
    private ListView friendList;
    private Button sendButton1;

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
        sendButton1=(Button) findViewById(R.id.sendButton1);
        sendButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMainScreen();
            }
        });


        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, friendList1){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);


                return view;
            }
        };
        friendList.setAdapter(arrayAdapter);

        //friend list check stuff
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView)view;
                check.setChecked(!check.isChecked());
            }
        });


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

                            //writing friend data to listview
                            for(int i=0;i<jsonArray.length();i++){
                                try{
                                    JSONObject oneObject=jsonArray.getJSONObject(i);
                                    String name= oneObject.getString("name");
                                    Log.e("forloopName",name);
                                    friendList1.add(name);
                                }catch(JSONException e){
                                    Log.e("OOps",e.toString());
                                }
                            }

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

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}


