package com.example.androidquizproject.androidquizproject.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import com.example.androidquizproject.androidquizproject.Classes.scoreToFire;
import com.example.androidquizproject.androidquizproject.R;


public class record_adapter extends BaseAdapter {                                        //מחלקה עבור קבלת רשומות תוצאות השיאים לצורך הצגתם באפליקציה
    private List<scoreToFire> score;
    private Context context;
    public record_adapter(List<scoreToFire> score,  Context context) {
        this.score = score;
        this.context = context;
    }

    @Override
    public int getCount() {
        return score.size();
    }

    @Override
    public Object getItem(int position) {
        return score.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.user_info,parent,false);
            }

        TextView number=convertView.findViewById(R.id.placeID);
        TextView userName=convertView.findViewById(R.id.userName);
        number.setText(Integer.toString(position+1));
        userName.setText(score.get(position).toString());
        return convertView;
    }
}
