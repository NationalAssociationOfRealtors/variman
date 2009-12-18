package org.realtors.rets.server.admin.swingui;

import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JList;

/**
 * A Swing list model using a collections list as the data source.  This is
 * read-only in the sense that if the collections list changes, the Swing
 * component is not notified of changes.
 */
public class ListListModel extends AbstractListModel
{
    public ListListModel()
    {
        this(Collections.EMPTY_LIST);
    }

    public ListListModel(List list)
    {
        mList = list;
        mFormatter = NULL_FORMATTER;
    }

    public void setList(List newList)
    {
        mList = newList;
        List oldList = mList;
        int changed;
        if (newList.size() > oldList.size())
        {
            fireIntervalAdded(this, oldList.size(), newList.size());
            changed =  oldList.size();
        }
        else if (newList.size() < oldList.size())
        {
            fireIntervalRemoved(this, newList.size(), oldList.size());
            changed = newList.size();
        }
        else
        {
            // New list is the same size
            changed = newList.size();
        }
        fireContentsChanged(this, 0, changed);
    }

    public int getSize()
    {
        return mList.size();
    }

    public Object getElementAt(int i)
    {
        return mFormatter.format(mList.get(i));
    }

    public Object getListElementAt(int i)
    {
        return mList.get(i);
    }

    public Object getSelectedListElement(JList list)
    {
        return getListElementAt(list.getSelectedIndex());
    }

    public void setFormatter(ListElementFormatter formatter)
    {
        mFormatter = formatter;
    }

    private static class NullFormatter implements ListElementFormatter
    {
        public Object format(Object object)
        {
            return object;
        }
    }

    public static final ListElementFormatter NULL_FORMATTER =
        new NullFormatter();

    List mList;
    ListElementFormatter mFormatter;
}