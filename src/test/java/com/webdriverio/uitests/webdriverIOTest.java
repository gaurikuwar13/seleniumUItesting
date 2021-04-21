package com.webdriverio.uitests;

import com.webdriverio.base.TestBase;
import com.webdriverio.pages.APIPage;
import com.webdriverio.pages.GenericComponents;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class webdriverIOTest extends TestBase {

    /**
     * This test case is to verify the navigation to the api page
     */
    @Test
    public void navigateToAPI() {
        GenericComponents genericComponents = new GenericComponents(driver);
        genericComponents.selectNavigationItem("API");
        assertThat(driver.getCurrentUrl())
                .as("API page is not open")
                .isEqualTo("https://webdriver.io/docs/api");
    }

    /**
     * This test case is to verify the sublist under each main menu item on the left navigation pane
     * It gets the test data from the test data file.
     * @param mainMenu
     * @param subMenuList
     */
    @ParameterizedTest
    @MethodSource("com.webdriverio.uitests.TestData#sublist")
    public void verifyProtocolList(String mainMenu, List<String> subMenuList) {

        GenericComponents genericComponents = new GenericComponents(driver);
        genericComponents.selectNavigationItem("API");
        assertThat(driver.getCurrentUrl())
                .as("API page is not open")
                .isEqualTo("https://webdriver.io/docs/api");
        APIPage apiPage = new APIPage(driver);
        apiPage.selectMainMenuLeftNavigation(mainMenu);
        assertThat(apiPage.subMenuList(mainMenu))
                .as(String.format("Options under {} are incorrect", "Protocols"))
                .isEqualTo(subMenuList);
    }

}
