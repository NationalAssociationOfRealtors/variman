package org.realtors.rets.server.admin.swingui;

import java.util.List;

import javax.swing.*;

/**
 * A Swing list model using a collections list as the data source.  This is
 * read-only in the sense that if the collections list changes, the Swing
 * component is not notified of changes.
 */
public class ListListModel extends AbstractListModel
{
    public ListListModel(List list)
    {
        mList = list;
    }

    public int getSize()
    {
        return mList.size();
    }

    public Object getElementAt(int i)
    {
        return mList.get(i);
    }

    List mList;
}