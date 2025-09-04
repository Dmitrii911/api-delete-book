package api;

import models.LoginBodyModel;
import models.LoginResponseModel;

import static io.restassured.RestAssured.given;
import static specs.SpecsBookStoreTest.*;
import static tests.TestData.PASSWORD;
import static tests.TestData.USERNAME;

public class AccountApiSteps {

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
}
