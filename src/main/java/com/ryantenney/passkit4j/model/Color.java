package com.ryantenney.passkit4j.model;

import java.io.IOException;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
@ToString(includeFieldNames=true)
@JsonSerialize(using=Color.ColorSerializer.class)
@JsonDeserialize(using=Color.ColorDeserializer.class)
public class Color {

	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLACK = new Color(0, 0, 0);

	private final int red;
	private final int green;
	private final int blue;

	public Color(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(java.awt.Color color) {
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
	}

	static final class ColorSerializer extends JsonSerializer<Color> {

		@Override
		public void serialize(Color color, JsonGenerator json, SerializerProvider serializer) throws IOException, JsonProcessingException {
			json.writeString(String.format("rgb(%d, %d, %d)", color.red(), color.green(), color.blue()));
		}

	}

	static final class ColorDeserializer extends JsonDeserializer<Color> {

		@Override
		public Color deserialize(JsonParser json, DeserializationContext context) throws IOException, JsonProcessingException {
			String str = json.getText().replace("rgb(", "").replace(")", "");
			StringTokenizer tok = new StringTokenizer(str, ",");
			int red = Integer.parseInt(tok.nextToken().trim(), 10);
			int green = Integer.parseInt(tok.nextToken().trim(), 10);
			int blue = Integer.parseInt(tok.nextToken().trim(), 10);
			return new Color(red, green, blue);
		}

	}

}
