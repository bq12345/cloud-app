package org.bq.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.bq.contact.ContactItem;
import org.bq.contact.ContactItemInterface;
import org.bq.contact.ContactListAdapter;
import org.bq.yun.R;

import java.util.List;

/**
 * Created by bq on 2015/1/5.
 */


public class SimpleContactAdapter extends ContactListAdapter {

    public SimpleContactAdapter(Context _context, int _resource,
                                List<ContactItemInterface> _items) {
        super(_context, _resource, _items);

    }

    // override this for custom drawing
    public void populateDataForRow(View parentView, ContactItemInterface item, int position) {
        // default just draw the item only
        View infoView = parentView.findViewById(R.id.infoRowContainer);
        TextView numberView = (TextView) infoView.findViewById(R.id.numberView);
        TextView nameView = (TextView) infoView.findViewById(R.id.nameView);
        ImageView userView = (ImageView) infoView.findViewById(R.id.userView);
        if (item instanceof ContactItem) {

            ContactItem contactItem = (ContactItem) item;
            nameView.setText(contactItem.name);
            numberView.setText(contactItem.number);
            userView.setImageBitmap(contactItem.bitmap);
        }

    }

}
