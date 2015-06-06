package org.bq.fragement;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.bq.yun.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AsyncFragment extends Fragment {
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.async, null);
        lv = (ListView) view.findViewById(R.id.async_list);
        AsyncAdapter adapter = new AsyncAdapter(getActivity(), Arrays.asList(new String[]{"联系人", "短信", "通话记录", "便签", "相册", "文件", "设备信息"}));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AsyncItemClickListener());
        Button btn = (Button) view.findViewById(R.id.async_btn);
        final ProgressDialog progressBar = new ProgressDialog(this.getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage("正在同步数据 ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.show();
            }
        });
        return view;

    }

    private class AsyncItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }


    private class AsyncAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<String> list;

        public AsyncAdapter(Context context, List list) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list = list;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int position) {
            return this.list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.async_item, null);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.async_check);
            TextView textView = (TextView) view.findViewById(R.id.async_text);
            textView.setText(this.list.get(position));
            return view;
        }
    }
}
