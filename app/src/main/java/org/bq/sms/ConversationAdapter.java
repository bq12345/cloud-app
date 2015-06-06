package org.bq.sms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bq.yun.R;


public class ConversationAdapter extends BaseAdapter {
    private final static String TAG = "ConversationAdapter";

    private List<Conversation> mConversations;
    private Context mContext;

    public ConversationAdapter(Context context, List<Conversation> conversations) {
        super();
        this.mContext = context;
        this.mConversations = conversations;
    }

    @Override
    public int getCount() {
        return mConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mConversations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation c = (Conversation) getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.conversation_row, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.smsUser);
            holder.name = (TextView) convertView.findViewById(R.id.smsName);
            holder.summary = (TextView) convertView.findViewById(R.id.smsSummary);
            holder.time = (TextView) convertView.findViewById(R.id.smsTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int[] colors = {Color.rgb(0, 167, 234), Color.rgb(249, 157, 67), Color.rgb(0, 167, 23), Color.rgb(243, 91, 79), Color.rgb(243, 186, 51)};
        int color = colors[new Random().nextInt(5)];
        holder.image.setBackgroundColor(color);
        holder.name.setText(c.getContact().getNameOrNumber());
        if (c.getContact().getName() != null && !c.getContact().getName().equals("")) {
            Bitmap bmp = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint();
            Typeface font = Typeface.create("黑体", Typeface.BOLD);
            p.setColor(Color.WHITE);
            p.setTypeface(font);
            p.setTextSize(100);
            canvas.drawText(c.getContact().getName().substring(0, 1), 16, 100, p);
            holder.image.setImageBitmap(bmp);
        }

        String summary = c.getSummary();
        if (summary.length() >= 20) {
            summary = summary.substring(0, 20) + "...";
        }
        holder.summary.setText(summary);

        // Set up time display format
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(c.getTimeStamp());
        Calendar curDate = Calendar.getInstance();
        curDate.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = null;
        if (date.get(Calendar.YEAR) == curDate.get(Calendar.YEAR)) {
            if (date.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)) {
                dateFormat = new SimpleDateFormat("h时mm分", Locale.CHINA);
            } else {
                dateFormat = new SimpleDateFormat("M月d日", Locale.CHINA);
            }
        } else {
            dateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        }
        holder.time.setText(dateFormat.format(c.getTimeStamp()));

        if (c.hasUnread()) {
            Log.i("ConversationAdapter", c.getContact().getNameOrNumber());
            holder.name.setTypeface(null, Typeface.BOLD);
        } else {
            holder.name.setTypeface(null, Typeface.NORMAL);
        }
        return convertView;
    }

    /**
     * remove data item from Adapter
     */
    public void remove(int position) {
        mConversations.remove(position);
    }

    /**
     * remove all data items from Adapter
     */
    public void clear() {
        mConversations.clear();
    }

    /**
     * add data item to Adapter
     */
    public void add(Conversation c) {
        mConversations.add(c);
    }

    private static class ViewHolder {
        TextView name;
        TextView summary;
        TextView time;
        ImageView image;
    }
}
