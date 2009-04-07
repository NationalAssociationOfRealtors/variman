/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.metadata;

public interface MetadataVisitor
{
    public Object visit(MSystem system);

    public Object visit(Resource resource);

    public Object visit(MClass clazz);

    public Object visit(Table table);

    public Object visit(Update update);

    public Object visit(UpdateType updateType);

    public Object visit(MObject object);

    public Object visit(Lookup lookup);

    public Object visit(LookupType lookupType);

    public Object visit(SearchHelp searchHelp);

    public Object visit(EditMask editMask);

    public Object visit(UpdateHelp updateHelp);

    public Object visit(ValidationLookup validationLookup);

    public Object visit(ValidationLookupType validationLookupType);

    public Object visit(ValidationExternal validationExternal);

    public Object visit(ValidationExpression validationExpression);

    public Object visit(ValidationExternalType validationExternalType);

    public Object visit(ForeignKey foreignKey);
}
