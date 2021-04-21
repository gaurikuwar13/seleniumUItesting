package com.webdriverio.base;

/**
 * This is a testConfiguration file to read the setting from the application.yml file
 * This gives flexibility to user to update the browser, hostName at one place rather than all over the test cases
 */
public class TestConfiguration {
    public TestConfiguration() {}
    private String browser;
    private String hostname;
    private String driverPath;

    // getters to read the configuration values
    public String getBrowser(){
        return browser;
    }

    public String getHostname() {
        return hostname;
    }

    public String getDriverPath() {
        return driverPath;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setHostName(String hostname){
        this.hostname = hostname;
    }

    public void setDriverPath(String driverPath){
        this.driverPath = driverPath;
    }

}
