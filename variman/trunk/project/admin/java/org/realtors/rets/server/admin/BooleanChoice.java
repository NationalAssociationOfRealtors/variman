/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wxChoice;
import org.wxwindows.wxWindow;

public class BooleanChoice extends wxChoice
{
    public BooleanChoice(wxWindow parent, int id)
    {
        super(parent, id, wxDefaultPosition, wxDefaultSize, CHOICES);
    }

    public void SetBooleanSelection(boolean b)
    {
        if (b)
        {
            SetSelection(TRUE_CHOICE);
        }
        else
        {
            SetSelection(FALSE_CHOICE);
        }
    }

    private static final String[] CHOICES = {"True", "False"};
    private static final int TRUE_CHOICE = 0;
    private static final int FALSE_CHOICE = 1;
}
