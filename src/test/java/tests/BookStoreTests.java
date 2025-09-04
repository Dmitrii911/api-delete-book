package tests;

import api.AccountApiSteps;
import api.BookStoreApiSteps;
import api.CheckingBook;
import api.UserApi;
import models.AddBookBodyModel;
import models.LoginResponseModel;
import models.UserResponseModel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookStoreTests extends TestBase {
    @Test
    void addBookToCollectionTest() {
        AccountApiSteps accountApiSteps = new AccountApiSteps();

        LoginResponseModel authResponse = step("Шаг 1: Авторизация", accountApiSteps::login);

        step("Шаг 2: Очистка предыдущей коллекции книг", () ->
                BookStoreApiSteps.delAllBooks(authResponse.getToken(), authResponse.getUserId()));

        AddBookBodyModel bookData = new AddBookBodyModel(
                authResponse.getUserId(),
                List.of(new AddBookBodyModel.BookIsbn("9781449325862")));

        step("Шаг 3: Добавление новой книги", () ->
                BookStoreApiSteps.addBook(bookData, authResponse.getToken()));

        step("Шаг 4: UI-проверка добавленной книги", () ->
                CheckingBook.checkBook(authResponse));

        step("Удаление книги из коллекции API", () ->
                BookStoreApiSteps.deleteBook(authResponse.getToken(), authResponse.getUserId()));

        UserResponseModel updatedUserResponse = step("Запрос информации о пользователе", () ->
                UserApi.userInfo(authResponse.getToken(), authResponse.getUserId()));

        step("Проверка, что коллекция книг пуста", () -> {
            assertThat(updatedUserResponse.getBooks(), Matchers.empty());
        });
    }
}




