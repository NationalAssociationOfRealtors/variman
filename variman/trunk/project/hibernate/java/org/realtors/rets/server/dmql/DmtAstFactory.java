/*
 */
package org.realtors.rets.server.dmql;

import antlr.ASTFactory;
import antlr.Token;
import antlr.collections.AST;

public class DmtAstFactory extends ASTFactory
{
    public DmtAstFactory()
    {
        super();
    }

    public AST create(Token tok, String className)
    {
        return super.create(tok, "org.realtors.rets.server.dmql." + className);
    }
}
