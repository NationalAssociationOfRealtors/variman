header {
package org.realtors.rets.server.dmql;
import java.util.*;
}

class DmqlTreeParser extends TreeParser;

options
{
    importVocab = DMQL;
    defaultErrorHandler = false;
}

{
    public void traceIn(String text, AST t) {
        if (mTrace) super.traceIn(text, t);
    }

    public void traceOut(String text, AST t) {
        if (mTrace) super.traceOut(text, t);
    }

    public void setTrace(boolean trace) {
        mTrace = trace;
    }

    public void setMetadata(DmqlParserMetadata metadata) {
        mMetadata = metadata;
    }

    /**
     * To be called after parsing. Returns the set of fields (system names or
     * standard names) found during the parse. Never returns <code>null</code>,
     * however, may return an empty set.
     *
     * @return the set of fields found during the parse.
     */
    public Set<String> getFoundFields() {
        return mFields;
    }

    private boolean isLookupMulti(String fieldName) {
        return (mMetadata.getFieldType(fieldName) ==
                DmqlFieldType.LOOKUP_MULTI);
    }

    private LookupList newLookupOr(String field, List lookups) {
        LookupList list = new LookupList(LookupListType.OR,
                                         mMetadata.fieldToColumn(field));
        list.setLookupMulti(isLookupMulti(field));
        addLookups(list, field, lookups);
        return list;
    }

    private LookupList newLookupAnd(String field, List lookups) {
        LookupList list = new LookupList(LookupListType.AND,
                                         mMetadata.fieldToColumn(field));
        list.setLookupMulti(isLookupMulti(field));
        addLookups(list, field, lookups);
        return list;
    }

    private LookupList newLookupNot(String field, List lookups) {
        LookupList list = new LookupList(LookupListType.NOT,
                                         mMetadata.fieldToColumn(field));
        list.setLookupMulti(isLookupMulti(field));
        addLookups(list, field, lookups);
        return list;
    }

    private void addLookups(LookupList list, String field, List lookups) {
        for (int i = 0; i < lookups.size(); i++) {
            String lookup = (String) lookups.get(i);
            String dbValue = mMetadata.getLookupDbValue(field, lookup);
            list.addLookup(dbValue);
        }
    }

    private DmqlStringList newStringList(String field, List strings) {
        String column = mMetadata.fieldToColumn(field);
        DmqlStringList list = new DmqlStringList(column);
        for (int i = 0; i < strings.size(); i++) {
            DmqlString string = (DmqlString) strings.get(i);
            list.add(string);
        }
        return list;
    }

    private DmqlStringList newStringList(String field, String string) {
        String column = mMetadata.fieldToColumn(field);
        DmqlStringList list = new DmqlStringList(column);
        list.add(new DmqlString(string));
        return list;
    }

    private EqualClause newEqualClause(String field, String value) {
        return newEqualClause(field, new StringSqlConverter(value));
    }

    private EqualClause newEqualClause(String field, SqlConverter sql) {
        String column = mMetadata.fieldToColumn(field);
        return new EqualClause(column, sql);
    }

    private AnyClause newAnyClause(String field) {
        String column = mMetadata.fieldToColumn(field);
        return new AnyClause(column);
    }

    private EmptyClause newEmptyClause(String field) {
        String column = mMetadata.fieldToColumn(field);
        DmqlFieldType dmqlFieldType = mMetadata.getFieldType(field);
        return new EmptyClause(column, dmqlFieldType);
    }

    private String fieldToColumn(String field) {
        return mMetadata.fieldToColumn(field);
    }

    private boolean mTrace = false;
    private DmqlParserMetadata mMetadata;
    private Set<String> mFields = new HashSet<String>();
}

query returns [SqlConverter sql]
    : sql=compound EOF
    ;

compound returns [SqlConverter sql]
    { SqlConverter l; SqlConverter r; }
    : #(OR  l=compound r=compound)  {sql = new OrClause (l, r);}
    | #(AND l=compound r=compound)  {sql = new AndClause(l, r);}
    | #(NOT l=compound)             {sql = new NotClause(l);}
    | sql=field_criteria
    ;

