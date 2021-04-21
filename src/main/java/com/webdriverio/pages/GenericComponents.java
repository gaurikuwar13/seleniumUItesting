package com.webdriverio.pages;

import com.webdriverio.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.awaitility.Awaitility.await;

/**
 * This is a class file which contains all the generic code to :-
 * -Navigate within different pages
 */
public class GenericComponents extends TestBase {

    protected WebDriver driver;

    //static locators to identify the elements
    private static By navigationBarItems = By.cssSelector("[class = 'navbar__item navbar__link']");

    public GenericComponents(WebDriver driver){
        this.driver = driver;
    }

    /**
     * This is a generic function to select an option on the navigation bar which could be used common across
     * all the pages
     * @param itemName The option to be selected
     */
    public void selectNavigationItem(String itemName) {
        List<WebElement> navigationItemList = driver.findElements(navigationBarItems);
        WebElement requiredItem = navigationItemList.stream()
                .filter(navigationItem -> navigationItem.getText().trim().equalsIgnoreCase(itemName))
                .reduce((a,b) -> {
                    throw new IllegalArgumentException(String.format("More than one item with same name found in navigation list",
                            itemName));
                })
                .orElseThrow(() -> new NotFoundException(String.format("The required label {} is not found",itemName)));
        waitForCondition(timeUnit,2000,100,requiredItem,(input)->
                (input.isDisplayed() && input.isEnabled()));
        requiredItem.click();
    }

    /**
     * This is a wrapper function for smart wait using awaitility. This would accept a lambda function as a condition
     * and be used to smart wait using polling.
     * @param timeUnit
     * @param waitTime
     * @param pollingTime
     * @param inputArg
     * @param condition
     * @param <V>
     */
    public static <V> void waitForCondition(TimeUnit timeUnit,long waitTime, long pollingTime,
                                            V inputArg,  Function<V,Boolean>condition) {
        await("Wait For the action")
                .atMost(waitTime, timeUnit )
                .pollInterval(pollingTime,timeUnit)
                .ignoreExceptions()
                .until(() -> condition.apply(inputArg));
    }
}
