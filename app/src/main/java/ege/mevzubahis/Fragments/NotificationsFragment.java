package ege.mevzubahis.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ege.mevzubahis.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
  SharedPreferences sharedPreferences;
  String senderID;
  String senderName;
  String userName;
  String dealKey;
  String matchName;
  String coin;
  String duration;


  private OnFragmentInteractionListener mListener;

  public NotificationsFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment NotificationsFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static NotificationsFragment newInstance(String param1, String param2) {
    NotificationsFragment fragment = new NotificationsFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    senderID=sharedPreferences.getString("userIDKey",null);
    senderName=sharedPreferences.getString("nameKey",null);
    userName=sharedPreferences.getString("nameKey",null);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);


    final ListView fragmentNotifListview;
    final ArrayList<String> NotifList = new ArrayList<>();
    final ArrayList<String> dealKeyLis= new ArrayList<>();
    final ArrayAdapter arrayAdapter;

    fragmentNotifListview = (ListView) rootView.findViewById(R.id.NotificationList);
    final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);


    swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        final ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, NotifList) {
          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextColor(Color.BLACK);

            return view;
          }
        };
        //arrayAdapter.clear();
        NotifList.clear();
        dealKeyLis.clear();
       // arrayAdapter.notifyDataSetChanged();
        fragmentNotifListview.setAdapter(arrayAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference();
        final DatabaseReference dealsRef = database.getReference("Deals");

        Query myQuery= reference.child("Deals");

        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {

              if(child.child("receiver").child(senderName).getValue() != null){

                if(child.child("receiver").child(senderName).getValue().toString().equals("onhold")){
                  String receiverItem = String.valueOf(child.child("matchName").getValue());
                  dealKey = child.getKey();

                  NotifList.add(receiverItem);
                  dealKeyLis.add(dealKey);
                  arrayAdapter.notifyDataSetChanged();
                }

              }
            }

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });

        swipeLayout.setRefreshing(false);
      }
    });
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference reference = database.getReference();
    final DatabaseReference dealsRef = database.getReference("Deals");


    fragmentNotifListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String betNameInPosition = fragmentNotifListview.getItemAtPosition(position).toString();
        final String dealKeyInPosition = dealKeyLis.get(position);
        reference.child(betNameInPosition).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
            //String duration = (String) map.get("duration").toString();

            Bundle args =new Bundle();
            args.putString("betNameInPosition",betNameInPosition);
            args.putString("dealKeyInPosition",dealKeyInPosition);

            FragmentManager myManager=getFragmentManager();
            NotificationDialogFragment NotificationDialog=new NotificationDialogFragment();
            NotificationDialog.setArguments(args);
            NotificationDialog.show(myManager,"NotificationDialogFragment");
            dealsRef.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                senderName=dataSnapshot.child(dealKeyInPosition).child("sender").getValue().toString();
                matchName=dataSnapshot.child(dealKeyInPosition).child("matchName").getValue().toString();
                coin=dataSnapshot.child(dealKeyInPosition).child("coin").getValue().toString();
                duration=dataSnapshot.child(dealKeyInPosition).child("duration").getValue().toString();
                Log.e("matchname: ",matchName);
                Log.e("sender",senderName);

               /* SweetAlertDialog sd=  new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE);
                sd.setTitleText(""+matchName);
                sd.setContentText("Due to:"+duration+"\n\nSent by:"+senderName+"\n\nCoin:"+coin);
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.setConfirmText("Accept!");
                sd.setCancelText("Reject!");
                sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                  @Override
                  public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dealsRef.child(dealKeyInPosition).child("receiver").child(userName).setValue("accepted");
                    sweetAlertDialog.setTitleText("Challenge Accepted !");
                    sweetAlertDialog.setContentText("You have accepted a challenge.");
                    NotifList.remove(betNameInPosition);
                    dealKeyLis.remove(dealKeyInPosition);
                    sweetAlertDialog.setConfirmText("OK");
                    sweetAlertDialog.setConfirmClickListener(null);
                    sweetAlertDialog.showCancelButton(false);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                  }
                });
                sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                  @Override
                  public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dealsRef.child(dealKeyInPosition).child("receiver").child(userName).setValue("rejected");
                    sweetAlertDialog.setTitleText("Challenge Rejected !");
                    sweetAlertDialog.setContentText("You have rejected a challenge.");
                    NotifList.remove(betNameInPosition);
                    dealKeyLis.remove(dealKeyInPosition);
                    sweetAlertDialog.setConfirmText("OK");
                    sweetAlertDialog.setConfirmClickListener(null);
                    sweetAlertDialog.showCancelButton(false);
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                  }
                });
                sd.show();*/
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });
      }
    });

    ButterKnife.bind(this, rootView);
    return rootView;





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
  }
*/
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
