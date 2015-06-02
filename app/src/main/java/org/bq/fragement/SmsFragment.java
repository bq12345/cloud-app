package org.bq.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.bq.sms.Conversation;
import org.bq.sms.ConversationAdapter;
import org.bq.sms.MessageManager;
import org.bq.yun.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bq on 2015/1/4.
 */
public class SmsFragment extends Fragment {
    private Handler mHandler;
    private ListView listView;
    private List<Conversation> conversationList;
    private ConversationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new Handler();
        RelativeLayout smsLayout = (RelativeLayout) inflater.inflate(R.layout.message, null);
        listView = (ListView) smsLayout.findViewById(R.id.sms_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        //listView.setMultiChoiceModeListener(multiChoiceModeListener);
        initData();
        listView.requestFocus();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation c = (Conversation) listView.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Conversation --" + c.getId(), Toast.LENGTH_SHORT).show();
                //intent.putExtra("cid", c.getId());
                //startActivity(intent);
            }
        });
        return smsLayout;

    }


    private void initData() {
        conversationList = MessageManager.getConversations(getActivity());
        Collections.sort(conversationList, new Comparator<Conversation>() {
            public int compare(Conversation o1, Conversation o2) {
                Long l1 = Long.valueOf(o1.getTimeStamp());
                Long l2 = Long.valueOf(o2.getTimeStamp());
                return l2.compareTo(l1);
            }
        });
        adapter = new ConversationAdapter(getActivity(), conversationList);
        listView.setAdapter(adapter);
    }
}
