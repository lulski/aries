package com.lulski.aries.post;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class PostUtilTests {

    @Test
    void whenSentenceContainSpaces_SpacesBecomesDashes() {
        var withSpaces = "Volvox Boiler Room";
        var expected = "Volvox-Boiler-Room";
        var becomeDashes = PostUtil.sanitizeTitleForURL(withSpaces);

        assertEquals(expected, becomeDashes);
    }

    @Test
    void whenSentenceContainExtraSpaces_extraSpaceGetsRemoved_andBecomesDash() {
        var withSpaces = "Despite—the—challenges—she  persevered and achieved her goals.";
        var expected = "Despite—the—challenges—she-persevered-and-achieved-her-goals.";
        var becomeDashes = PostUtil.sanitizeTitleForURL(withSpaces);

        assertEquals(expected, becomeDashes);
    }

    @Test
    void whenSentenceContainsDashes_dashesBecomesSpaces() {
        var originalValue = "Despite-the-challenges-she-persevered-and-achieved-her-goals.";
        var expected = "Despite the challenges she persevered and achieved her goals.";
        var result = PostUtil.sanitizeTitleForURL_reverse(originalValue);

        assertEquals(expected, result);
    }

    @Test
    void whenSentenceContainsTwoDashes_dashesBecomesSpaces() {
        var originalValue = "Despite--the-challenges--she-persevered-and--achieved-her--goals.";
        var expected = "Despite the challenges she persevered and achieved her goals.";
        var result = PostUtil.sanitizeTitleForURL_reverse(originalValue);

        assertEquals(expected, result);
    }

}
