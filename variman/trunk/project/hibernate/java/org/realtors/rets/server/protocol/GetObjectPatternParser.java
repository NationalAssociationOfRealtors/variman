/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

public class GetObjectPatternParser
{
    public GetObjectPatternParser(String pattern)
    {
        mPattern = pattern;
        mNormalState = new NormalState();
        mEscapeState = new EscapeState();
        mState = mNormalState;
    }

    public GetObjectPatternFormatter parse()
    {
        mCurrentLiteral = new StringBuffer();
        int patternLength = mPattern.length();
        GetObjectPatternFormatter mHeadFormatter = new NullPatternFormatter();
        mCurrentFormatter = mHeadFormatter;
        for (int i = 0; i < patternLength; i++)
        {
            char ch = mPattern.charAt(i);
            mState.handleCharacter(ch);
        }
        addCurrentLiteralFormatter();
        return mHeadFormatter;
    }

    private void addFormatter(GetObjectPatternFormatter formatter)
    {
        mCurrentFormatter.setNext(formatter);
        mCurrentFormatter = formatter;
    }

    private void addCurrentLiteralFormatter()
    {
        if (mCurrentLiteral.length() > 0)
        {
            addFormatter(
                new LiteralPatternFormatter(mCurrentLiteral.toString()));
            mCurrentLiteral.setLength(0);
        }
    }

    // ----------------------------------------------------------------
    // States
    // ----------------------------------------------------------------

    private interface ParserState
    {
        public void handleCharacter(char ch);
    }

    private class NormalState implements ParserState
    {
        public void handleCharacter(char ch)
        {
            switch (ch)
            {
                case '%':
                    addCurrentLiteralFormatter();
                    mState = mEscapeState;
                    break;

                default:
                    mCurrentLiteral.append(ch);
            }
        }
    }

    private class EscapeState implements ParserState
    {
        public void handleCharacter(char ch)
        {
            switch (ch)
            {
                case 'k':
                    addFormatter(new KeyFormatter());
                    break;

                case 'i':
                    addFormatter((new ObjectIdFormatter()));
                    break;

                default:
                    addFormatter(new LiteralPatternFormatter("%" + ch));
            }
            mState = mNormalState;
        }
    }

    // ----------------------------------------------------------------
    // Formatters
    // ----------------------------------------------------------------

    private static class NullPatternFormatter extends GetObjectPatternFormatter
    {
        protected void doFormat(StringBuffer buffer,
                                GetObjectPatternContext context)
        {
        }
    }

    private static class LiteralPatternFormatter
        extends GetObjectPatternFormatter
    {
        public LiteralPatternFormatter(String literal)
        {
            mLiteral = literal;
        }

        protected void doFormat(StringBuffer buffer,
                                GetObjectPatternContext context)
        {
            buffer.append(mLiteral);
        }

        private String mLiteral;

    }

    private static class KeyFormatter
        extends GetObjectPatternFormatter
    {
        protected void doFormat(StringBuffer buffer,
                                GetObjectPatternContext context)
        {
            buffer.append(context.getKey());
        }
    }

    private static class ObjectIdFormatter
        extends GetObjectPatternFormatter
    {
        protected void doFormat(StringBuffer buffer,
                                GetObjectPatternContext context)
        {
            buffer.append(context.getObjectId());
        }
    }

    private String mPattern;
    private ParserState mState;
    private NormalState mNormalState;
    private EscapeState mEscapeState;
    private StringBuffer mCurrentLiteral;
    private GetObjectPatternFormatter mCurrentFormatter;
}
