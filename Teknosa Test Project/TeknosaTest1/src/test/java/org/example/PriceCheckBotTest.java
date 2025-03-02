package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PriceCheckBotTest {

    private PriceCheckBot bot;


    // Test for "Telefon" search
    @ParameterizedTest
    @CsvSource({
            "0, true, Telefon",
            "1, true, Telefon",
            "2, true, Telefon",
            "3, true, Telefon",
            "4, true, Telefon",
            "5, true, Telefon",
            "6, true, Telefon",
            "7, true, Telefon",
            "8, true, Telefon",
            "9, true, Telefon",
            "10, true, Telefon",
            "11, true, Telefon",
            "12, true, Telefon",
            "13, true, Telefon",
            "14, true, Telefon",
    })
    void testAllProductsInPriceRangeForTelephone(int priceRangeIndex, boolean expected,String searchedItem) {
        bot = new PriceCheckBot();
        bot.navigateToPage("https://www.teknosa.com/arama/?s="+searchedItem);
        bot.waitForPageLoad();

        bot.selectPriceRange(priceRangeIndex);

        boolean result = bot.allProductsInPriceRange(priceRangeIndex);
            bot.quit();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "0,  Telefon",
            "1,  Telefon",
            "2,  Telefon",
            "3, Telefon "

    })
    void testAllProductsInRateRangeForBilgisayarSearch(int priceRateIndex,String searchedItem) {
        bot = new PriceCheckBot();
        bot.navigateToPage("https://www.teknosa.com/arama/?s="+searchedItem);
        bot.waitForPageLoad();

        bot.selectRateRange(priceRateIndex);
        bot.waitForPageLoad();
        boolean result = bot.checkValidRateRange(priceRateIndex);
        bot.quit();
        assertTrue(result);
    }



    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, true",
            "2, true",
            "3, true",
            "4, true",
            "5, true",
            "6, true",
            "7, true",
            "8, true",
            "9, true",
            "10, true",
            "11, true",
            "12, true",
            "13, true",
            "14, true",

    })
    void testAllProductsInPriceRangeFor(int priceRangeIndex, boolean expected) {
        bot = new PriceCheckBot();

        bot.navigateToPage("https://www.teknosa.com/bilgisayar-tablet-c-116"); // Test etmek istediğiniz sayfaya gidin
        bot.waitForPageLoad();

        bot.selectPriceRange(priceRangeIndex);
        bot.waitForPageLoad();

        boolean result = bot.allProductsInPriceRange(priceRangeIndex);
        bot.quit();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
         "https://www.teknosa.com/tablet-c-116012"
    })
    void testIncreaseingPrice(String baseUrl){
        bot= new PriceCheckBot();
        bot.navigateToPage(baseUrl);
        bot.waitForPageLoad();

        bot.selectIncreasingPrice();


        boolean result =bot.checkAscendingOrder();

        bot.quit();
        assertTrue(result);

    }

    @ParameterizedTest
    @CsvSource({
            "https://www.teknosa.com/tablet-c-116012"
    })
    void testDescendingPrice(String baseUrl){
        bot= new PriceCheckBot();
        bot.navigateToPage(baseUrl);
        bot.waitForPageLoad();

        bot.selectDescendingPrice();


        boolean result =bot.checkDescendingOrder();

        bot.quit();
        assertTrue(result);

    }

}