field_criteria returns [SqlConverter sql]
    : sql=lookup_list
    | sql=string_list
    | sql=range_list
    | sql=string_literal
    | sql=number_value
    | sql=period_value
    | sql=dotempty_value
    | sql=dotany_value
    ;

field returns [String text]
    : t:TEXT {
        text = t.getText();
        mFields.add(text); // Keep track of the fields stumbled upon.
      }
    ;

lookup_list returns [LookupList list]
    { String f; List l; }
    : #(LOOKUP_OR  f=field l=lookups)   {list = newLookupOr (f, l);}
    | #(LOOKUP_AND f=field l=lookups)   {list = newLookupAnd(f, l);}
    | #(LOOKUP_NOT f=field l=lookups)   {list = newLookupNot(f, l);}
    ;

lookups returns [List lookups]
    { lookups = new ArrayList();}
    : (l:LOOKUP {lookups.add(l.getText());}
        )+
    ;

string_list returns [DmqlStringList list]
    { String f; List s; }
    : #(STRING_LIST f=field s=strings) {list = newStringList(f, s);}
    ;

strings returns [List list]
    { list = new ArrayList(); DmqlString s;}
    : (s=string {list.add(s);}
        )+
    ;

string returns [DmqlString string]
    { string = new DmqlString(); }
    : #(STRING
            (t:TEXT     {string.add(t.getText());}
            | STAR      {string.add(DmqlString.MATCH_ZERO_OR_MORE);}
            | QUESTION  {string.add(DmqlString.MATCH_ZERO_OR_ONE);}
            )+
        )
    ;

range_list returns [OrClause or]
    { or = new OrClause(); String f; SqlConverter r;}
    : #(RANGE_LIST f=field
            (r=range[f] {or.add(r);}
            )+
        )
    ;

range [String field] returns [SqlConverter sql]
    { SqlConverter c1; SqlConverter c2;
      String column = fieldToColumn(field); }
    : #(BETWEEN c1=range_component c2=range_component)
                                    {sql = new BetweenClause(column, c1, c2);}
    | #(LESS c1=range_component)    {sql = new LessThanClause(column, c1);}
    | #(GREATER c1=range_component) {sql = new GreaterThanClause(column, c1);}
    ;

range_component returns [SqlConverter sql]
    : sql=period
    | n:NUMBER  {sql = new StringSqlConverter(n.getText());}
    | t:TEXT    {sql = new QuotedSqlConverter(t.getText());}
    ;

period returns [SqlConverter sql]
    : d:DATE       {sql = new DateSqlConverter(d.getText());}
    | t:TIME       {sql = new TimeSqlConverter(t.getText());}
    | dt:DATETIME  {sql = new DateTimeSqlConverter(dt.getText());}
    | TODAY        {sql = new DateSqlConverter();}
    | NOW          {sql = new DateTimeSqlConverter();}
    ;

string_literal returns [DmqlStringList list]
    { String f; }
    : #(STRING_LITERAL f=field s:STRING_LITERAL)
         { list = newStringList(f, s.getText()); }
    ;

number_value returns [SqlConverter sql]
    { String f; }
    : #(NUMBER f=field n:NUMBER) 
            { if (mMetadata.getFieldType(f) == DmqlFieldType.BOOLEAN)
                sql = newEqualClause(f, new BooleanSqlConverter(n.getText()));
              else
                sql = newEqualClause(f, n.getText());
            }
    ;

period_value returns [SqlConverter sql]
    { String f; SqlConverter p;}
    : #(PERIOD f=field p=period) { sql = newEqualClause(f, p);}
    ;

dotany_value returns [SqlConverter sql]
    { String f;}
    : #(DOT_ANY f=field) { sql = newAnyClause(f);}
    ;

dotempty_value returns [SqlConverter sql]
    { String f;}
    : #(DOT_EMPTY f=field) { sql = newEmptyClause(f); }
    ;
