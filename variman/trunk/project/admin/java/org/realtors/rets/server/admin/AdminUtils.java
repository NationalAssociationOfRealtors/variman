/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wxTextCtrl;

public class AdminUtils
{
    public static void setValue(wxTextCtrl textCtrl, Object text)
    {
        if (text == null)
        {
            textCtrl.SetValue("");
        }
        else
        {
            textCtrl.SetValue(text.toString());
        }
    }

    public static void setValue(wxTextCtrl textCtrl, int number)
    {
        textCtrl.SetValue(Integer.toString(number));
    }

    public static void setValue(wxTextCtrl textCtrl, boolean b)
    {
        textCtrl.SetValue(Boolean.toString(b));
    }
}
