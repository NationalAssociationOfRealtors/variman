package org.realtors.rets.server.webapp.cct;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;

/**
 * @jsp:tag name="evenoddtd"
 */
public class EvenOddTd extends BodyTagSupport
{
    public EvenOddTd()
    {
        mCount = null;
    }

    /**
     * @jsp:attribute required="true" rtexprvalue="false"
     *  description="The counter bean"
     * @return the name of the counter bean
     */
    public String getCount()
    {
        return mCount;
    }

    /**
     *
     * @param count the name of the counter bean
     */
    public void setCount(String count)
    {
        mCount = count;
    }

    public int doStartTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        Integer counter = (Integer) RequestUtils.lookup(pageContext, mCount,
                                                        null);
        int count = counter.intValue();
        String tdclass;
        if ((count % 2) == 1)
        {
            tdclass = "odd";
        }
        else
        {
            tdclass = "even";
        }
        try
        {
            out.write("<td class=\"");
            out.write(tdclass);
            out.write("\">");
        }
        catch (IOException e)
        {
            LOG.error("An exception occured", e);
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException
    {
        JspWriter out = pageContext.getOut();
        try
        {
            out.write("</td>");
        }
        catch (IOException e)
        {
            LOG.error("An exception occured", e);
            throw new JspException(e);

        }
        return EVAL_PAGE;
    }

    private String mCount;
    private static final Logger LOG = Logger.getLogger(EvenOddTd.class);
}
