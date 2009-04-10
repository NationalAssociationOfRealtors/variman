package org.realtors.rets.server.admin.swingui.metadata;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class MetadataDialog extends JDialog
{
    public MetadataDialog(JFrame frame)
    {
        super(frame);
    }
    
    public int getResponse()
    {
        return mResponse;
    }

    public void setResponse(int response)
    {
        mResponse = response;
    }

    protected static final String sBoolean [] = {   "False",
                                                    "True"
                                                  };

    private int                 mResponse;
    protected static final int  TEXT_WIDTH = 20;
}
