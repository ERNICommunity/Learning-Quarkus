@QuarkusTest
public class GreetingResourceTest {
    @Test
    public void testGreetingResource() {
        RestAssured.given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(Matchers.equalTo("Hello from mock"));
    }

    @io.quarkus.test.Mock
    @ApplicationScoped
    public static class MockGreetingService extends GreetingService {
        @Override
        public String greet() {
            return "Hello from mock";
        }
    }
}