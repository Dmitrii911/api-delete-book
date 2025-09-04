package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private SelenideElement userName = $("#userName-value"),
            deleteButtons = $("#delete-record-undefined"),
            deleteModal = $("#closeSmallModal-ok"),
            noRowsFound = $(".rt-noData");


    @Step("Открыть страницу")
    public ProfilePage openPage() {
        open("/profile");

        return this;
    }

    @Step("Удалить баннер")
    public ProfilePage removeAdds() {
        executeJavaScript("$('#fixedban').remove()");
        executeJavaScript("$('footer').remove()");

        return this;
    }


    @Step("Проверить User Name")
    public ProfilePage checkUserName(String login) {
        userName.shouldHave(text(login));

        return this;

    }

    @Step("Удалить книгу")
    public ProfilePage clickOnDeleteBtn() {
        deleteButtons.click();

        return this;
    }

    @Step("Подтвердить удаление книги в модальном окне")
    public ProfilePage clickOkInModal() {
        deleteModal.click();

        return this;
    }

    @Step("Проверить, что список книг пуст")
    public ProfilePage checkListOfBooksIsEmpty() {
        noRowsFound.isDisplayed();
        noRowsFound.shouldHave(text("No rows found"));

        return this;
    }
}