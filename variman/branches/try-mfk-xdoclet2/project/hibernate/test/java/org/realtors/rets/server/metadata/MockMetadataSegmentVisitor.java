/*
 */
package org.realtors.rets.server.metadata;

public class MockMetadataSegmentVisitor extends MetadataSegmentVisitor
{
    public MockMetadataSegmentVisitor()
    {
        mSystemCount = new MockCallCount();
        mResourceCount = new MockCallCount();
        mClassCount = new MockCallCount();
        mTableCount = new MockCallCount();
        mUpdateCount = new MockCallCount();
        mUpdateTypeCount = new MockCallCount();
        mObjectCount = new MockCallCount();
        mSearchHelpCount = new MockCallCount();
        mEditMaskCount = new MockCallCount();
        mLookupCount = new MockCallCount();
        mLookupTypeCount = new MockCallCount();
        mValidationLookupCount = new MockCallCount();
        mValidationLookupTypeCount = new MockCallCount();
        mValidationExternalCount = new MockCallCount();
        mValidationExternalTypeCount = new MockCallCount();
        mValidationExpressionCount = new MockCallCount();
        mForeignKeyCount = new MockCallCount();
        mEmptyCount = new MockCallCount();
    }

    public void setExpectedMSystemCalls(int callCount)
    {
        mSystemCount.setExpectedCallCount(callCount);
    }
    public void visitMSystem(MetadataSegment segment)
    {
        mSystemCount.increment();
    }

    public void setExpectedResourceCalls(int callCount)
    {
        mResourceCount.setExpectedCallCount(callCount);
    }

    public void visitResource(MetadataSegment segment)
    {
        mResourceCount.increment();
    }

    public void setExpectedMClassCalls(int callCount)
    {
        mClassCount.setExpectedCallCount(callCount);
    }

    public void visitMClass(MetadataSegment segment)
    {
        mClassCount.increment();
    }

    public void setExpectedEmptyCalls(int callCount)
    {
        mEmptyCount.setExpectedCallCount(callCount);
    }

    public void visitEmpty(MetadataSegment segment)
    {
        mEmptyCount.increment();
    }

    public void setExpectedTableCalls(int callCount)
    {
        mTableCount.setExpectedCallCount(callCount);
    }

    public void visitTable(MetadataSegment segment)
    {
        mTableCount.increment();
    }

    public void setExpectedUpdateCalls(int callCount)
    {
        mUpdateCount.setExpectedCallCount(callCount);
    }

    public void visitUpdate(MetadataSegment segment)
    {
        mUpdateCount.increment();
    }

    public void setExpectedUpdateTypeCalls(int callCount)
    {
        mUpdateTypeCount.setExpectedCallCount(callCount);
    }

    public void visitUpdateType(MetadataSegment segment)
    {
        mUpdateTypeCount.increment();
    }

    public void setExpectedMObjectCalls(int callCount)
    {
        mObjectCount.setExpectedCallCount(callCount);
    }

    public void visitMObject(MetadataSegment segment)
    {
        mObjectCount.increment();
    }

    public void setExpectedSearchHelpCalls(int callCount)
    {
        mSearchHelpCount.setExpectedCallCount(callCount);
    }

    public void visitSearchHelp(MetadataSegment segment)
    {
        mSearchHelpCount.increment();
    }

    public void setExpectedEditMaskCalls(int callCount)
    {
        mEditMaskCount.setExpectedCallCount(callCount);
    }

    public void visitEditMask(MetadataSegment segment)
    {
        mEditMaskCount.increment();
    }

    public void setExpectedLookupCalls(int callCount)
    {
        mLookupCount.setExpectedCallCount(callCount);
    }

    public void visitLookup(MetadataSegment segment)
    {
        mLookupCount.increment();
    }

    public void setExpectedLookupTypeCalls(int callCount)
    {
        mLookupTypeCount.setExpectedCallCount(callCount);
    }

    public void visitLookupType(MetadataSegment segment)
    {
        mLookupTypeCount.increment();
    }

    public void setExpectedValidationLookupCalls(int callCount)
    {
        mValidationLookupCount.setExpectedCallCount(callCount);
    }

    public void visitValidationLookup(MetadataSegment segment)
    {
        mValidationLookupCount.increment();
    }

    public void setExpectedValidationLookupTypeCalls(int callCount)
    {
        mValidationLookupTypeCount.setExpectedCallCount(callCount);
    }

    public void visitValidationLookupType(MetadataSegment segment)
    {
        mValidationLookupTypeCount.increment();
    }

    public void setExpectedValidationExternalCalls(int callCount)
    {
        mValidationExternalCount.setExpectedCallCount(callCount);
    }

    public void visitValidationExternal(MetadataSegment segment)
    {
        mValidationExternalCount.increment();
    }

    public void setExpectedValidationExternalTypeCalls(int callCount)
    {
        mValidationExternalTypeCount.setExpectedCallCount(callCount);
    }

    public void visitValidationExternalType(MetadataSegment segment)
    {
        mValidationExternalTypeCount.increment();
    }

    public void setExpectedValidationExpressionCalls(int callCount)
    {
        mValidationExpressionCount.setExpectedCallCount(callCount);
    }

    public void visitValidationExpression(MetadataSegment segment)
    {
        mValidationExpressionCount.increment();
    }

    public void setExpectedForeignKeyCalls(int callCount)
    {
        mForeignKeyCount.setExpectedCallCount(callCount);
    }

    public void visitForeignKey(MetadataSegment segment)
    {
        mForeignKeyCount.increment();
    }

    public void verify()
    {
        mSystemCount.verify();
        mResourceCount.verify();
        mClassCount.verify();
        mTableCount.verify();
        mUpdateCount.verify();
        mUpdateTypeCount.verify();
        mObjectCount.verify();
        mSearchHelpCount.verify();
        mEditMaskCount.verify();
        mLookupCount.verify();
        mLookupTypeCount.verify();
        mValidationLookupCount.verify();
        mValidationLookupTypeCount.verify();
        mValidationExternalCount.verify();
        mValidationExternalTypeCount.verify();
        mValidationExpressionCount.verify();
        mForeignKeyCount.verify();
        mEmptyCount.verify();
    }

    private MockCallCount mSystemCount;
    private MockCallCount mResourceCount;
    private MockCallCount mClassCount;
    private MockCallCount mTableCount;
    private MockCallCount mUpdateCount;
    private MockCallCount mUpdateTypeCount;
    private MockCallCount mObjectCount;
    private MockCallCount mSearchHelpCount;
    private MockCallCount mEditMaskCount;
    private MockCallCount mLookupCount;
    private MockCallCount mLookupTypeCount;
    private MockCallCount mValidationLookupCount;
    private MockCallCount mValidationLookupTypeCount;
    private MockCallCount mValidationExternalCount;
    private MockCallCount mValidationExternalTypeCount;
    private MockCallCount mValidationExpressionCount;
    private MockCallCount mForeignKeyCount;
    private MockCallCount mEmptyCount;
}
