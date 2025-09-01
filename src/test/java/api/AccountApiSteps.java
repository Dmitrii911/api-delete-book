package api;

import models.AddBookBodyModel;
import models.LoginBodyModel;
import models.LoginResponseModel;

import java.util.List;

import static io.restassured.RestAssured.given;
import static specs.SpecsBookStoreTest.*;
import static tests.TestData.PASSWORD;
import static tests.TestData.USERNAME;

public class AccountApiSteps {

    public AccountApiSteps(LoginResponseModel authResponse) {
        this.authResponse = authResponse;
    }

    public LoginResponseModel login() {
        LoginBodyModel authData = new LoginBodyModel(USERNAME, PASSWORD);
        return given(baseRequestSpec)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(baseResponseSpec)
                .extract().as(LoginResponseModel.class);

    }
    private LoginResponseModel authResponse;

    public AddBookBodyModel addBookBodyModel() {
        AddBookBodyModel bookData = new AddBookBodyModel(
                authResponse.getUserId(),
                List.of(new AddBookBodyModel.BookIsbn("9781449325862")));
        return (AddBookBodyModel) given(baseRequestSpec)
                .header("Authorization", "Bearer " + authResponse.getToken())
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(bookCollectionResponseSpec)
                .statusCode(201);
    }
}
