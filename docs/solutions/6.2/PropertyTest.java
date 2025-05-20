@QuarkusTest
public class PropertyTest {
    @ConfigProperty(name = "erni.custom")
    String prop;

    @Test
    public void testProperty() {
        assertEquals("main", prop);
    }
}
