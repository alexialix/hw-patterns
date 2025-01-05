package ru.netology;

import io.qameta.allure.selenide.AllureSelenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        // Добавляем слушателя для Allure
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        // Убираем слушателя Allure после выполнения тестов
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").findBy(text("Запланировать")).click();


        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));


        $("[data-test-id='date'] input").doubleClick().sendKeys(secondMeetingDate);
        $$("button").findBy(text("Запланировать")).click();


        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$("button").findBy(text("Перепланировать")).click();


        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
    }
}