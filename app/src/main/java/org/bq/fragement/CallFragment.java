package org.bq.fragement;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bq.adapter.SimpleContactAdapter;
import org.bq.contact.ContactData;
import org.bq.contact.ContactItem;
import org.bq.contact.ContactItemInterface;
import org.bq.contact.SimpleContactListView;
import org.bq.yun.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bq on 2015/1/4.
 */
public class CallFragment extends Fragment {
    private static final String[] CALL_TYPE_NAME = {"接听", "打出", "未接"};
    private static final int[] CALL_TYPE_BGCOLOR = {Color.BLUE, Color.GREEN, Color.RED};
    private static final int[] CALL_TYPE_FGCOLOR = {Color.YELLOW, Color.RED, Color.WHITE};
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout contactView = (RelativeLayout) inflater.inflate(R.layout.call, null);
        Cursor cursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        lv = (ListView) contactView.findViewById(R.id.call_list);
        lv.setFastScrollEnabled(true);
        CallLogCursorAdapter adapter = new CallLogCursorAdapter(getActivity(), cursor);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new CallItemClickListener());
        return contactView;

    }

    private class CallItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }


    private class CallLogCursorAdapter extends CursorAdapter {
        private final SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm:ss");
        private LayoutInflater mInflater;


        public CallLogCursorAdapter(Context context, Cursor c) {
            super(context, c);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            setChildView(view, cursor);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.call_item, null);
            setChildView(view, cursor);
            return view;
        }


        public void setChildView(View view, Cursor cursor) {

            TextView callTypeImg = (TextView) view.findViewById(R.id.calltype);
            TextView nameTxt = (TextView) view.findViewById(R.id.name);
            TextView numberTxt = (TextView) view.findViewById(R.id.number);
            TextView epochTxt = (TextView) view.findViewById(R.id.epoch);
            TextView durationTxt = (TextView) view.findViewById(R.id.duration);

            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            if (name == null || name.trim().length() == 0) {
                name = "(未知号码)";
            }

            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            //  Integer epoch = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DATE));
            Integer duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            Integer callType = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));


            Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
            String time = sfd.format(date);


            nameTxt.setText(name);
            numberTxt.setText(number/*+":"+time*/);
            durationTxt.setText(duration + "秒");
            epochTxt.setText("" + time);
            int color = 0;

            switch (callType) {
                case CallLog.Calls.INCOMING_TYPE: {
                    color = Color.BLUE;
                    break;
                }
                case CallLog.Calls.OUTGOING_TYPE: {
                    color = Color.GREEN;
                    break;
                }
                case CallLog.Calls.MISSED_TYPE: {
                    color = Color.RED;
                    break;
                }
            }
            if (callType > 0 && callType < 4) {
                callTypeImg.setText(CALL_TYPE_NAME[callType - 1]);
                callTypeImg.setBackgroundColor(CALL_TYPE_BGCOLOR[callType - 1]);
                callTypeImg.setTextColor(CALL_TYPE_FGCOLOR[callType - 1]);
            }

        }
    }
}
