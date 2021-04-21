package com.webdriverio.uitests;

import com.webdriverio.base.TestBase;
import com.webdriverio.pages.GenericComponents;
import com.webdriverio.pages.SearchPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends TestBase {

    @ParameterizedTest
    @MethodSource("com.webdriverio.uitests.TestData#searchList")
    /**
     * This a generic test to verify the search functionality based on the section and is parameterized to verify
     * multiple combination of search.
     * Searches for string in search window and selects the value based on the section passed by user
     * For eg :- verifies search string within "API" selection , searchString within
     */
    public void searchClickTest( String searchString, String searchSection ) {
        GenericComponents genericComponents = new GenericComponents(driver);
        genericComponents.selectNavigationItem("API");
        assertThat(driver.getCurrentUrl())
                .as("API page is not open")
                .isEqualTo( "https://webdriver.io/docs/api" );
        SearchPage searchPage = new SearchPage(driver);
        String searchValue = searchPage.selectSearchText(searchString, searchSection);
        //Generate a dynamic xpath to verify the contents of page based on selected value
        String dynamicXpath = String.format( "//*[contains(@class,'docMainContainer')]//*[text()='%s']", searchValue );
        logger.info( "The dynamic xpath is : {}", dynamicXpath );
        searchPage.waitForCondition(timeUnit, 4000, 300, null,
                (input) -> !driver.findElements(By.xpath(dynamicXpath)).isEmpty());
        //Verify that we are on right page by finding the element by id
        assertThat( driver.findElements(By.xpath( dynamicXpath)).isEmpty()).isFalse();
    }

    @Test
    /**
     * This test is to verify :-
     * The recent search appears by default in the search window
     * Clicking on recent search opens up the right page with correct information
     */
    public void verifyRecentSearch() throws InterruptedException {
        GenericComponents genericComponents = new GenericComponents( driver );
        genericComponents.selectNavigationItem( "API" );
        assertThat( driver.getCurrentUrl() )
                .as( "API page is not open" )
                .isEqualTo( "https://webdriver.io/docs/api" );
        SearchPage searchPage = new SearchPage(driver);
        String searchValue = searchPage.selectSearchText("click", "API");
        searchPage.waitForCondition(timeUnit, 4000, 300, null,
                (input) -> !driver.findElements(createDynamicXpath(searchValue)).isEmpty());
        //Verify that we are on right page by finding the element by id
        assertThat(driver.findElements(createDynamicXpath(searchValue)).isEmpty()).isFalse();
        searchPage = new SearchPage(driver);
        searchPage.clickSearch();
        String recentSearchValue =searchPage.verifyRecentSearch();
        searchPage.waitForCondition(timeUnit, 4000, 300, null,
                (input) -> !driver.findElements(createDynamicXpath(recentSearchValue)).isEmpty() );
        //Verify that we are on right page by finding the element by searchText
        assertThat( driver.findElements(createDynamicXpath(recentSearchValue)).isEmpty() ).isFalse();
    }

    @Test
    /**
     *This test :-
     * Adds a search value to the favorites section from recent search
     * Select item from Favorites and verify the page with right information is displayed.
     */
    public void favoriteSearch() {
        GenericComponents genericComponents = new GenericComponents( driver );
        genericComponents.selectNavigationItem( "API" );
        assertThat( driver.getCurrentUrl() )
                .as( "API page is not open" )
                .isEqualTo( "https://webdriver.io/docs/api" );
        SearchPage searchPage = new SearchPage(driver);
        String searchValue = searchPage.selectSearchText("click", "API");
        searchPage.waitForCondition(timeUnit, 4000, 300, null,
                (input) -> !driver.findElements(createDynamicXpath(searchValue)).isEmpty());
        //Verify that we are on right page by finding the element by id
        assertThat(driver.findElements(createDynamicXpath(searchValue)).isEmpty()).isFalse();
        searchPage = new SearchPage(driver);
        searchPage.clickSearch();
        String favoriteSearch = searchPage.addFavoriteSearch();
        searchPage.waitForCondition(timeUnit, 4000, 300, null,
                (input) -> !driver.findElements(createDynamicXpath(favoriteSearch)).isEmpty() );
        //Verify that we are on right page by finding the element by searchText
        assertThat(driver.findElements(createDynamicXpath(favoriteSearch)).isEmpty()).isFalse();

    }

    @Test
    public void verifyRandomSearchString() {
        SearchPage searchPage = new SearchPage(driver);
        String randomString = "RandomString" + RandomStringUtils.randomAlphanumeric(6);
        searchPage.enterSearchText(randomString);
        searchPage.waitForCondition(timeUnit, 4000, 300, null,
                (input) -> driver.findElements(searchPage.noResultsSearchLocator).stream().findFirst().isPresent());
        assertThat(!driver.findElements(searchPage.noResultsSearchLocator).isEmpty())
                .isTrue();


    }
    /**
     * This method dynamically creates a Xpath to search the searchText on the mainContainer of the page
     * @param searchValue
     * @return
     */
    public By createDynamicXpath(String searchValue) {
        return By.xpath(String.format( "//*[contains(@class,'docMainContainer')]//*[text()='%s']", searchValue));
    }
}