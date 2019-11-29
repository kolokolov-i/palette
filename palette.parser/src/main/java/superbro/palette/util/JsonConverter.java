package superbro.palette.util;

import com.google.gson.*;

import java.awt.*;
import java.lang.reflect.Type;

public class JsonConverter {

    public static class ColorSerializer implements JsonSerializer<Color> {
        @Override
        public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

    public static class ColorDesializer implements JsonDeserializer<Color> {
        @Override
        public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String string = json.getAsString();
            int r, g, b;
            r = Integer.parseInt(string.substring(1, 3), 16);
            g = Integer.parseInt(string.substring(3, 5), 16);
            b = Integer.parseInt(string.substring(5, 7), 16);
            return new Color(r, g, b);
        }
    }
}
