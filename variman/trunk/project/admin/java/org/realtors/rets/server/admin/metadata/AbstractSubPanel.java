/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxChoice;
import org.wxwindows.wxObject;
import org.wxwindows.wxScrolledWindow;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxValidator;
import org.wxwindows.wxWindow;
import org.wxwindows.wxTextValidator;

import org.realtors.rets.server.admin.BooleanChoice;
import org.realtors.rets.server.admin.TwoColumnGridSizer;

public abstract class AbstractSubPanel extends wxScrolledWindow
{
    protected AbstractSubPanel(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        TwoColumnGridSizer grid = new TwoColumnGridSizer(this);

        addContent(grid);

        box.Add(grid, 1, wxEXPAND | wxALL, 5);
        SetSizer(box);
        SetScrollRate(0, 10);
    }

    protected abstract void addContent(TwoColumnGridSizer grid);

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

    public static void setValue(wxChoice choice, Object object)
    {
        if (object == null)
        {
            choice.SetStringSelection("<none>");
        }
        else
        {
            choice.SetStringSelection(object.toString());
        }
    }

    public static void setValue(BooleanChoice choice, boolean b)
    {
        choice.SetBooleanSelection(b);
    }

    public wxTextCtrl createNumericTextCtrl()
    {
        wxTextCtrl textCtrl = new wxTextCtrl(this, -1);
        textCtrl.SetValidator(new wxTextValidator(wxFILTER_NUMERIC));
        return textCtrl;
    }
}
