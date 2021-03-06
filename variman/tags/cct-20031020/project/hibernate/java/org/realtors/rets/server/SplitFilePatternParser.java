/*
 */
package org.realtors.rets.server;

import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

public class SplitFilePatternParser extends PatternParser
{
    public SplitFilePatternParser(String pattern)
    {
        super(pattern);
    }

    protected void finalizeConverter(char formatChar)
    {
        switch (formatChar)
        {
            case 'X':
            case 'x':
            case 'p':
            case 't':
                super.finalizeConverter(formatChar);
                break;

            default:
                LogLog.error("Unexpected char [" + formatChar +
                             "] at position " + i + " in conversion patterrn.");
                addConverter(
                    new LiteralPatternConverter(currentLiteral.toString()));
                currentLiteral.setLength(0);
        }
    }

    private static class LiteralPatternConverter extends PatternConverter
    {
        LiteralPatternConverter(String value)
        {
            literal = value;
        }

        public final void format(StringBuffer sbuf, LoggingEvent event)
        {
            sbuf.append(literal);
        }

        public String convert(LoggingEvent event)
        {
            return literal;
        }

        private String literal;
    }
}
