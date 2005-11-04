//$Id: StringHelper.java,v 1.21 2004/07/15 06:18:45 oneovthafew Exp $
package net.sf.hibernate.util;

import java.util.Iterator;
import java.util.StringTokenizer;

public final class StringHelper {

	public static final String EMPTY_STRING="";
	public static final char DOT='.';
	public static final char UNDERSCORE='_';
	public static final String COMMA_SPACE=", ";
	public static final String COMMA = ",";
	public static final String OPEN_PAREN = "(";
	public static final String CLOSE_PAREN = ")";
    public static final char SINGLE_QUOTE = '\'';

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length==0) return EMPTY_STRING;
		StringBuffer buf = new StringBuffer( length * strings[0].length() )
		.append(strings[0]);
		for (int i=1; i<length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static String join(String seperator, Iterator objects) {
		StringBuffer buf = new StringBuffer();
		if ( objects.hasNext() ) buf.append( objects.next() );
		while ( objects.hasNext() ) {
			buf.append(seperator).append( objects.next() );
		}
		return buf.toString();
	}

	public static String[] add(String[] x, String sep, String[] y) {
		String[] result = new String[ x.length ];
		for ( int i=0; i<x.length; i++ ) {
			result[i] = x[i] + sep + y[i];
		}
		return result;
	}

	public static String repeat(String string, int times) {
		StringBuffer buf = new StringBuffer( string.length() * times );
		for (int i=0; i<times; i++) buf.append(string);
		return buf.toString();
	}


	public static String replace(String template, String placeholder, String replacement) {
		return replace(template, placeholder, replacement, false);
	}

	public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
		int loc = template.indexOf(placeholder);
		if (loc<0) {
			return template;
		}
		else {
			final boolean actuallyReplace = !wholeWords ||
				loc + placeholder.length() == template.length() ||
				!Character.isJavaIdentifierPart( template.charAt( loc + placeholder.length() ) );
			String actualReplacement = actuallyReplace ? replacement : placeholder;
			return new StringBuffer( template.substring(0, loc) )
				.append(actualReplacement)
				.append( replace(
					template.substring( loc + placeholder.length() ),
					placeholder,
					replacement,
					wholeWords
				) ).toString();
		}
	}


	public static String replaceOnce(String template, String placeholder, String replacement) {
		int loc = template.indexOf(placeholder);
		if ( loc<0 ) {
			return template;
		}
		else {
			return new StringBuffer( template.substring(0, loc) )
			.append(replacement)
			.append( template.substring( loc + placeholder.length() ) )
			.toString();
		}
	}


	public static String[] split(String seperators, String list) {
		return split(seperators, list, false);
	}

	public static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[ tokens.countTokens() ];
		int i=0;
		while ( tokens.hasMoreTokens() ) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}

	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, ".");
	}

	public static String unqualify(String qualifiedName, String seperator) {
		return qualifiedName.substring( qualifiedName.lastIndexOf(seperator) + 1 );
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		if ( loc<0 ) {
			return EMPTY_STRING;
		}
		else {
			return qualifiedName.substring(0, loc);
		}
	}

	public static String[] suffix( String[] columns, String suffix) {
		if (suffix==null) return columns;
		String[] qualified = new String[columns.length];
		for ( int i=0; i<columns.length; i++ ) {
			qualified[i] = suffix( columns[i], suffix);
		}
		return qualified;
	}

	public static String suffix(String name, String suffix) {
		return (suffix==null) ? name : name + suffix;
	}

	public static String[] prefix( String[] columns, String prefix) {
		if (prefix==null) return columns;
		String[] qualified = new String[columns.length];
		for ( int i=0; i<columns.length; i++ ) {
			qualified[i] = prefix + columns[i];
		}
		return qualified;
	}

	public static String root(String qualifiedName) {
		int loc = qualifiedName.indexOf(".");
		return (loc<0) ? qualifiedName : qualifiedName.substring(0, loc);
	}

	public static boolean booleanValue(String tfString) {
		String trimmed = tfString.trim().toLowerCase();
		return trimmed.equals("true") || trimmed.equals("t");
	}

	public static String toString(Object[] array) {
		int len = array.length;
		if (len==0) return StringHelper.EMPTY_STRING;
		StringBuffer buf = new StringBuffer(len * 12);
		for ( int i=0; i<len-1; i++ ) {
			buf.append( array[i] ).append(StringHelper.COMMA_SPACE);
		}
		return buf.append( array[len-1] ).toString();
	}

	public static String[] multiply(String string, Iterator placeholders, Iterator replacements) {
		String[] result = new String[] { string };
		while ( placeholders.hasNext() ) {
			result = multiply( result, (String) placeholders.next(), (String[]) replacements.next() );
		}
		return result;
	}

	private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
		String[] results = new String[ replacements.length * strings.length ];
		int n=0;
		for ( int i=0; i<replacements.length; i++ ) {
			for ( int j=0; j<strings.length; j++ ) {
				results[n++] = replaceOnce( strings[j], placeholder, replacements[i] );
			}
		}
		return results;
	}

	/*public static String unQuote(String name) {
		return ( Dialect.QUOTE.indexOf( name.charAt(0) ) > -1 ) ?
		name.substring(1, name.length()-1) :
		name;
	}

	public static void unQuoteInPlace(String[] names) {
		for ( int i=0; i<names.length; i++ ) names[i] = unQuote( names[i] );
	}

	public static String[] unQuote(String[] names) {
		String[] unquoted = new String[ names.length ];
		for ( int i=0; i<names.length; i++ ) unquoted[i] = unQuote( names[i] );
		return unquoted;
	}*/

	public static int count(String string, char character) {
		int n=0;
		for ( int i=0; i<string.length(); i++ ) {
			if ( string.charAt(i)==character ) n++;
		}
		return n;
	}

	public static int countUnquoted(String string, char character) {
        if ( SINGLE_QUOTE == character ) {
            throw new IllegalArgumentException("Unquoted count of quotes is invalid");
        }
        // Impl note: takes advantage of the fact that an escpaed single quote
        // embedded within a quote-block can really be handled as two seperate
        // quote-blocks for the purposes of this method...
		int count=0;
        int stringLength = string == null ? 0 : string.length();
        boolean inQuote = false;
		for ( int indx=0; indx<stringLength; indx++ ) {
            if ( inQuote ) {
                if ( SINGLE_QUOTE == string.charAt(indx) ) {
                    inQuote = false;
                }
            }
            else if ( SINGLE_QUOTE == string.charAt(indx) ) {
                inQuote = true;
            }
            else if ( string.charAt(indx)==character ) {
                count++;
            }
        }
		return count;
	}

	public static boolean isNotEmpty(String string) {
		return string!=null && string.length() > 0;
	}

	public static String qualify(String prefix, String name) {
		if ( name.startsWith("'") ) return name;

		return new StringBuffer( prefix.length() + name.length() + 1 )
			.append(prefix)
			.append(DOT)
			.append(name)
			.toString();
	}

	public static String[] qualify(String prefix, String[] names) {
		if (prefix==null) return names;
		int len = names.length;
		String[] qualified = new String[len];
		for ( int i=0; i<len; i++) {
			qualified[i] = qualify( prefix, names[i] );
		}
		return qualified;
	}

	private StringHelper() { /* static methods only - hide constructor */ }

	public static int firstIndexOfChar(String sqlString, String string, int startindex) {
		int matchAt=-1;
		for (int i = 0; i < string.length(); i++) {
			int curMatch  = sqlString.indexOf( string.charAt(i), startindex );
			if (curMatch >=0) {
				if (matchAt==-1) { // first time we find match!
					matchAt = curMatch;
				} 
				else {
					matchAt = Math.min(matchAt, curMatch);
				}
			}
		}
		return matchAt;
	}
	
	public static String truncate(String string, int length) {
		if (string.length()<=length) {
			return string;
		}
		else {
			return string.substring(0, length);
		}
	}

}






