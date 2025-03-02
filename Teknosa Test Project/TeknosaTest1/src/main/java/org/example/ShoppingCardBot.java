package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ShoppingCardBot {
    private WebDriver driver;
    private WebDriverWait wait;
    private final int TIMEOUT = 30;  // Timeout süresini artırdık

    // Fiyat aralıkları

    public ShoppingCardBot() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public void navigateToPage(String url) {
        driver.get(url);
    }
    public void openLoginScreen(){
        //String username, String password
        JavascriptExecutor js = (JavascriptExecutor) driver;
        By OptionButtonLocator= By.cssSelector(".btn-user");
        WebElement OptionButton= driver.findElement(OptionButtonLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OptionButton);


        By LoginButtonLocator= By.cssSelector(" .homeCallLogin");
        WebElement LoginButton= driver.findElement(LoginButtonLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", LoginButton);


        waitForPageLoad();
    }

    public void enterEmail(String email){

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.setTimeout(function() { }, 2000);"); // 2 saniye
        By UsernameInputLocator = By.id("j_username1");
        WebElement UsernameInput = driver.findElement(UsernameInputLocator);
        UsernameInput.sendKeys(email);
        js.executeScript("window.setTimeout(function() { }, 1000);"); // 2 saniye
        By ContinueButtonLocator = By.id("newLoginStepSecond");
        WebElement ContinueButton = driver.findElement(ContinueButtonLocator);
        js.executeScript("arguments[0].click();", ContinueButton);
        try {
            
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void enterPassword(String password){

        JavascriptExecutor js =(JavascriptExecutor) driver;
        js.executeScript("window.setTimeout(function() { }, 2000);"); // 2 saniye
        By PasswordInputLocator =By.id("j_password");
        WebElement PasswordInput=driver.findElement(PasswordInputLocator);
        waitForPageLoad();
        js.executeScript("arguments[0].value='" + password + "';", PasswordInput);
        js.executeScript("window.setTimeout(function() { }, 1000);"); // 1 saniye
        By ContinueButtonLocator = By.id("customerLoginButton");
        WebElement ContinueButton = driver.findElement(ContinueButtonLocator);
        js.executeScript("arguments[0].click();", ContinueButton);


    }

    public void openShoppingCard(){

        int time = 10;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));


        By CardButtonLocator = By.cssSelector("button.mini-cart-link");
        WebElement CardButton = wait.until(ExpectedConditions.visibilityOfElementLocated(CardButtonLocator));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", CardButton);

        waitForPageLoad();
        try {

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        By CardShowButtonLocator= By.cssSelector(".mmc-cta");

        WebElement CardShowButton= driver.findElement(CardShowButtonLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", CardShowButton);


    }


    public boolean verifyCartTotal() {
        double totalPriceCalculated = 0.0;


        List<WebElement> cartRows = driver.findElements(By.cssSelector(".cart-row"));

        for (WebElement row : cartRows) {

            WebElement priceElement = row.findElement(By.cssSelector("[name='priceWithDiscount']"));
            WebElement quantityElement = row.findElement(By.cssSelector("[name='productQuantity']"));
            double price = Double.parseDouble(priceElement.getAttribute("value").replace(".", "").replace(",", "."));
            int quantity = Integer.parseInt(quantityElement.getAttribute("value"));

            totalPriceCalculated += price * quantity;
        }


        WebElement totalPriceElement = driver.findElement(By.cssSelector(".cart-sum-table td"));
        String priceText = totalPriceElement.getText().replace(" TL", "").trim();
        double totalPriceDisplayed = Double.parseDouble(priceText.replace(".", "").replace(",", "."));


        System.out.println("totalPriceCalculated= "+totalPriceCalculated/10);
        System.out.println("totalPriceDisplayed= "+totalPriceDisplayed);
        return totalPriceCalculated/10 == totalPriceDisplayed;
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




    public void waitForPageLoad() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.setTimeout(function() { }, 5000);"); // 2 saniye
    }

    public void quit() {
        driver.quit();
    }

    public void checkQuantityAgainstMaxValuePlusOne() {

        By quantityInputLocator = By.id("quantity_0");
        WebElement quantityInput = driver.findElement(quantityInputLocator);


        int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
        int maxValue = Integer.parseInt(quantityInput.getAttribute("maxvalue"));


        JavascriptExecutor js = (JavascriptExecutor) driver;
        quantityInput.sendKeys(Keys.BACK_SPACE);
        js.executeScript("arguments[0].value='" + maxValue+1 + "';", quantityInput);
        quantityInput.sendKeys(Keys.ENTER);
    }
    public void checkQuantityAgainstMaxValue() {

        By quantityInputLocator = By.id("quantity_0");
        WebElement quantityInput = driver.findElement(quantityInputLocator);


        int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
        int maxValue = Integer.parseInt(quantityInput.getAttribute("maxvalue"));


        JavascriptExecutor js = (JavascriptExecutor) driver;
        quantityInput.sendKeys(Keys.BACK_SPACE);
        js.executeScript("arguments[0].value='" + maxValue + "';", quantityInput);
        quantityInput.sendKeys(Keys.ENTER);
    }
    public void checkQuantityValue(int value) {

        By quantityInputLocator = By.id("quantity_0");
        WebElement quantityInput = driver.findElement(quantityInputLocator);

        int quantity = Integer.parseInt(quantityInput.getAttribute("value"));
        int maxValue = Integer.parseInt(quantityInput.getAttribute("maxvalue"));


        JavascriptExecutor js = (JavascriptExecutor) driver;
        quantityInput.sendKeys(Keys.BACK_SPACE);
        js.executeScript("arguments[0].value='" + value + "';", quantityInput);
        quantityInput.sendKeys(Keys.ENTER);
    }

    public String getMaxQuantityMessage() {

        By messageLocator = By.cssSelector("#mnp-max-quantity .icn-body");
        wait.until(ExpectedConditions.visibilityOfElementLocated(messageLocator));
        WebElement messageElement = driver.findElement(messageLocator);
        try {

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return messageElement.getText();
    }

    public boolean isNegativeAlertPresent() {

        By alertLocator = By.id("js-global-error-message");

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPositiveAlertPresent() {

        By alertLocator = By.id("js-global-success-message");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmptyErrorMessagePresent(){
        By alertLocator = By.cssSelector(".cart-empty-info");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(alertLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
