package org.realtors.rets.server.dmql;

public abstract class AbstractBackend implements DmtVisitor
{

    public void preVisit(DmtAST node)
    {
        throw new RuntimeException("AbstractBackend.preVisit should not be " +
                                   "called with a " + node.getClass().getName());
    }

    public void visit(DmtAST node)
    {
        throw new RuntimeException("AbstractBackend.visit should not be " +
                                   "called with a " + node.getClass().getName());
    }

    public void postVisit(DmtAST node)
    {
        throw new RuntimeException("AbstractBackend.postVisit should not be " +
                                   "called with a " + node.getClass().getName());
    }

}

