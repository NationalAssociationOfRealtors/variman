/*
 */
package org.realtors.rets.server.metadata;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class MetadataSegmentVisitorTest extends TestCase
{
    protected void setUp()
    {
        mVisitor = new MockMetadataSegmentVisitor();
        mVisitor.setExpectedEmptyCalls(0);
    }

    public void testDispatchEmpty()
    {
        mVisitor.setExpectedEmptyCalls(1);
        MetadataSegment segment =
            new MetadataSegment(new ArrayList(), null, null, null);
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchSystem()
    {
        mVisitor.setExpectedMSystemCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createSystem());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchResource()
    {
        mVisitor.setExpectedResourceCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createResource());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchClass()
    {
        mVisitor.setExpectedMClassCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createClass());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchTable()
    {
        mVisitor.setExpectedTableCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createTable());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchUpdate()
    {
        mVisitor.setExpectedUpdateCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createUpdate());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchUpdateType()
    {
        mVisitor.setExpectedUpdateTypeCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createUpdateType());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchObject()
    {
        mVisitor.setExpectedMObjectCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createMObject());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchSearchHelp()
    {
        mVisitor.setExpectedSearchHelpCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createSearchHelp());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchEditMask()
    {
        mVisitor.setExpectedEditMaskCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createEditMask());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchLookup()
    {
        mVisitor.setExpectedLookupCalls(1);
        MetadataSegment segment = createSegment(ObjectMother.createLookup());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchLookupType()
    {
        mVisitor.setExpectedLookupTypeCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createLookupType());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchValidationLookup()
    {
        mVisitor.setExpectedValidationLookupCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createValidationLookup());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchValidationLookupType()
    {
        mVisitor.setExpectedValidationLookupTypeCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createValidationLookupType());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchValidationExternal()
    {
        mVisitor.setExpectedValidationExternalCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createValidationExternal());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchValidationExternalType()
    {
        mVisitor.setExpectedValidationExternalTypeCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createValidationExternlType());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchValidationExpression()
    {
        mVisitor.setExpectedValidationExpressionCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createValidationExpression());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    public void testDispatchForeignKey()
    {
        mVisitor.setExpectedForeignKeyCalls(1);
        MetadataSegment segment =
            createSegment(ObjectMother.createForeignKey());
        mVisitor.dispatch(segment);
        mVisitor.verify();
    }

    private MetadataSegment createSegment(ServerMetadata metadata)
    {
        List data = new ArrayList();
        data.add(metadata);
        MetadataSegment segment = new MetadataSegment(data, null, null, null);
        return segment;
    }

    private MockMetadataSegmentVisitor mVisitor;
}
