package com.lulski.aries.post;

import java.util.StringTokenizer;

import org.springframework.util.StringUtils;

public class PostUtil {

    /**
     * Replace " "(space) in the given {@code String} into a "-" (dash)
     * {@code String}
     * 
     * @param input {@code String}
     * @return input without " " space character {@code String}
     * 
     */
    public static String sanitizeTitleForURL(String input) {

        String[] words = StringUtils.tokenizeToStringArray(input, " ");

        StringBuilder sBuilder = new StringBuilder();
        for (String string : words) {
            string = StringUtils.replace(string, " ", "-");

            if (sBuilder.length() > 0)
                sBuilder.append("-");
            sBuilder.append(string);
        }

        return sBuilder.toString();
    }

    /**
     * Replace "-"(space) in the given {@code String} into a " " (dash)
     * {@code String}
     * 
     * @param input {@code String}
     * @return input without "-" dash character {@code String}
     * 
     */
    public static String sanitizeTitleForURL_reverse(String title) {
        String[] words = StringUtils.tokenizeToStringArray(title, "-");

        StringBuilder sBuilder = new StringBuilder();
        for (String string : words) {
            string = StringUtils.replace(string, "-", " ");

            if (sBuilder.length() > 0)
                sBuilder.append(" ");
            sBuilder.append(string);
        }
        return sBuilder.toString();

    }

}
