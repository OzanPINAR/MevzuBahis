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
import java.util.HashMap;

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
    String ucoin;
    String dealKey;
    boolean checkFlag=false;

    public void winCond(){
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
                for(final DataSnapshot child : dataSnapshot.getChildren()){
                    if(!child.child("result").getValue().equals("undecided")){
                        if(child.child("result").getValue().equals("Home")){
                            Log.e("matchname",child.child("matchname").getValue().toString());
                            dealQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child2 : dataSnapshot.getChildren()) {
                                        if (child2.child("sender").getValue().toString().equals(userName)) {
                                            if(child2.child("Home").child(userName).getValue()!=null && child2.child("Home").child(userName).getValue().equals("true")){
                                                if(child.child("matchname").getValue().equals(child2.child("matchName").getValue().toString())) {

                                                    reference.child("Deals").child(child2.getKey()).child("Home").child(userName).setValue("paid");
                                                    Log.e("matchName", child2.child("matchName").getValue().toString());
                                                    Log.e("HOME WINS", "sender");
                                                    Log.e("usercoin", userCoin);
                                                    Long childCount = child2.child("Home").getChildrenCount();
                                                    String totalCoin = (String) child2.child("totalCoin").getValue();
                                                    Long winCoin = (Long.valueOf(userCoin) + (Long.valueOf(totalCoin) / childCount));
                                                    userCoin = winCoin.toString();
                                                    Log.e("wincoin", winCoin.toString());
                                                    reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                }
                                            }
                                        }
                                        try {
                                            if (child2.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                                if(child2.child("Home").child(userName).getValue().equals("true")){
                                                    if(child.child("matchname").getValue().equals(child2.child("matchName").getValue().toString())) {

                                                        reference.child("Deals").child(child2.getKey()).child("Home").child(userName).setValue("paid");
                                                        Log.e("Home wins", "receiver");
                                                        Log.e("usercoin", userCoin);
                                                        Long childCount = child2.child("Home").getChildrenCount();
                                                        Log.e("childcount", childCount.toString());
                                                        Long totalCoin = (Long) child2.child("totalCoin").getValue();
                                                        Long winCoin = (Long.valueOf(userCoin) + (totalCoin / childCount));
                                                        userCoin = winCoin.toString();
                                                        Log.e("wincoin", winCoin.toString());
                                                        reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                    }
                                                }
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
                                    for (DataSnapshot child2 : dataSnapshot.getChildren()) {
                                        if (child2.child("sender").getValue().toString().equals(userName)) {
                                            if(child2.child("Draw").child(userName).getValue()!=null &&child2.child("Draw").child(userName).getValue().equals("true")){
                                                if(child.child("matchname").getValue().equals(child2.child("matchName").getValue().toString())) {

                                                    reference.child("Deals").child(child2.getKey()).child("Draw").child(userName).setValue("paid");
                                                    Log.e("DRAW WINS", "sender");
                                                    Log.e("usercoin", userCoin);
                                                    Long childCount = child2.child("Draw").getChildrenCount();
                                                    String totalCoin = child2.child("totalCoin").getValue().toString();
                                                    Long totalCoin2 = Long.valueOf(totalCoin);
                                                    Long winCoin = (Long.valueOf(userCoin) + (totalCoin2 / childCount));
                                                    userCoin = winCoin.toString();
                                                    Log.e("wincoin", winCoin.toString());
                                                    reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                }
                                            }
                                        }
                                        try {
                                            if (child2.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                                if(child2.child("Draw").child(userName).getValue().equals("true")){
                                                    if(child.child("matchname").getValue().equals(child2.child("matchName").getValue().toString())) {

                                                        reference.child("Deals").child(child2.getKey()).child("Draw").child(userName).setValue("paid");
                                                        Log.e("Draw wins", "receiver");
                                                        Log.e("usercoin", userCoin);
                                                        Long childCount = child2.child("Draw").getChildrenCount();
                                                        Long totalCoin = (Long) child2.child("totalCoin").getValue();
                                                        Long winCoin = (Long.valueOf(userCoin) + (totalCoin / childCount));
                                                        userCoin = winCoin.toString();
                                                        Log.e("wincoin", winCoin.toString());
                                                        reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                    }
                                                }
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
                                    for (DataSnapshot child2 : dataSnapshot.getChildren()) {
                                        if (child2.child("sender").getValue().toString().equals(userName)) {
                                            if(child2.child("Away").child(userName).getValue()!=null&&child2.child("Away").child(userName).getValue().equals("true")){
                                                if(child.child("matchname").getValue().equals(child2.child("matchName").getValue().toString())) {

                                                    reference.child("Deals").child(child2.getKey()).child("Away").child(userName).setValue("paid");
                                                    Log.e("AWAY WINS", "sender");
                                                    Log.e("usercoin", userCoin);
                                                    Long childCount = child2.child("Away").getChildrenCount();
                                                    String totalCoin = child2.child("totalCoin").getValue().toString();
                                                    Long totalCoin2 = Long.valueOf(totalCoin);
                                                    Long winCoin = (Long.valueOf(userCoin) + (totalCoin2 / childCount));
                                                    userCoin = winCoin.toString();
                                                    Log.e("wincoin", winCoin.toString());
                                                    reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                }
                                            }
                                        }
                                        try {
                                            if (child2.child("receiver").child(userName).getValue().toString().equals("accepted")) {
                                                if(child2.child("Away").child(userName).getValue().equals("true")){
                                                    if(child.child("matchname").getValue().equals(child2.child("matchName").getValue().toString())) {

                                                        reference.child("Deals").child(child2.getKey()).child("Away").child(userName).setValue("paid");
                                                        Log.e("Away wins", "receiver");
                                                        Log.e("usercoin", userCoin);
                                                        Long childCount = child2.child("Away").getChildrenCount();
                                                        Long totalCoin = (Long) child2.child("totalCoin").getValue();
                                                        Long winCoin = (Long.valueOf(userCoin) + (totalCoin / childCount));
                                                        userCoin = winCoin.toString();
                                                        Log.e("wincoin", winCoin.toString());
                                                        reference.child("Users").child(userID).child("coin").setValue(winCoin);
                                                    }
                                                }
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
    public void loseCond(final String Coin){


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        reference = FirebaseDatabase.getInstance().getReference();
        userName=sharedPreferences.getString("nameKey", null);
        userID=sharedPreferences.getString("userIDKey",null);
        Log.e("kontrol","amac");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ucoin= dataSnapshot.child("Users").child(userID).child("coin").getValue().toString();
                Log.e("userCoinLOSE",ucoin);
                if(Long.valueOf(ucoin)!=null && Long.valueOf(Coin)!=null) {
                    Long value = (Long.valueOf(ucoin) - Long.valueOf(Coin));
                    Log.e("losevalue", value.toString());
                    reference.child("Users").child(userID).child("coin").setValue(value);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void paid(boolean checkFlag, String child){
        if(checkFlag){
            //reference
        }
    }
}
