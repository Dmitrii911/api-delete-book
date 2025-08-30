package tests;

import com.codeborne.selenide.Selenide;
import models.AddBookBodyModel;
import models.LoginBodyModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import pages.ProfilePage;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static specs.SpecsBookStoreTest.*;
import static tests.TestData.PASSWORD;
import static tests.TestData.USERNAME;

public class BookStoreTests extends TestBase {
    @Test
    void addBookToCollectionTest() {
        ProfilePage profilePage = new ProfilePage();
        LoginBodyModel authData = new LoginBodyModel(USERNAME, PASSWORD);

        LoginResponseModel authResponse = step("Шаг 1: Авторизация", () ->
                given(loginRequestSpec)
                        .body(authData)
                        .when()
                        .post("/Account/v1/Login")
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Шаг 2: Очистка предыдущей коллекции книг", () ->
                given(bookCollectionRequestSpec)
                        .header("Authorization", "Bearer " + authResponse.getToken())
                        .queryParams("UserId", authResponse.getUserId())
                        .when()
                        .delete("/BookStore/v1/Books")
                        .then()
                        .spec(bookCollectionResponseSpec)
                        .statusCode(204));

        AddBookBodyModel bookData = new AddBookBodyModel(
                authResponse.getUserId(),
                List.of(new AddBookBodyModel.BookIsbn("9781449325862")));

        step("Шаг 3: Добавление новой книги", () ->
                given(bookCollectionRequestSpec)
                        .header("Authorization", "Bearer " + authResponse.getToken())
                        .body(bookData)
                        .when()
                        .post("/BookStore/v1/Books")
                        .then()
                        .spec(bookCollectionResponseSpec)
                        .statusCode(201));

        step("Шаг 4: UI-проверка добавленной книги", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));


            open("/profile");
            $(".ReactTable").shouldHave(text("Speaking JavaScript"));
        });


        step("Удаление книги из коллекции API", () ->
            given(loginRequestSpec)
                    .header("Authorization", "Bearer " + authResponse.getToken())
                    .when()
                    .delete("/BookStore/v1/Books")
                    .then()
                    .spec(bookCollectionResponseSpec)
                    .statusCode(204));

        UserResponseModel updatedUserResponse = step("Запрос информации о пользователе", () ->
                given(loginRequestSpec)
                        .header("Authorization", "Bearer " + authResponse.getToken())
                        .when()
                        .get("/Account/v1/User/" + authResponse.getUserId())
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(UserResponseModel.class));

        step("Проверка, что коллекция книг пуста", () -> {
            assertThat(updatedUserResponse.getBooks(), Matchers.empty());
        });
        }
    }




