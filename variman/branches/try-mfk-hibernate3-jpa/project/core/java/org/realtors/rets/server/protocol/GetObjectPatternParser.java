/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import org.apache.commons.lang.StringUtils;

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
        public EscapeState()
        {
            resetCount();
        }

        public void handleCharacter(char ch)
        {
            if (ch == 'k')
            {
                addFormatter(new KeyFormatter(getCount()));
                gotoNormalState();
            }
            else if (ch == 'i')
            {
                addFormatter((new ObjectIdFormatter()));
                gotoNormalState();
            }
            else if (ch == 'I')
            {
                addFormatter(new EmptyObjectIdFormatter());
                gotoNormalState();
            }
            else if (Character.isDigit(ch))
            {
                appendCountDigit(ch);
            }
            else if (ch == '-')
            {
                mSign *= -1;
            }
            else
            {
                addFormatter(new LiteralPatternFormatter("%" + ch));
                gotoNormalState();
            }
        }

        private void gotoNormalState()
        {
            mState = mNormalState;
            resetCount();
        }

        private void resetCount()
        {
            mCount = 0;
            mSign = 1;
        }

        private int getCount()
        {
            return mSign * mCount;
        }

        private void appendCountDigit(char ch)
        {
            mCount *= 10;
            mCount += Character.getNumericValue(ch);
        }

        private int mCount;
        private int mSign;
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
        public KeyFormatter(int width)
        {
            mWidth = width;
        }

        protected void doFormat(StringBuffer buffer,
                                GetObjectPatternContext context)
        {
            String key = context.getKey();
            if (mWidth > 0)
            {
                key = key.substring(0, mWidth);
            }
            else if (mWidth < 0)
            {
                key = StringUtils.substring(key, mWidth);
            }
            buffer.append(key);
        }

        private int mWidth;
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

    private static class EmptyObjectIdFormatter
        extends GetObjectPatternFormatter
    {
        protected void doFormat(StringBuffer buffer,
                                GetObjectPatternContext context)
        {
            int objectId = context.getObjectId() - 1;
            if (objectId != 0)
            {
                buffer.append("_").append(objectId);
            }
        }
    }

    private String mPattern;
    private ParserState mState;
    private NormalState mNormalState;
    private EscapeState mEscapeState;
    private StringBuffer mCurrentLiteral;
    private GetObjectPatternFormatter mCurrentFormatter;
}
