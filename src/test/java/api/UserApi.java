package api;

import models.UserResponseModel;

import static io.restassured.RestAssured.given;
import static specs.SpecsBookStoreTest.baseRequestSpec;
import static specs.SpecsBookStoreTest.baseResponseSpec;

public class UserApi {
    public static UserResponseModel userInfo(String token, String userId) {
        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .spec(baseResponseSpec)
                .extract().as(UserResponseModel.class);
    }
}
