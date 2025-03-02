package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCardBotTest {
    private ShoppingCardBot bot;

    @ParameterizedTest
    @CsvSource({
        "testoku123@gmail.com,Ozankemalutku64"
    })
    public void ShoppingCardTest(String email, String password){
        bot=new ShoppingCardBot();
        String baseUrl="https://www.teknosa.com";
        bot.navigateToPage(baseUrl);
        bot.openLoginScreen();
        bot.enterEmail(email);
        bot.waitForPageLoad();
        bot.enterPassword(password);
        bot.waitForPageLoad();
        bot.openShoppingCard();
        boolean result=bot.verifyCartTotal();
        bot.quit();
        assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            "testoku123@gmail.com,Ozankemalutku64"
    })
    public void ItemQuantityTestE1(String email, String password){
        bot=new ShoppingCardBot();
        String baseUrl="https://www.teknosa.com";
        bot.navigateToPage(baseUrl);
        bot.openLoginScreen();
        bot.enterEmail(email);
        bot.waitForPageLoad();
        bot.enterPassword(password);
        bot.waitForPageLoad();
        bot.openShoppingCard();
        bot.checkQuantityAgainstMaxValuePlusOne();
        boolean result= bot.isNegativeAlertPresent();
        assertTrue(result);

    }

    @ParameterizedTest
    @CsvSource({
            "testoku123@gmail.com,Ozankemalutku64"
    })
    public void ItemQuantityMaxTest(String email, String password){
        bot=new ShoppingCardBot();
        String baseUrl="https://www.teknosa.com";
        bot.navigateToPage(baseUrl);
        bot.openLoginScreen();
        bot.enterEmail(email);
        bot.waitForPageLoad();
        bot.enterPassword(password);
        bot.waitForPageLoad();
        bot.openShoppingCard();

        bot.checkQuantityAgainstMaxValue();
        boolean result= bot.isPositiveAlertPresent();
        assertTrue(result);

    }

    @ParameterizedTest
    @CsvSource({
            "testoku123@gmail.com,Ozankemalutku64 ,2",
            "testoku123@gmail.com,Ozankemalutku64 ,-1",
            "testoku123@gmail.com,Ozankemalutku64 ,0"
    })
    public void ItemQuantityTest(String email, String password,int quantity){
        bot=new ShoppingCardBot();
        String baseUrl="https://www.teknosa.com";
        bot.navigateToPage(baseUrl);
        bot.openLoginScreen();
        bot.enterEmail(email);
        bot.waitForPageLoad();
        bot.enterPassword(password);
        bot.waitForPageLoad();
        bot.openShoppingCard();

        if(quantity>0) {
            bot.checkQuantityValue(quantity);
            boolean result = bot.isPositiveAlertPresent();
            assertTrue(result);
        } else if (quantity==0) {
            bot.checkQuantityValue(quantity);
            boolean result = bot.isEmptyErrorMessagePresent();
            assertTrue(result);
        }if (quantity<0){
            bot.checkQuantityValue(quantity);
            boolean result = bot.isNegativeAlertPresent();
            assertTrue(result);
        }
        bot.quit();
    }





}