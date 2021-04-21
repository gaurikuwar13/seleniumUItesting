package com.webdriverio.uitests;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class TestData {

    /**
     * Test data to verify the sublist under any main menu in left pane
     * @return
     */
    private static Stream<Arguments> sublist() {
        List<String> protocolList = new ArrayList<>();
        protocolList.add("WebDriver Protocol");
        protocolList.add("Appium");
        protocolList.add("Mobile JSON Wire Protocol");
        protocolList.add("Chromium");
        protocolList.add("Sauce Labs");
        protocolList.add("Selenium Standalone");
        protocolList.add("JSON Wire Protocol");

        List<String> mockList = new ArrayList<>();
        mockList.add("abort");
        mockList.add("abortOnce");
        mockList.add("clear");
        mockList.add("respond");
        mockList.add("respondOnce");
        mockList.add("restore");

        return Stream.of(
                Arguments.of("Protocols", protocolList),
                Arguments.of("mock", mockList)
        );
    }

    /**
     * Test data to verify the search values
     * @return
     */
    public static Stream<Arguments> searchList(){
        return Stream.of(
                Arguments.of("click", "API"),
                Arguments.of("click","element")
        );
    }
}
