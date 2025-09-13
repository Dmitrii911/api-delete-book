package api;

import models.LoginBodyModel;
import models.LoginResponseModel;

import static io.restassured.RestAssured.given;
import static specs.SpecsBookStoreTest.*;
import static tests.TestData.*;

public class AccountApiSteps {

    public LoginResponseModel login() {
        LoginBodyModel authData = new LoginBodyModel(username, password);
        return given(baseRequestSpec)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(baseResponseSpec)
                .extract().as(LoginResponseModel.class);
    }
}
