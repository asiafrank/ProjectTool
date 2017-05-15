package com.asiafrank.tools.util;

/**
 * @author user created at 5/15/2017.
 */
public class Strings {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Determines whether or not the string 'searchIn' contains the string
     * 'searchFor', dis-regarding case starting at 'startAt' Shorthand for a
     * String.regionMatch(...)
     *
     * @param searchIn
     *            the string to search in
     * @param startAt
     *            the position to start at
     * @param searchFor
     *            the string to search for
     *
     * @return whether searchIn starts with searchFor, ignoring case
     */
    public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor) {
        return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
    }

    /**
     * Finds the position of a substring within a string ignoring case.
     *
     * @param searchIn
     *            the string to search in
     * @param searchFor
     *            the array of strings to search for
     * @return the position where <code>searchFor</code> is found within <code>searchIn</code> starting from <code>startingPosition</code>.
     */
    public static int indexOfIgnoreCase(String searchIn, String searchFor) {
        return indexOfIgnoreCase(0, searchIn, searchFor);
    }

    /**
     * Finds the position of a substring within a string ignoring case.
     *
     * @param startingPosition
     *            the position to start the search from
     * @param searchIn
     *            the string to search in
     * @param searchFor
     *            the array of strings to search for
     * @return the position where <code>searchFor</code> is found within <code>searchIn</code> starting from <code>startingPosition</code>.
     */
    public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor) {
        if ((searchIn == null) || (searchFor == null)) {
            return -1;
        }

        int searchInLength = searchIn.length();
        int searchForLength = searchFor.length();
        int stopSearchingAt = searchInLength - searchForLength;

        if (startingPosition > stopSearchingAt || searchForLength == 0) {
            return -1;
        }

        // Some locales don't follow upper-case rule, so need to check both
        char firstCharOfSearchForUc = Character.toUpperCase(searchFor.charAt(0));
        char firstCharOfSearchForLc = Character.toLowerCase(searchFor.charAt(0));

        for (int i = startingPosition; i <= stopSearchingAt; i++) {
            if (isCharAtPosNotEqualIgnoreCase(searchIn, i, firstCharOfSearchForUc, firstCharOfSearchForLc)) {
                // find the first occurrence of the first character of searchFor in searchIn
                while (++i <= stopSearchingAt && (isCharAtPosNotEqualIgnoreCase(searchIn, i, firstCharOfSearchForUc, firstCharOfSearchForLc))) {
                }
            }

            if (i <= stopSearchingAt && startsWithIgnoreCase(searchIn, i, searchFor)) {
                return i;
            }
        }

        return -1;
    }

    private static boolean isCharAtPosNotEqualIgnoreCase(String searchIn, int pos, char firstCharOfSearchForUc, char firstCharOfSearchForLc) {
        return Character.toLowerCase(searchIn.charAt(pos)) != firstCharOfSearchForLc && Character.toUpperCase(searchIn.charAt(pos)) != firstCharOfSearchForUc;
    }

    private static boolean isCharEqualIgnoreCase(char charToCompare, char compareToCharUC, char compareToCharLC) {
        return Character.toLowerCase(charToCompare) == compareToCharLC || Character.toUpperCase(charToCompare) == compareToCharUC;
    }
}
