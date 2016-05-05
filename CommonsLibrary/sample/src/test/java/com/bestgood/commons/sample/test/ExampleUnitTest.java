package com.bestgood.commons.sample.test;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//        String verify_code = "3444";
//        String verify_code = "1_3444";
        String verify_code = "2_3444";
        String[] condition = verify_code.split("_");
        System.out.println(condition);
    }
}