package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SqlHelper;
import ru.netology.pages.PaymentMethod;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderPageTest {
    public static String url = System.getProperty("sut.url");

    @BeforeEach
    public void openPage() {
        open(url);
    }

    @AfterEach
    public void cleanBase() {
        SqlHelper.clearDB();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void buyPositiveAllFieldValidApproved() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getApprovedCard());
        payment.waitNotificationApproved();
        assertEquals("APPROVED", SqlHelper.getPaymentStatus());
    }

    @Test
    void buyPositiveAllFieldValidDeclined() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getDeclinedCard());
        payment.waitNotificationFailure();
        assertEquals("DECLINED", SqlHelper.getPaymentStatus());
    }

    @Test
    void buyNegativeAllFieldEmpty() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getEmptyCard());
        payment.waitNotificationWrongFormat4Fields();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeNumberCardSymbols() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getNumberCardSymbols());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeCardNotInDatabase() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardNotInDatabase());
        payment.waitNotificationFailure();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeMonthSymbol() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardMonthSymbol());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeMonthOver() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardMonthOver());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeMonthThisYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardMonthThisYear());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeMonthOverThisYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardMonthOverThisYear());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardYear());
        payment.waitNotificationExpiredError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeYearSymbol() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardYearSymbol());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeYearUnderThisYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardYearUnderThisYear());
        payment.waitNotificationExpiredError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeYearOverThisYearOn6() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardYearOverThisYearOn6());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeCvvSymbol() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardCvvSymbol());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }
    @Test
    void buyNegativeOwnerCirillic() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardHolderCirillic());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeOwnerNumeric() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardHolderNumeric());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void buyNegativeOwnerSpecialSymbols() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToBuyPage();
        payment.inputData(DataHelper.getCardSpecialSymbols());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }
}
