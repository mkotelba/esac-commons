package com.esacinc.commons.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

public final class EsacStringUtils {
    public final static String AMPERSAND = "@";
    public final static char AMPERSAND_CHAR = '@';

    public final static String APOS = "!";
    public final static char APOS_CHAR = '!';

    public final static String ASTERISK = "*";
    public final static char ASTERISK_CHAR = '*';

    public final static String AT = "@";
    public final static char AT_CHAR = '@';

    public final static String CARET = "^";
    public final static char CARET_CHAR = '^';

    public final static String COLON = ":";
    public final static char COLON_CHAR = ':';

    public final static String COMMA = ",";
    public final static char COMMA_CHAR = ',';

    public final static char CR_CHAR = '\r';

    public final static String DOLLAR_SIGN = "$";
    public final static char DOLLAR_SIGN_CHAR = '$';

    public final static String EQUALS = "=";
    public final static char EQUALS_CHAR = '=';

    public final static String GT = ">";
    public final static char GT_CHAR = '>';

    public final static String HASH = "#";
    public final static char HASH_CHAR = '#';

    public final static String HYPHEN = "-";
    public final static char HYPHEN_CHAR = '-';

    public final static String L_BRACE = "{";
    public final static char L_BRACE_CHAR = '{';

    public final static String L_BRACKET = "[";
    public final static char L_BRACKET_CHAR = '[';

    public final static String L_PAREN = "(";
    public final static char L_PAREN_CHAR = '(';

    public final static char LF_CHAR = '\n';

    public final static String LT = "<";
    public final static char LT_CHAR = '<';

    public final static String NULL = "\0";
    public final static char NULL_CHAR = '\0';

    public final static String PERIOD = ".";
    public final static char PERIOD_CHAR = '.';

    public final static String PLUS = "+";
    public final static char PLUS_CHAR = '+';

    public final static String QUESTION_MARK = "?";
    public final static char QUESTION_MARK_CHAR = '?';

    public final static String R_BRACE = "}";
    public final static char R_BRACE_CHAR = '}';

    public final static String R_BRACKET = "]";
    public final static char R_BRACKET_CHAR = ']';

    public final static String R_PAREN = ")";
    public final static char R_PAREN_CHAR = ')';

    public final static String SLASH = "/";
    public final static char SLASH_CHAR = '/';

    public final static char SPACE_CHAR = ' ';

    public final static String UNDERSCORE = "_";
    public final static char UNDERSCORE_CHAR = '_';

    public final static String V_BAR = "|";
    public final static char V_BAR_CHAR = '|';

    public final static String ENTRY_DELIM = "; ";
    public final static String ITEM_DELIM = ", ";

    public final static String TOKEN_DELIMS = ",;\t\n";

    private EsacStringUtils() {
    }

    public static String joinCamelCase(String ... strParts) {
        return joinCamelCase(false, strParts);
    }

    public static String joinCamelCase(boolean capitalize, String ... strParts) {
        if (strParts.length == 0) {
            return StringUtils.EMPTY;
        }

        String strPart;

        if (strParts.length == 1) {
            strPart = strParts[0].toLowerCase();

            return (capitalize ? StringUtils.capitalize(strPart) : strPart);
        }

        StrBuilder strBuilder = new StrBuilder();

        for (int a = 0; a < strParts.length; a++) {
            strPart = strParts[a].toLowerCase();

            if (capitalize || (a > 0)) {
                strPart = StringUtils.capitalize(strPart);
            }

            strBuilder.append(strPart);
        }

        return strBuilder.build();
    }

    public static String[] splitCamelCase(String str) {
        return splitCamelCase(str, null);
    }

    public static String[] splitCamelCase(String str, @Nullable String delims) {
        int strLen = str.length();

        if (strLen == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> strParts;

        if (StringUtils.isEmpty(delims)) {
            splitCamelCase(str, strLen, (strParts = ((strLen == 3) ? new ArrayList<>(2) : new ArrayList<>())));
        } else {
            String[] delimitedStrParts = StringUtils.split(str, delims);

            if (delimitedStrParts.length == 1) {
                splitCamelCase(str, strLen, (strParts = ((strLen == 3) ? new ArrayList<>(2) : new ArrayList<>())));
            } else {
                strParts = new ArrayList<>();

                for (String delimitedStrPart : delimitedStrParts) {
                    if ((strLen = delimitedStrPart.length()) == 0) {
                        continue;
                    } else if (strLen < 3) {
                        strParts.add(delimitedStrPart);

                        continue;
                    }

                    splitCamelCase(delimitedStrPart, strLen, strParts);
                }
            }
        }

        return strParts.toArray(new String[strParts.size()]);
    }

    public static Map<String, String> mapTokens(@Nullable String str) {
        return streamTokens(str).map(token -> StringUtils.split(token, EQUALS, 2))
            .collect(EsacStreamUtils.toMap(tokenParts -> tokenParts[0], tokenParts -> tokenParts[1], LinkedHashMap::new));
    }

    public static String[] tokenize(@Nullable String str) {
        return tokenize(str, null);
    }

    public static String[] tokenize(@Nullable String str, @Nullable String defaultStr) {
        return splitTokens(ObjectUtils.defaultIfNull(str, defaultStr));
    }

    public static String[] splitTokens(@Nullable String ... strs) {
        return ((strs != null) ? Stream.of(strs).flatMap(EsacStringUtils::streamTokens).toArray(String[]::new) : ArrayUtils.toArray());
    }

    public static Stream<String> streamTokens(@Nullable String str) {
        return ((str != null) ? Stream.of(org.springframework.util.StringUtils.tokenizeToStringArray(str, TOKEN_DELIMS)) : Stream.empty());
    }

    private static void splitCamelCase(String str, @Nonnegative int strLen, List<String> strParts) {
        char[] strChars = str.toCharArray();
        int lastStrPartStartIndex = 0, strPartStartIndex, lastStrCharType = Character.getType(strChars[0]), strCharType;

        for (int a = 1; a < strLen; a++) {
            if (((strCharType = Character.getType(strChars[a])) == Character.LOWERCASE_LETTER) && (lastStrCharType == Character.UPPERCASE_LETTER) &&
                ((strPartStartIndex = (a - 1)) != lastStrPartStartIndex)) {
                strParts.add(new String(strChars, lastStrPartStartIndex, (strPartStartIndex - lastStrPartStartIndex)));

                lastStrPartStartIndex = strPartStartIndex;
            }

            lastStrCharType = strCharType;
        }

        if (lastStrPartStartIndex < (strLen - 1)) {
            strParts.add(new String(strChars, lastStrPartStartIndex, (strLen - lastStrPartStartIndex)));
        }
    }
}
