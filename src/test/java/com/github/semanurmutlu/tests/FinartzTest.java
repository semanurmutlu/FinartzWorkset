package com.github.semanurmutlu.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FinartzTest {

    WebDriver webDriver;
    WebDriverWait webDriverWait;
    ChromeOptions chromeOptions;
    List<String> expectedSubTitles;
    List<String> subTitles;

    @Before
    public void initWebDriver() {

        System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");

        expectedSubTitles= new ArrayList<String>(Arrays.asList ("Financial Services","Blockchain Services","Advisory Services"));
        webDriver = new ChromeDriver();
        webDriver.manage().window().fullscreen();
        webDriverWait = new WebDriverWait(webDriver,120);
    }



    @Test
    public void testMain() throws InterruptedException {


        // Task 1
        homePageTest();
        assertEquals("https://www.finartz.com/",webDriver.getCurrentUrl());

        // Task 2 & 3
        try {
            subTitles = solutionPageTest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(String subTitle : subTitles) {
            System.out.println("   " + subTitle);
        }
        assertEquals("https://www.finartz.com/solutions.html",webDriver.getCurrentUrl());
        Assert.assertArrayEquals(expectedSubTitles.toArray(),subTitles.toArray());

        //Task 4
       blogPageTest();
       assertEquals("https://blog.finartz.com/",webDriver.getCurrentUrl());

       // Task 5
       searchPageTest();
       assertEquals("https://blog.finartz.com/search?q=Financial%20Services",webDriver.getCurrentUrl());


    }

    public void homePageTest(){
        System.out.println("--> HomePage Test <--");
        webDriver.get("https://www.finartz.com/");

    }

    public List<String> solutionPageTest() throws InterruptedException {
        System.out.println("--> Solution Test <--");

        WebElement webElement = webDriver.findElement(By.partialLinkText("Solutions"));
        JavascriptExecutor executor = (JavascriptExecutor)webDriver;
        executor.executeScript("arguments[0].click();", webElement);
        webDriverWait.until(ExpectedConditions.titleContains("Solutions"));
        List<WebElement> subTitlesSolutions = webDriver.findElements(By.xpath("//h2[@class='title section-title has-text-centered dark-text']"));
        subTitles = new ArrayList<String>();
        for(WebElement subtitle : subTitlesSolutions){
            subTitles.add(subtitle.getText());
        }
        return subTitles;

    }


    public void blogPageTest() throws InterruptedException {
        System.out.println("--> Blog Test <--");

        WebElement blog = webDriver.findElement(By.partialLinkText("Blog"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", blog);
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        webDriverWait.until(ExpectedConditions.titleIs("Finartz"));
    }

    public void searchPageTest() {

        WebElement searchButton = webDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[2]/div/nav/div[1]/label/span"));
        searchButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[2]/div/nav/div[1]/label/input")));
        WebElement searchBox = webDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[3]/div[2]/div/nav/div[1]/label/input"));
        searchBox.sendKeys(expectedSubTitles.get(0));
        //System.out.println(expectedSubTitles.get(0));
        searchBox.sendKeys(Keys.RETURN);

        webDriverWait.until(ExpectedConditions.titleContains(expectedSubTitles.get(0)));
    }
    @After
    public void tearDown() {
        webDriver.quit();
    }
}
