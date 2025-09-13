package tests;

import api.AccountApiSteps;
import api.BookStoreApiSteps;
import api.UserApi;
import helpers.AuthHelper;
import models.AddBookBodyModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;

import java.util.List;

import static com.codeborne.selenide.Selenide.confirm;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;
import static tests.TestData.username;

public class BookStoreTests extends TestBase {
    @Test
    void addBookToCollectionTest() {
        AccountApiSteps accountApiSteps = new AccountApiSteps();
        AuthHelper authHelper = new AuthHelper();
        ProfilePage profilePage = new ProfilePage();

        LoginResponseModel authResponse = step("Шаг 1: Авторизация", accountApiSteps::login);

        step("Шаг 2: Очистка предыдущей коллекции книг", () ->
                BookStoreApiSteps.delAllBooks(authResponse.getToken(), authResponse.getUserId()));

        AddBookBodyModel bookData = new AddBookBodyModel(
                authResponse.getUserId(),
                List.of(new AddBookBodyModel.BookIsbn("9781449325862")));

        step("Шаг 3: Добавление новой книги", () ->
                BookStoreApiSteps.addBook(bookData, authResponse.getToken()));

        step("Шаг 4: Авторизация через cookies", () ->
                authHelper.setAuthCookies(authResponse)
        );

        step("Шаг 5: Удаление книги из коллекции через UI", () -> {
            ProfilePage.openPage()
                    .removeAdds()
                    .checkUserName(username)
                    .clickOnDeleteBtn()
                    .clickOkInModal();
            confirm();
            profilePage.checkListOfBooksIsEmpty();
        });

        UserResponseModel updatedUserResponse = step("Запрос информации о пользователе", () ->
                UserApi.userInfo(authResponse.getToken(), authResponse.getUserId()));

        step("Проверка, что коллекция книг пуста", () -> {
            assertThat(updatedUserResponse.getBooks(), Matchers.empty());
        });
    }
}




