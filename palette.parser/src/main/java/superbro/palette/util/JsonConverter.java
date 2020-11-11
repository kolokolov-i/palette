package superbro.palette.util;

import com.google.gson.*;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;

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

    /*
    public static class ChipDeserializer implements JsonDeserializer<ColorChip> {
        @Override
        public ColorChip deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ColorChip result = new ColorChip();
            JsonObject chipObj = json.getAsJsonObject();
            result.name = chipObj.get("name").getAsString();
            result.description = chipObj.get("description").getAsString();
            result.colorRGB = chipObj.get("colorRGB").getAsString();
            result.colorCMYKc = chipObj.get("colorCMYKc").getAsString();
            result.colorCMYKu = chipObj.get("colorCMYKu").getAsString();
            return result;
        }
    }
    */
    public static class ChipGroupDeserializer implements JsonDeserializer<ColorGroup> {

        @Override
        public ColorGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ColorGroup result = new ColorGroup();
            JsonObject groupObj = json.getAsJsonObject();
            result.name = groupObj.get("name").getAsString();
            String layout = groupObj.get("layout").getAsString();
            result.layout = ColorGroup.ChipLayout.valueOf(layout);
            result.chips = context.deserialize(groupObj.get("chips"), ColorChip[].class);
            return result;
        }
    }
}
