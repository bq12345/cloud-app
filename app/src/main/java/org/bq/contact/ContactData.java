package org.bq.contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Toast;

import org.bq.yun.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bq on 2015/1/5.
 */
public class ContactData {

    /**
     * 获取库Phone表字段 *
     */
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称 *
     */
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码 *
     */
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 头像ID *
     */
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**
     * 联系人的ID *
     */
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 联系人
     */
    private ArrayList<String> mContactsName = new ArrayList<String>();

    public static ArrayList<ContactItemInterface> getPhoneContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContactItemInterface> users = new ArrayList<ContactItemInterface>();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                ContactItem item = new ContactItem();
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                // 得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                // photoid 大于0 表示联系人有头像
                // 如果没有给此人设置头像则放入null，绘制时使用静态变量（一个默认的）

                int[] colors = {Color.rgb(0, 167, 234), Color.rgb(249, 157, 67), Color.rgb(0, 167, 23), Color.rgb(243, 91, 79), Color.rgb(243, 186, 51)};
                int color = colors[new Random().nextInt(5)];
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts
                            .openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
                    String title = contactName.substring(0, 1);
                    Bitmap bmp = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmp);
                    canvas.drawColor(color);
                    Paint p = new Paint();
                    Typeface font = Typeface.create("黑体", Typeface.BOLD);
                    p.setColor(Color.WHITE);
                    p.setTypeface(font);
                    p.setTextSize(90);
                    canvas.drawText(title, 16, 100, p);
                    contactPhoto = bmp;
                }
                item.id = contactid;
                item.name = contactName;
                item.number = phoneNumber;
                item.bitmap = contactPhoto;
                users.add(item);
            }

            phoneCursor.close();
        }
        return users;
    }
}
