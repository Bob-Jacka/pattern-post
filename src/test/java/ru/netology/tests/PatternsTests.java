package ru.netology.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.dataclass.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PatternsTests {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        DataGenerator.sendRequest(registeredUser);
        $("[data-test-id='login'] .input__control").sendKeys(registeredUser.getLogin());
        $("[data-test-id='password'] .input__control").sendKeys(registeredUser.getPassword());
        $("[data-test-id='action-login'].button").click();
        $("h2").shouldBe(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id='login'] .input__control").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id='password'] .input__control").sendKeys(notRegisteredUser.getPassword());
        $("[data-test-id='action-login'].button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.partialText("Неверно указан логин или пароль"), Duration.ofSeconds(5000));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        DataGenerator.sendRequest(blockedUser);
        $("[data-test-id='login'] .input__control").sendKeys(blockedUser.getLogin());
        $("[data-test-id='password'] .input__control").sendKeys(blockedUser.getPassword());
        $("[data-test-id='action-login'].button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.partialText("Пользователь заблокирован"), Duration.ofSeconds(5000));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        DataGenerator.sendRequest(registeredUser);
        $("[data-test-id='login'] .input__control").sendKeys(wrongLogin);
        $("[data-test-id='password'] .input__control").sendKeys(registeredUser.getPassword());
        $("[data-test-id='action-login'].button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.partialText("Неверно указан логин или пароль"), Duration.ofSeconds(5000));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        DataGenerator.sendRequest(registeredUser);
        $("[data-test-id='login'] .input__control").sendKeys(registeredUser.getLogin());
        $("[data-test-id='password'] .input__control").sendKeys(wrongPassword);
        $("[data-test-id='action-login'].button").click();
        $("[data-test-id='error-notification']").shouldBe(Condition.partialText("Неверно указан логин или пароль"), Duration.ofSeconds(5000));
    }
}
