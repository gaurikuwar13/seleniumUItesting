package com.webdriverio.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBase {

    @Getter
    protected TestConfiguration testConfiguration;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    public WebDriver driver;
    public TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    @BeforeEach
    public void before() throws IOException {
        this.testConfiguration = new TestConfiguration();
        //Analyze why FIle does not accept the relative path
        File file = new File("/Users/gkulkarni/OneDrive - ETQ, LLC/seleniumUItesting/src/test/resources/application.yml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        TestConfiguration config = objectMapper.readValue(file, TestConfiguration.class);
        System.out.println("Test config info " + config.getBrowser());
        initiateDriver(config.getBrowser(), config.getDriverPath(), config.getHostname());
    }

    public WebDriver initiateDriver(String browser, String driverPath, String hostname) {
        logger.info("Initiate the driver and return the driver instance");
        switch(browser.trim().toUpperCase()) {

            case "FIREFOX":
                System.setProperty("webdriver.gecko.driver", driverPath);
                driver = new FirefoxDriver();
                driver.get(hostname);
            //Add cases for all supported browsers here
        }
        return driver;
    }

    @AfterEach
    public void after() {
        driver.quit();
    }
}
