package api;

import io.restassured.response.Response;
import models.AddBookBodyModel;

import static io.restassured.RestAssured.given;
import static specs.SpecsBookStoreTest.baseRequestSpec;
import static specs.SpecsBookStoreTest.bookCollectionResponseSpec;

public class BookStoreApiSteps {
    public static Response addBook(AddBookBodyModel bookData, String token) {

        return given(baseRequestSpec)
                .header("Authorization", "Bearer " + token)
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(bookCollectionResponseSpec)
                .statusCode(201)
                .extract().response();
    }

    public static Response delAllBooks(String token, String userId) {

        given(baseRequestSpec)
                .header("Authorization", "Bearer " + token)
                .queryParams("UserId", userId)
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(bookCollectionResponseSpec)
                .statusCode(204);
        return null;
    }
}
