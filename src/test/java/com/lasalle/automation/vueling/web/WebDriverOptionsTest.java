package com.lasalle.automation.vueling.web;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * - Window: get, getTitle, getCurrentUrl, getPageSource, close, quit
 * - Navigate: to, back, forward, refresh
 * - FindElement & FindElements
 * switchTo: frame, alert, window…
 * WebElement: click, clear, findElement/s, getAttribute, getText, sendkeys…
 * Locators según preferencia:
 * By Id
 * By name
 * By css: https://saucelabs.com/resources/articles/selenium-tips-css-selectors
 * By xpath
 * Wait: implicitlyWait vs explicitWait (expected conditions)
 */
public class WebDriverOptionsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static WebDriver driver;

    @Test
    public void testWebDrives() throws InterruptedException
    {
        LOGGER.debug("start testWebDrive");

        System.setProperty ("webdriver.chrome.driver","/home/s2o/tmp/chromedriver_linux64/chromedriver" );
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        LOGGER.debug("driver started");

        driver.get("https://the-internet.herokuapp.com");
        driver.getTitle();
        driver.getCurrentUrl();
        driver.getPageSource();

        driver.navigate().to("https://the-internet.herokuapp.com/abtest");
        driver.navigate().back();
        driver.navigate().forward();
        driver.navigate().refresh();

        driver.navigate().to("https://the-internet.herokuapp.com");
        driver.findElement(By.linkText("JavaScript Alerts")).click();
        List<WebElement> buttons = driver.findElements(By.cssSelector("button"));
        buttons.get(0).click();
        driver.switchTo().alert().accept();
        buttons.get(1).click();
        driver.switchTo().alert().dismiss();

        // Esperas - Explícitas FluentWait
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");
        driver.findElement(By.cssSelector("#checkbox-example > button")).click();
        Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .pollingEvery(Duration.of(2, ChronoUnit.SECONDS))
                .ignoring(NoSuchElementException.class);
        WebElement fluentElement = fluentWait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                return webDriver.findElement(By.id("message"));
            }
        });
        String fluentElementText = fluentElement.getText();
        Assert.assertTrue(fluentElement.isDisplayed());
        LOGGER.debug("finish element, fluentElementText:[{}]", fluentElementText);

        // Esperas - Implícitas implicitlyWait
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");
        driver.findElement(By.cssSelector("#checkbox-example > button")).click();
        WebElement implicitWaitElement = driver.findElement(By.id("message"));
        String implicitWaitElementText = implicitWaitElement.getText();
        Assert.assertTrue(implicitWaitElement.isDisplayed());
        LOGGER.debug("finish element, implicitWaitElementText:[{}]", implicitWaitElementText);

        // Esperas - Explícitas WebDriverWait
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
        driver.findElement(By.cssSelector("#start > button")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        String text = element.getText();
        Assert.assertTrue(element.isDisplayed());
        LOGGER.debug("finish element, WebDriverWait, text:[{}]", text);

        driver.close();
        LOGGER.debug("driver closed");
    }

}
