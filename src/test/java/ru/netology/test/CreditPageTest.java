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

public class CreditPageTest {
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
    void creditPositiveAllFieldValidApproved() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getApprovedCard());
        payment.waitNotificationApproved();
        assertEquals("APPROVED", SqlHelper.getCreditRequestStatus());
    }

    @Test
    void creditPositiveAllFieldValidDeclined() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getDeclinedCard());
        payment.waitNotificationFailure();
        assertEquals("DECLINED", SqlHelper.getCreditRequestStatus());
    }

    @Test
    void creditNegativeAllFieldEmpty() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getEmptyCard());
        payment.waitNotificationWrongFormat4Fields();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeNumberCardSymbols() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getNumberCardSymbols());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeCardNotInDatabase() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardNotInDatabase());
        payment.waitNotificationFailure();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeMonthSymbol() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardMonthSymbol());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeMonthOver() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardMonthOver());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeMonthThisYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardMonthThisYear());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeMonthOverThisYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardMonthOverThisYear());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardYear());
        payment.waitNotificationExpiredError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeYearSymbol() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardYearSymbol());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeYearUnderThisYear() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardYearUnderThisYear());
        payment.waitNotificationExpiredError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeYearOverThisYearOn6() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardYearOverThisYearOn6());
        payment.waitNotificationExpirationDateError();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeCvvSymbol() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardCvvSymbol());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeOwnerCirillic() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardHolderCirillic());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeOwnerNumeric() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardHolderNumeric());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }

    @Test
    void creditNegativeOwnerSpecialSymbols() {
        val startPage = new PaymentMethod();
        val payment = startPage.goToCreditPage();
        payment.inputData(DataHelper.getCardSpecialSymbols());
        payment.waitNotificationWrongFormat();
        assertEquals("0", SqlHelper.getOrderCount());
    }
}

