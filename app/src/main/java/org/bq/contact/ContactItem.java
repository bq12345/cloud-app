package org.bq.contact;

import android.graphics.Bitmap;

import net.sourceforge.pinyin4j.*;

import org.bq.helper.PinyinUtil;

/**
 * Created by bq on 2015/1/5.
 */
public class ContactItem implements ContactItemInterface {
    public long id;
    public String name;
    public Bitmap bitmap;
    public String number;

    @Override
    public String toString() {
        return "[" + name + number + "]";
    }

    @Override
    public String getItemForIndex() {
        return PinyinUtil.hanziToPinyin(name);
    }
}
