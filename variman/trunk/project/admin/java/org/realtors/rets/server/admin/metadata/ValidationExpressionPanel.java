/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.ValidationExpression;

public class ValidationExpressionPanel extends AbstractSubPanel
{
    public ValidationExpressionPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mId = new wxTextCtrl(this, -1);
        grid.addRow("ID:", mId, wxEXPAND);

        mType = new wxTextCtrl(this, -1);
        grid.addRow("Type:", mType, wxEXPAND);

        mValue = new wxTextCtrl(this, -1);
        grid.addRow("Value:", mValue, wxEXPAND);
    }

    public void setModel(ValidationExpression validationExpression)
    {
        AdminUtils.setValue(
            mId, validationExpression.getValidationExpressionID());
        AdminUtils.setValue(
            mType, validationExpression.getValidationExpressionType());
        AdminUtils.setValue(mValue, validationExpression.getValue());
    }

    private wxTextCtrl mId;
    private wxTextCtrl mType;
    private wxTextCtrl mValue;
}
