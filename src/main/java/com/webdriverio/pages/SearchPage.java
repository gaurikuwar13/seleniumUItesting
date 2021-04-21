package com.webdriverio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SearchPage extends GenericComponents {

    public SearchPage(WebDriver driver) {
        super(driver);
    }
    //locators to find the Search locator
    private static By searchButtonLocator = By.className("DocSearch-Button-Container");
    private static By searchDialogLocator = By.className("DocSearch-Modal");
    private static By searchBoxLocator = By.id("docsearch-input");
    private static By searchSection = By.cssSelector("section[class = 'DocSearch-Hits']");
    private static By searchSectionTextLocator = By.cssSelector("div[class = 'DocSearch-Hit-source']");
    private static By searchItemLocator = By.className("DocSearch-Hit-title");
    private static By searchContainerLocator = By.cssSelector("div[class = 'DocSearch-Dropdown-Container']");
    private static By selectFavoriteLocator = By.cssSelector("button[title='Save this search']");
    public static By noResultsSearchLocator = By.cssSelector("div[class='DocSearch-NoResults-Prefill-List'] li");



    /**
     * getter for searchButtonLocator
     * @return
     */
    public WebElement getSearchButton() {
        return driver.findElement(searchButtonLocator);
    }

    public WebElement getSearchDialogBox() {
        return driver.findElement(searchDialogLocator);
    }

    public WebElement getSearchBoxLocator() {
        return driver.findElement(searchBoxLocator);
    }

    public List<WebElement> getSearchSelection() {
        return driver.findElements(searchSection);
    }

    public WebElement getSearchContainerLocator() {
        return driver.findElement(searchContainerLocator);
    }

    /**
     * Click on the search box to open the search dialog
     */
    public void clickSearch() {
        waitForCondition(timeUnit,500,100,getSearchButton(),(input)-> input.isDisplayed()
                && input.isEnabled());
        getSearchButton().click();
        waitForCondition(timeUnit,500,100,getSearchDialogBox(),(input) -> input.isEnabled()&&
                input.isDisplayed());
    }

    /**
     * Enter the click the search string
     * @param searchString
     */
    public void enterSearchText(String searchString) {
        clickSearch();
        getSearchBoxLocator().clear();
        getSearchBoxLocator().sendKeys(searchString);
        getSearchBoxLocator().sendKeys( Keys.ENTER);
    }

    /**
     * Select the first element in the search from suggested section
     * For eg :- API, tobeClickable
     * @param searchString
     * @param searchSection
     * @return
     */
    public String selectSearchText(String searchString, String searchSection) {
        enterSearchText(searchString);
        WebElement selectionSection = filterSearch(searchSection);
        WebElement selectedLabelElement = selectionSection.findElements(searchItemLocator).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("No item in list"));
        String selectedElementText = selectedLabelElement.getText();
        logger.info("The text is : {}",selectedElementText);
        selectedLabelElement.click();
        return selectedElementText;
    }

    /**
     * This method is to filter the webElement to be searched so that user can perforrm actions
     * like add to favorites etc on it.
     * @param searchSection
     * @return
     */
    public WebElement filterSearch(String searchSection){
        waitForCondition(timeUnit,5000,300,null,
                (input)-> driver.findElement(searchContainerLocator).isDisplayed());
        WebElement selectionSection = getSearchSelection().stream()
                .filter(selection -> selection.findElement(searchSectionTextLocator).getText().trim()
                        .equalsIgnoreCase(searchSection))
                .findFirst()
                .orElseThrow(()-> new NotFoundException(String.format("The required label {} is not found",
                        searchSection)));
        return selectionSection;
    }

    /**
     * This method verifies the search options under recent searches
     * @return
     */
    public String verifyRecentSearch() {
        WebElement recentSelectionSection = filterSearch("Recent");
        WebElement selectedLabelElement = recentSelectionSection.findElements(searchItemLocator).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("No item in list"));
        String selectedElementText = selectedLabelElement.getText();
        logger.info("The text is : {}",selectedElementText);
        selectedLabelElement.click();
        return selectedElementText;
    }

    public String addFavoriteSearch() {
        WebElement recentSelectionSection = filterSearch("Recent");
        WebElement selectedLabelElement = recentSelectionSection.findElements(searchItemLocator).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("No item in list"));
        String selectedElementText = selectedLabelElement.getText();
        recentSelectionSection.findElement(selectFavoriteLocator).click();
        WebElement favoriteSection = filterSearch("Favorites");
        WebElement favoriteLabelElement = favoriteSection.findElements(searchItemLocator).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("No item in list"));
        String favoriteElementText = favoriteLabelElement.getText();
        assertThat(selectedElementText.trim()).isEqualTo(favoriteElementText);
        favoriteLabelElement.click();
        return favoriteElementText;
    }
}
