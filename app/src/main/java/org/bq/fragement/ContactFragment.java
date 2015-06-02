package org.bq.fragement;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import org.bq.adapter.SimpleContactAdapter;
import org.bq.contact.ContactData;
import org.bq.contact.ContactItem;
import org.bq.contact.ContactItemInterface;
import org.bq.contact.SimpleContactListView;
import org.bq.yun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bq on 2015/1/4.
 */
public class ContactFragment extends Fragment {
    List<ContactItemInterface> contactList;
    List<ContactItemInterface> filterList;
    OnItemSelectedListener mCallback;
    private SimpleContactListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout contactView = (RelativeLayout) inflater.inflate(R.layout.contact, null);

        filterList = new ArrayList<ContactItemInterface>();
        contactList = ContactData.getPhoneContacts(getActivity());

        SimpleContactAdapter adapter = new SimpleContactAdapter(getActivity(),
                R.layout.contact_item, contactList);

        lv = (SimpleContactListView) contactView.findViewById(R.id.listview);
        lv.setFastScrollEnabled(true);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new ContactItemClickListener());
        return contactView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 保证容器Activity实现了回调接口 否则抛出异常警告
        try {
            mCallback = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    // 通讯接口, 加载该Fragment的容器Activity必须实现此接口可以接收ListView的点击消息
    public interface OnItemSelectedListener {
        /**
         * 当Fragment中的ListView点击的时候触发
         */
        public void onItemSelected(ContactItem item);
    }

    private class ContactItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ContactItem item = (ContactItem) contactList.get(position);
            mCallback.onItemSelected(item);
            final long personId = item.id;
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, personId);

            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            Bitmap photo = item.bitmap;
            if (photo != null) {
                intent.putExtra("photo", photo);
            }
            startActivity(intent);
        }
    }


}
