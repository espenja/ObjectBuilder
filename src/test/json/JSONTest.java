package test.json;

import model.CustomObject4;

import org.junit.Test;
import static org.junit.Assert.*;

import formatters.json.JSON;

public class JSONTest {
	
	private String JSONReturnShouldBe = "{ \"CustomObject4\": { \"StringField\": \"espen\", \"IntegerField\": \"123\", \"CharacterField\": \"c\", \"BooleanField\": \"true\", \"DoubleField\": \"123.45\", \"FloatField\": \"123.4567\", \"LongField\": \"123456789123456789\", \"EnumField\": \"TEST1\", \"ListField\": [ \"index1\", \"index2\", [ \"index11\", \"index22\", \"index33\" ]  ], \"MapField\": { \"HALLOOOOOOO\": \"jacobsson\", \"espen\": \"123\", \"marit\": [ \"Index1InHashMap\" ]  }  }  }";
	
	@Test
	public void IsJsonCorrect() {
		JSON json = new JSON();
		String jsonString = json.read(new CustomObject4());
		assertEquals(JSONReturnShouldBe, jsonString);
	}
}
