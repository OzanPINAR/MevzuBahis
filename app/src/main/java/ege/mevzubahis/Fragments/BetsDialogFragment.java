package ege.mevzubahis.Fragments;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import ege.mevzubahis.R;

/**
 * Created by Acer Bilgisayar on 18.4.2017.
 */

public class BetsDialogFragment extends DialogFragment implements View.OnClickListener{

    TextView matchName;
    TextView duration;
    RadioButton homeWins;
    RadioButton draw;
    RadioButton awayWins;
    TextView coin;
    EditText editText;
    Button button;
    TextView databaseDuration;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog,null);
        matchName=(TextView)view.findViewById(R.id.matchName);
        duration=(TextView)view.findViewById(R.id.duration);
        homeWins=(RadioButton)view.findViewById(R.id.homeWins);
        draw=(RadioButton)view.findViewById(R.id.draw);
        awayWins=(RadioButton)view.findViewById(R.id.awayWins);
        coin=(TextView)view.findViewById(R.id.coin);
        editText=(EditText)view.findViewById(R.id.editText);
        button=(Button)view.findViewById(R.id.button2);
        databaseDuration=(TextView)view.findViewById(R.id.databaseDuration);

        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.button2){
            dismiss();
            Toast.makeText(getActivity(),"Button is clicked",Toast.LENGTH_LONG).show();
        }

    }
}
