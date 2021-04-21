package com.webdriverio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This a class file representing the API page.
 */
public class APIPage extends GenericComponents {

    //static locators of APIPage
    private static By leftPaneMenuMainItem = By.xpath("//a[@class ='menu__link menu__link--sublist']");
    private static By parentNode = By.xpath(".//parent::li");


    public APIPage( WebDriver driver ) {
        super(driver);
    }

    /**
     * This is a wrapper function to get the list of all submenu items of any selected parent item.
     * It would return null if the main menu does not have a submenu .For eg :- Expect
     *
     * @param parentMenu
     * @return
     */
    public List<String> subMenuList(String parentMenu) {
        List<String> subMenuNames = new ArrayList<>();
        try{
            List<WebElement> parentItemList = driver.findElements(leftPaneMenuMainItem);
            WebElement requiredItem = parentItemList.stream()
                    .filter(navigationItem -> navigationItem.getText().trim().equalsIgnoreCase(parentMenu))
                    .reduce((a,b) -> {
                        throw new IllegalArgumentException(String.format("More than one item with same name found in navigation list",
                                parentMenu));
                    })
                    .orElseThrow(() -> new NotFoundException(String.format("The required label {} is not found",parentMenu)));
            waitForCondition(timeUnit,2000,100,requiredItem,(input)->
                    (input.isDisplayed() && input.isEnabled()));
            WebElement parentNodeElement = requiredItem.findElement(parentNode);
            List<WebElement> subMenuList = parentNodeElement.findElements(By.xpath(".//li"));
            subMenuList.forEach( subMenuElement -> subMenuNames.add(subMenuElement.getText()));
            return subMenuNames;
        } catch(NoSuchElementException | NotFoundException exception){
            logger.info("The main menu does not have a sublist");
            return subMenuNames;
        }
    }

    /**
     * Click on the main menu withing the navigation pane
     * @Note This method would not check if the menu is in open or collapsed mode.
     * @param mainMenu
     */
    public WebElement selectMainMenuLeftNavigation(String mainMenu) {
        List<WebElement> leftPaneMainMenuList = driver.findElements(leftPaneMenuMainItem);
        WebElement selectedMainItem =leftPaneMainMenuList.stream()
                .filter(mainMenuItem ->
                        mainMenuItem.getText().trim().equalsIgnoreCase(mainMenu))
                .reduce((a,b) -> {
                    throw new IllegalArgumentException(String.format("More than one item with same name found in navigation list",
                            mainMenu));})
                .orElseThrow(() -> new NotFoundException(String.format("The required label {} is not found",mainMenu)));
        waitForCondition(timeUnit,500,100,selectedMainItem,(input)->(input.isEnabled()&& input.isDisplayed()));
        selectedMainItem.click();
        return selectedMainItem;
    }
}
