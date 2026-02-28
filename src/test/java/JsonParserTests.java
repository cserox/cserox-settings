import org.delastochkin.JsonBuilder;
import org.delastochkin.JsonParser;
import org.delastochkin.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTests {
    @Test
    void testReadAttribute() throws IOException {
        String input = "{\n\"name\": Darcula,\n\"dark\": true}";
        Settings settings = JsonParser.parseSettings(new StringReader(input));
        assertEquals("Darcula", settings.attribute("name").orElse(null));
        assertEquals("true", settings.attribute("dark").orElse(null));
        assertTrue(settings.attribute("does-not-exist").isEmpty());
        assertTrue(settings.element("name").isEmpty());
    }

    @Test
    void testReadElement() throws IOException {
        String input = "{\n\"colors\": {\"accentColor\": \"#ff79c6\"}}";
        Settings settings = JsonParser.parseSettings(new StringReader(input));
        assertTrue(settings.attribute("colors").isEmpty());
        assertTrue(settings.attribute("accentColor").isEmpty());
        assertTrue(settings.element("colors").isPresent());
        Settings colors = settings.element("colors").get();
        assertEquals("#ff79c6", colors.attribute("accentColor").orElse(null));
        assertNull(colors.attribute("does-not-exist").orElse(null));
    }

    //TODO better tests
    @Test
    void testReadSeveralElements() throws IOException {
        String input = """
                {
                    "parent-attribute": "1",
                    "first-parent-element": {
                        "child-attribute": "2",
                        "first-child-element": {
                            "grand-child-attribute": "3"
                        },
                        "second-child-element": {
                            "grand-child-element": {
                                "grand-grand-child-attribute": "4"
                            }
                        }
                    },
                    "second-parent-element": {
                        "child-attribute": "5"
                    }
                }""";
        assertEquals(input.replaceAll(" {4}", "\t"), JsonBuilder.fromSettings(JsonParser.parseSettings(new StringReader(input))));
    }
}
