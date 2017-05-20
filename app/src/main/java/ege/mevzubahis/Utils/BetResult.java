package ege.mevzubahis.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ege.mevzubahis.Activities.AboutActivity;
import ege.mevzubahis.Activities.MyAdapter;
import ege.mevzubahis.Fragments.HomeFragment;
import ege.mevzubahis.MainActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Acer Bilgisayar on 20.5.2017.
 */

public class BetResult {
    private DatabaseReference reference;
    SharedPreferences sharedPreferences;
    String userName;
    String userID;
    String userCoin;
    String dealKey;

    public void test(){
        Log.e("OZiiZİİZİZİ","deneme");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        reference = FirebaseDatabase.getInstance().getReference();
        userName=sharedPreferences.getString("nameKey", null);
        userID=sharedPreferences.getString("userIDKey",null);


        Query betQuery = reference.child("Bets").child("Sports");
        final Query dealQuery = reference.child("Deals");

        final ArrayList<String> dealKeyList = new ArrayList<>();


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userCoin= dataSnapshot.child("Users").child(userID).child("coin").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



       betQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){

                    if(!child.child("result").getValue().equals("undecided")){
                        if(child.child("result").getValue().equals("Home")){
                            dealQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        Log.e("test","1");
                                        if (child.child("sender").getValue().toString().equals(userName)) {
                                            if(child.child("Home").child(userName).getValue().equals("true")){
                                                Log.e("HOME WINS","sender");
                                                Log.e("usercoin",userCoin);
                                                Long childCount=child.child("Home").getChildrenCount();
                                                Long totalCoin=(Long) child.child("totalCoin").getValue();
                                                Long winCoin= (Long.valueOf(userCoin)+(totalCoin/childCount));
                                                userCoin=winCoin.toString();
                                                Log.e("wincoin",winCoin.toString());
                                                reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                            }
                                            Log.e("test","2");

                                        }

                                        try {

                                            if (child.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                                if(child.child("Home").child(userName).getValue().equals("true")){
                                                    Log.e("Home wins","receiver");
                                                    Log.e("usercoin",userCoin);
                                                    Long childCount=child.child("Home").getChildrenCount();
                                                    Log.e("childcount",childCount.toString());
                                                    Long totalCoin=(Long) child.child("totalCoin").getValue();
                                                    Long winCoin= (Long.valueOf(userCoin)+(totalCoin/childCount));
                                                    userCoin=winCoin.toString();
                                                    Log.e("wincoin",winCoin.toString());
                                                    reference.child("Users").child(userID).child("coin").setValue(winCoin);

                                                }
                                                Log.e("test","3");


                                            }

                                        }
                                        catch (NullPointerException e){
                                            Context context = getApplicationContext();
                                            CharSequence text = "null";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else if(child.child("result").getValue().equals("Draw")){
                            dealQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        Log.e("test","1");
                                        if (child.child("sender").getValue().toString().equals(userName)) {
                                            if(child.child("Draw").child(userName).getValue().equals("true")){
                                                Log.e("DRAW WINS","sender");
                                                Log.e("usercoin",userCoin);
                                                Long childCount=child.child("Draw").getChildrenCount();
                                                Long totalCoin=(Long) child.child("totalCoin").getValue();
                                                Long winCoin= (Long.valueOf(userCoin)+(totalCoin/childCount));
                                                userCoin=winCoin.toString();
                                                Log.e("wincoin",winCoin.toString());
                                                reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                            }
                                            Log.e("test","2");

                                        }

                                        try {

                                            if (child.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                                if(child.child("Draw").child(userName).getValue().equals("true")){
                                                    Log.e("Draw wins","receiver");
                                                    Log.e("usercoin",userCoin);
                                                    Long childCount=child.child("Draw").getChildrenCount();
                                                    Long totalCoin=(Long) child.child("totalCoin").getValue();
                                                    Long winCoin= (Long.valueOf(userCoin)+(totalCoin/childCount));
                                                    userCoin=winCoin.toString();
                                                    Log.e("wincoin",winCoin.toString());
                                                    reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                }
                                                Log.e("test","3");


                                            }

                                        }
                                        catch (NullPointerException e){
                                            Context context = getApplicationContext();
                                            CharSequence text = "null";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else if(child.child("result").getValue().equals("Away")){
                            dealQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        Log.e("test","1");
                                        if (child.child("sender").getValue().toString().equals(userName)) {
                                            if(child.child("Away").child(userName).getValue().equals("true")){
                                                Log.e("AWAY WINS","sender");
                                                Log.e("usercoin",userCoin);
                                                Long childCount=child.child("Away").getChildrenCount();
                                                Long totalCoin=(Long) child.child("totalCoin").getValue();
                                                Long winCoin= (Long.valueOf(userCoin)+(totalCoin/childCount));
                                                userCoin=winCoin.toString();
                                                Log.e("wincoin",winCoin.toString());
                                                reference.child("Users").child(userID).child("coin").setValue(winCoin);

                                            }
                                            Log.e("test","2");

                                        }

                                        try {

                                            if (child.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                                if(child.child("Away").child(userName).getValue().equals("true")){
                                                    Log.e("Away wins","receiver");
                                                    Log.e("usercoin",userCoin);
                                                    Long childCount=child.child("Away").getChildrenCount();
                                                    Long totalCoin=(Long) child.child("totalCoin").getValue();
                                                    Long winCoin= (Long.valueOf(userCoin)+(totalCoin/childCount));
                                                    userCoin=winCoin.toString();
                                                    Log.e("wincoin",winCoin.toString());
                                                    reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                }
                                                Log.e("test","3");


                                            }

                                        }
                                        catch (NullPointerException e){
                                            Context context = getApplicationContext();
                                            CharSequence text = "null";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
}
