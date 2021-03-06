package ege.mevzubahis.Fragments;

import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import ege.mevzubahis.R;
import ege.mevzubahis.Utils.CircleTransform;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private DatabaseReference mDatabase;
  SharedPreferences sharedPreferences;


  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private String urlProfileImg;
  private String urlProfileImg1;
  private String userID;
  SharedPreferences pref;
  private ImageView imgProfile2;
  private ImageView imgProfile3;
  private String username2;

  private String deneme ="dENEME";


  private OnFragmentInteractionListener mListener;

  public StatsFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment StatsFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static StatsFragment newInstance(String param1, String param2) {
    StatsFragment fragment = new StatsFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
    mDatabase = FirebaseDatabase.getInstance().getReference();

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    userID=sharedPreferences.getString("userIDKey",null);

    userID=pref.getString("userIDKey",null);
    Log.e("5userID = ", " " + userID);

    username2=pref.getString("nameKey",null);

    urlProfileImg="https://graph.facebook.com/" + userID+ "/picture?type=large";

  }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
    // Inflate the layout for this fragment
     View view = inflater.inflate(R.layout.fragment_stats, container, false);
     final TextView winText = (TextView) view.findViewById(R.id.winText);
     final TextView lostText = (TextView) view.findViewById(R.id.lostText);
     final TextView ratioText = (TextView) view.findViewById(R.id.ratioText);
     final TextView coinText = (TextView) view.findViewById(R.id.coinText);
     final TextView nameText = (TextView) view.findViewById(R.id.nameText);
     Log.e("user id is: ",userID);

     imgProfile2 = (ImageView) view.findViewById(R.id.img_profile);
     imgProfile3 = (ImageView) view.findViewById(R.id.img_profile1);

       Glide.with(this)
               .load(urlProfileImg)
               .crossFade()
               .thumbnail(0.5f)
               .diskCacheStrategy(DiskCacheStrategy.ALL)
               .into(imgProfile3);

       Glide.with(this)
         .load(urlProfileImg)
         .crossFade()
         .thumbnail(0.5f)
         .bitmapTransform(new CircleTransform(getActivity()))
         .diskCacheStrategy(DiskCacheStrategy.ALL)
         .into(imgProfile2);



     mDatabase.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
         Log.e("onDataChange","a");
         Long winValue = (Long) dataSnapshot.child("win").getValue();
         Long lostValue = (Long) dataSnapshot.child("lost").getValue();
         Long coinValue = (Long) dataSnapshot.child("coin").getValue();
         Log.e("Database ", winValue.toString());
         Log.e("Database ", lostValue.toString());
         Log.e("Database ", coinValue.toString());

         nameText.setText(username2);

         winText.setText(winValue.toString());
         lostText.setText(lostValue.toString());
         float ratioValue = winValue.floatValue()/lostValue.floatValue();
         //check for 0/0 = infinity
         if(lostValue.floatValue()==0){
           ratioText.setText(Float.toString(winValue));
         }else {
           ratioText.setText(Float.toString(ratioValue));
         }
         coinText.setText(coinValue.toString());
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

         //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

       }
     });

    ButterKnife.bind(this, view);
    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  /*@Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }
  }*/

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

}