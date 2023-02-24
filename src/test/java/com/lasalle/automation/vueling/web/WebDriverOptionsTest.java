package com.lasalle.automation.vueling.web;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Set;

public class WebDriverOptionsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static WebDriver driver;

    @Test
    public void testWebDrives() throws InterruptedException
    {
        LOGGER.debug("start testWebDrive");

        System.setProperty ("webdriver.chrome.driver","C:\\Users\\crist\\elkfaltava\\chromedriver_win32\\chromedriver.exe" );
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        LOGGER.debug("driver started");

        driver.get("https://www.vueling.com/es/");
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();

        //guardando primer tab
        String firstTab = driver.getWindowHandle();

        //aeropuertos
        driver.findElement(By.cssSelector("#tab-search > div > div.form-group.form-group--flight-search > vy-airport-selector.form-input.origin")).click(); //not necessary
        WebElement aorigen =  driver.findElement(By.id("originInput")); //not necessary
        aorigen.sendKeys("Barcelona"); //not necessary
        driver.findElement(By.cssSelector("#popup-list > vy-airports-li > li")).click(); //not necessary
        driver.findElement(By.cssSelector("#tab-search > div > div.form-group.form-group--flight-search > vy-airport-selector.form-input.destination")).click();
        WebElement adestino =  driver.findElement(By.id("destinationInput"));
        adestino.sendKeys("Madrid");
        driver.findElement(By.cssSelector("#popup-list > vy-airports-li > li.liStation")).click();

        //fechas
        driver.findElement(By.cssSelector("#oneWayLabel > span.txt-s")).click();
        WebElement fecha = null;
        while (fecha == null) {
            try {
                fecha = driver.findElement(By.id("calendarDaysTable202351"));
                fecha.click();
            } catch (Exception e) {
                WebElement botonPasarMes = driver.findElement(By.id("nextButtonCalendar"));
                botonPasarMes.click();
            }
        }
        driver.findElement(By.id("btnSubmitHomeSearcher")).click();

        //cambiar de pestaña
        Set<String> handles = driver.getWindowHandles();
        handles.remove(firstTab);
        String secondTab = handles.iterator().next();
        driver.close(); //cerramos la pestaña de booking
        driver.switchTo().window(secondTab);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("outboundFlightCardsContainer")));
        WebElement viaje = driver.findElement(By.id("flightCardContent"));
        Assert.assertEquals(viaje.isDisplayed(), true);

        driver.close();
        LOGGER.debug("driver closed");
    }
}