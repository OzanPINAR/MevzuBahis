package ege.mevzubahis.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ege.mevzubahis.MainActivity;
import ege.mevzubahis.R;
import ege.mevzubahis.Utils.BetResult;

public class FriendActivity extends AppCompatActivity {

    private ListView friendList;
    private Button sendButton1;

    String type;

    String choice;
    String coin;
    String matchName;
    String senderName;
    String duration;

    String socialchoice;
    String socialcoin;
    String socialbetname;
    String socialduration;

    SharedPreferences sharedPreferences;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        final ListView friendList;
        final ArrayList<String> friendList1 = new ArrayList<>();
        final ArrayList<String> checkedFriends=new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        friendList = (ListView) findViewById(R.id.friendList);
        Bundle b = getIntent().getExtras();
        type= b.getString("type");

        friendList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        sendButton1=(Button) findViewById(R.id.sendButton1);
        sendButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checked olan itemleri bul
                SparseBooleanArray checkedItems = friendList.getCheckedItemPositions();
                if (checkedItems != null) {
                    for (int i=0; i<checkedItems.size(); i++) {
                        if (checkedItems.valueAt(i)) {
                            String item = friendList.getAdapter().getItem(checkedItems.keyAt(i)).toString();
                             Log.e("selected items ",item);
                            checkedFriends.add(item);
                            Log.e("arrays",checkedFriends.get(i));
                        }
                    }
                }

                //databasede yeni deal yarat
                if(type.equals("Sport")) {
                    createNewSportDeal(senderName, matchName, coin, choice, checkedFriends, duration);
                }else if(type.equals("Social")){
                    createNewSocialDeal(senderName,socialbetname,socialcoin,socialchoice,checkedFriends,socialduration);
                }
                //notification gönder

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
                check.setChecked(check.isChecked());

            }
        });




        if(b.getString("type").equals("Sport")) {
            choice = b.getString("choice");
            Log.e("Choice", choice);
            coin = b.getString("coin");
            Log.e("Coin:", coin.toString());
            matchName = b.getString("matchname");
            Log.e("MatchName", matchName);
            senderName = sharedPreferences.getString("nameKey", null);
            Log.e("senderName", senderName);
            duration = b.getString("duration");
        }

        Bundle c = getIntent().getExtras();
        if(c.getString("type").equals("Social")) {
            senderName = sharedPreferences.getString("nameKey", null);
            socialbetname = c.getString("socialbetname");
            Log.e("socialbetname", socialbetname);
            socialchoice = c.getString("socialchoice");
            Log.e("socialchoice", socialchoice);
            socialcoin = c.getString("socialcoin");
            Log.e("socialcoin", socialcoin);
            socialduration = c.getString("socialduration");
            Log.e("socialduration", socialduration);
        }

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
    private void createNewSocialDeal(String senderName,String betName, String coin, String choice, ArrayList<String> arrayList, String duration){
        String key= mDatabase.child("Deals").push().getKey();
        Log.e("socialKEY",key);
        if(choice.equals("yes")){
            Log.e("socialchoice",choice);
            Log.e("socialsender",senderName);
            mDatabase.child("Deals").child(key).child("Yes").child(senderName).setValue("true");
        }else if(choice.equals("no")){
            mDatabase.child("Deals").child(key).child("No").child(senderName).setValue("true");
        }

        mDatabase.child("Deals").child(key).child("sender").setValue(senderName);
        mDatabase.child("Deals").child(key).child("matchName").setValue(betName);
        mDatabase.child("Deals").child(key).child("coin").setValue(coin);
        mDatabase.child("Deals").child(key).child("totalCoin").setValue(coin);
        mDatabase.child("Deals").child(key).child("duration").setValue(duration);
        for(int i=0;i<arrayList.size();i++){
            mDatabase.child("Deals").child(key).child("receiver").child(arrayList.get(i)).setValue("onhold");
        }
        mDatabase.child("Deals").child(key).child("type").setValue("Social");
        BetResult betResult= new BetResult();
        betResult.loseCond(coin);

    }
    private void createNewSportDeal(String senderName,String matchName,String coin,String choice,ArrayList<String> arrayList,String duration){
        String key=mDatabase.child("Deals").push().getKey();
        if(choice.equals("home")){
            mDatabase.child("Deals").child(key).child("Home").child(senderName).setValue("true");
        }else if(choice.equals("draw")){
            mDatabase.child("Deals").child(key).child("Draw").child(senderName).setValue("true");
        }else{
            mDatabase.child("Deals").child(key).child("Away").child(senderName).setValue("true");
        }
        mDatabase.child("Deals").child(key).child("sender").setValue(senderName);
        mDatabase.child("Deals").child(key).child("matchName").setValue(matchName);
        mDatabase.child("Deals").child(key).child("coin").setValue(coin);
        mDatabase.child("Deals").child(key).child("totalCoin").setValue(coin);
        mDatabase.child("Deals").child(key).child("duration").setValue(duration);
        for(int i=0;i<arrayList.size();i++){
            mDatabase.child("Deals").child(key).child("receiver").child(arrayList.get(i)).setValue("onhold");
        }
        mDatabase.child("Deals").child(key).child("type").setValue("Sport");
        BetResult betResult= new BetResult();
        betResult.loseCond(coin);
    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}


