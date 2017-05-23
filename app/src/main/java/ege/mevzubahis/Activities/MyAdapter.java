package ege.mevzubahis.Activities;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ege.mevzubahis.R;
import ege.mevzubahis.Utils.BetCard;

/**
 * Created by Acer Bilgisayar on 15.4.2017.
 */

public class MyAdapter extends ArrayAdapter<BetCard> {

    private String[] names;
    private Context mContext;

    public MyAdapter(Context context, String [] bets) {
        super(context, R.layout.listview_item);
        this.names=bets;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView==null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.listview_item, parent, false);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }
            mViewHolder.mName.setText(names[position]);

        return convertView;
    }
    static class ViewHolder{
        TextView mName;
    }
}
