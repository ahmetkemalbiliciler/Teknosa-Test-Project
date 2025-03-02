package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.time.Duration;

public class PriceCheckBot {

    private WebDriver driver;
    private WebDriverWait wait;
    private final int TIMEOUT = 30;


    double[][] priceRanges = {
            {0.0, 25.0}, {25.0, 50.0}, {50.0, 100.0}, {100.0, 200.0},
            {200.0, 500.0}, {500.0, 1000.0}, {1000.0, 2500.0}, {2500.0, 5000.0},
            {5000.0, 7500.0}, {7500.0, 10000.0}, {10000.0, 15000.0}, {15000.0, 20000.0},
            {20000.0, 25000.0}, {25000.0, 30000.0}, {30000.0, Double.MAX_VALUE}
    };
    double[][] rateRanges = {
            {4.0, 5.0}, {3.0, 3.9}, {2.0,2.9},{1.0, 1.9}
    };

    public PriceCheckBot() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public void fullscreen() {
        driver.manage().window().fullscreen();
    }


    public void navigateToPage(String url) {
        driver.get(url);
    }

    public void selectPriceRange(int index) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String checkboxId = "price" + index;
        WebElement checkbox = driver.findElement(By.id(checkboxId));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
    }

    public void selectIncreasingPrice(){
        String buttonId="price-asc";
        WebElement button =driver.findElement(By.id(buttonId));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }
    public void selectDescendingPrice(){
        String buttonId="price-desc";
        WebElement button=driver.findElement(By.id(buttonId));
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",button);
    }



    public void waitScroll() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".prc.prc-last")));
    }

    public List<Double> extractPrices() {
        List<Double> prices = new ArrayList<>();
        List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".prc.prc-last")));

        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace(" TL", "").trim();
            double price = parseNumber(priceText);
            if (price != 0) {
                prices.add(price);
            }
        }
        return prices;
    }

    private double parseNumber(String formattedNumber) {
        try {
            String cleanNumber = formattedNumber.replace(".", "").replace(",", ".");
            return Double.parseDouble(cleanNumber);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz fiyat formatı: " + formattedNumber);
            return 0;
        }
    }

    public boolean validatePrices(List<Double> prices, double minPrice, double maxPrice) {
        for (double price : prices) {
            System.out.println("Price: " + price);
            if (price < minPrice || price > maxPrice) {
                System.out.println("Price " + price + " is out of range!");
                return false;
            }
        }
        return true;
    }

    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public boolean allProductsInPriceRange(int priceRangeIndex) {
        double minPrice = priceRanges[priceRangeIndex][0];
        double maxPrice = priceRanges[priceRangeIndex][1];


        List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".prc.prc-last")));

        List<Double> prices = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace(" TL", "").trim();
            double price = parseNumber(priceText);
            if (price != 0) {
                prices.add(price);
            }
        }

        return validatePrices(prices, minPrice, maxPrice);
    }

    public boolean isSortedAscending(List<Double> prices) {
        if (prices == null || prices.isEmpty()) {
            return true;
        }
        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);

        return prices.equals(sortedPrices);

    }
    public boolean isPageAscendingOrder() {
        List<Double> prices = extractPrices();
        if (isSortedAscending(prices)) {

            return true;
        } else {

            return false;
        }
    }

    public boolean isSortedDescending(List<Double> prices){
        if (prices == null || prices.isEmpty()) {
            return true;
        }
        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices, Comparator.reverseOrder());

        return prices.equals(sortedPrices);
    }
    public boolean isPageDescendingOrder() {
        List<Double> prices = extractPrices();
        if (isSortedDescending(prices)) {

            return true;
        } else {

            return false;
        }
    }

    public boolean checkAscendingOrder() {
        By buttonLocator = By.cssSelector(".btn.btn-extra.plp-paging-load-more");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 3100)");
        int i =1;
        while (true) {
            try {
                Thread.sleep(2000);
                List<WebElement> buttons = driver.findElements(buttonLocator);
                if (buttons.isEmpty()) {

                    break;
                }
                if (!isPageAscendingOrder()){
                    System.out.println("Page "+i+ "is invalid ordered" );
                    return false;}
                WebElement button = buttons.get(0);
                if (button.isDisplayed() && button.isEnabled()) {

                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

                    Thread.sleep(2000);

                    js.executeScript("window.scrollTo(0, 5600)");
                } else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return true;
    }



    public boolean checkDescendingOrder() {
        By buttonLocator = By.cssSelector(".btn.btn-extra.plp-paging-load-more");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 3100)");
        int i =1;
        while (true) {
            try {
                Thread.sleep(2000);
                List<WebElement> buttons = driver.findElements(buttonLocator);
                if (buttons.isEmpty()) {

                    break; // Buton artık bulunamazsa döngüyü sonlandır
                }
                if (!isPageDescendingOrder()){
                    System.out.println("Page "+i+ "is inavalid ordered" );
                    return false;}
                WebElement button = buttons.get(0);
                if (button.isDisplayed() && button.isEnabled()) {

                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

                    Thread.sleep(2000);

                    js.executeScript("window.scrollTo(0, 5600)");
                } else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return true;
    }


    public void selectRateRange(int index) {
        try {

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String checkboxId = "reviewAvgRatingRange" + index;
        WebElement checkbox = driver.findElement(By.id(checkboxId));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
    }


    public boolean extractRates(int rateRangeIndex) {
        List<Double> rates = new ArrayList<>();

        double minRate = rateRanges[rateRangeIndex][0];
        double maxRate = rateRanges[rateRangeIndex][1];


        List<WebElement> rateElements = driver.findElements(By.cssSelector(".readstars"));

        for (WebElement rateElement : rateElements) {
            double rate=Double.parseDouble(rateElement.getText());

            if (rate > 0) {
                rates.add(rate);
            }
        }
        return validateRates(rates,minRate,maxRate);
    }

    public boolean validateRates(List<Double> rates, double minRate, double maxRate) {
        for (double rate : rates) {
            System.out.println("Rate: " + rate);
            if (rate < minRate || rate > maxRate) {
                System.out.println("Rate: " + rate + " is out of range!");
                return false;
            }
        }
        return true;
    }

    public boolean checkValidRateRange( int rateIndex) {
        By buttonLocator = By.cssSelector(".btn.btn-extra.plp-paging-load-more");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int i =1;
        while (true) {
            try {
                Thread.sleep(2000);
                List<WebElement> buttons = driver.findElements(buttonLocator);
                if (buttons.isEmpty()) {
                    break;
                }

                if (!extractRates(rateIndex)){
                    System.out.println("Page "+i+ "is inavalid ordered" );
                    return false;}
                WebElement button = buttons.get(0);

                if (button.isDisplayed() && button.isEnabled()) {

                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

                    Thread.sleep(2000);


                } else {

                    break;

                }

            } catch (Exception e) {

                break;
            }
            i++;
        }
        return true;
    }

    public void quit() {
        driver.quit();
    }
}

