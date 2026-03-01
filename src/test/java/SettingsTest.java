import org.delastochkin.Setting;
import org.delastochkin.Settings;
import org.delastochkin.TryingToModifyEmptySettingsError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {

	private Settings settings;

	@BeforeEach
	void setup() {
		settings = new Settings();
	}

	@Test
	public void testGetSetAttribute() {
		assertTrue(settings.attribute("attribute").isEmpty());
		settings.setAttribute("attribute", "value");
		assertEquals("value", settings.attribute("attribute").orElseThrow());
		settings.setAttribute("attribute", "value2");
		assertEquals("value2", settings.attribute("attribute").orElseThrow());
		assertTrue(settings.element("attribute").isEmpty());
	}

	@Test
	public void testGetSetElement() {
		assertTrue(settings.element("element").isEmpty());
		Settings element = settings.newElement("element");
		assertEquals(element, settings.element("element").orElseThrow());
		Settings element2 = settings.newElement("element");
		assertEquals(element2, settings.element("element").orElseThrow());
		assertTrue(settings.attribute("element").isEmpty());
		element2.setAttribute("attribute", "value");
		assertTrue(settings.attribute("attribute").isEmpty());
		assertEquals("value", settings.element("element").orElseThrow().attribute("attribute").orElseThrow());
	}

	@Test
	public void testElementReplacesAttributeAndViceVersa() {
		settings.newElement("name");
		settings.setAttribute("name", "value");
		assertTrue(settings.element("name").isEmpty());
		assertTrue(settings.attribute("name").isPresent());
		settings.newElement("name");
		assertTrue(settings.attribute("name").isEmpty());
		assertTrue(settings.element("name").isPresent());
	}

	@Test
	public void testEmptySettings() {
		Settings empty = Settings.empty();
		assertEquals(empty, Settings.empty(), "Empty settings must be static");
		assertTrue(empty.attribute("any").isEmpty());
		assertTrue(empty.element("any").isEmpty());
		assertTrue(empty.childElements().findFirst().isEmpty());
		assertTrue(empty.childAttributes().findFirst().isEmpty());
		assertTrue(empty.fetch((setting) -> true, Setting::self).findFirst().isEmpty());
		assertThrows(TryingToModifyEmptySettingsError.class, () -> empty.newElement("element"));
		assertThrows(TryingToModifyEmptySettingsError.class, () -> empty.setAttribute("attribute", "value"));
	}

	@Test
	public void testChildAttributes() {
		assertTrue(settings.childAttributes().findFirst().isEmpty());
		settings.setAttribute("child1", "value1");
		settings.setAttribute("child2", "value2");
		//To check that child attributes returns only direct child attributes
		settings.newElement("element").setAttribute("grand-child", "value3");
		assertContainsAll(
				Map.of("child1", "value1", "child2", "value2"),
				settings.childAttributes(),
				"attribute"
		);
	}

	@Test
	public void testChildElements() {
		assertTrue(settings.childElements().findFirst().isEmpty());
		Settings child1 = settings.newElement("child1");
		Settings child2 = settings.newElement("child2");
		//To check that child elements returns only direct child elements
		child1.setAttribute("attribute1", "value1");
		child1.newElement("grand-child").setAttribute("attribute2", "value2");
		settings.setAttribute("attribute3", "value3");
		assertContainsAll(
				Map.of("child1", child1, "child2", child2),
				settings.childElements(),
				"element"
		);
	}

	private Settings getTestFetchSettings() {
		Settings settings = new Settings();
		settings.setAttribute("parent-attribute", "1");
		Settings firstParentElement = settings.newElement("first-parent-element");
		firstParentElement.setAttribute("child-attribute", "2");
		firstParentElement
				.newElement("first-child-element")
				.setAttribute("grand-child-attribute", "3");
		firstParentElement
				.newElement("second-child-element")
				.newElement("grand-child-element")
				.setAttribute("grand-grand-child-attribute", "4");
		settings.newElement("second-parent-element").setAttribute("second-child-attribute", "5");
		return settings;
		/*
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
				"second-child-attribute": "5"
			}
		}
		*/
	}

	@Test
	public void testFetchPredicate() {
		settings = getTestFetchSettings();
		assertEmptyStream(settings.fetch((setting) -> false, Setting::self));
		assertStreamContainsAllSettings(settings.fetch((setting) -> true, Setting::self));
		assertContainsAll(
				List.of("1", "2", "3", "4", "5"),
				settings.fetch(Setting::isAttribute, setting -> setting.attribute().orElseThrow()),
				"attribute value"
		);
	}

	@Test
	public void testFetchQuery() {
		settings = getTestFetchSettings();
		assertEmptyStream(settings.fetch((setting, parameter) -> false, null, Setting::self));
		assertStreamContainsAllSettings(settings.fetch((setting, parameter) -> true, null, Setting::self));
		assertContainsAll(
				List.of("1", "2", "3", "4", "5"),
				settings.fetch(
						(setting, name) -> setting.name().endsWith(name),
						"attribute",
						setting -> setting.attribute().orElseThrow()),
				"attribute value"
		);
	}

	private void assertEmptyStream(Stream<Setting> stream) {
		assertTrue(stream.findFirst().isEmpty());
	}

	private void assertStreamContainsAllSettings(Stream<Setting> stream) {
		assertContainsAll(
				Map.of(
						"parent-attribute", "1",
						"first-parent-element", settings.element("first-parent-element").orElseThrow(),
						"child-attribute", "2",
						"first-child-element", settings.element("first-parent-element").orElseThrow().element("first-child-element").orElseThrow(),
						"grand-child-attribute", "3",
						"second-child-element", settings.element("first-parent-element").orElseThrow().element("second-child-element").orElseThrow(),
						"grand-child-element", settings.element("first-parent-element").orElseThrow().element("second-child-element").orElseThrow().element("grand-child-element").orElseThrow(),
						"grand-grand-child-attribute", "4",
						"second-parent-element", settings.element("second-parent-element").orElseThrow(),
						"second-child-attribute", "5"
				),
				stream,
				"setting"
		);
	}

	public <S extends Setting> void assertContainsAll(Map<String, Object> expected, Stream<S> actual, String settingName) {
		//Streams are not reusable, so need to collect first
		Collection<S> actualCollection = actual.toList();
		assertContainsAll(
				expected.keySet(),
				actualCollection.stream().map(Setting::name),
				settingName + " name"
		);
		assertContainsAll(
				expected.values(),
				actualCollection.stream().map(Setting::value),
				settingName + " value"
		);
	}

	public <T> void assertContainsAll(Collection<T> expected, Stream<T> actual, String settingName) {
		final List<T> expectedMutable = new ArrayList<>(expected);
		actual.forEach((item) -> {
					assertTrue(expectedMutable.contains(item), "Unexpected " + settingName + ": " + item);
					expectedMutable.remove(item);
				}
		);
		assertTrue(expectedMutable.isEmpty(), "Stream doesn't contain following expected " + settingName + "s: " + expectedMutable);
	}


}
