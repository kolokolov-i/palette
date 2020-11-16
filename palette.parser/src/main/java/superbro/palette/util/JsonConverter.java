package superbro.palette.util;

import com.google.gson.*;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;

import java.awt.*;
import java.lang.reflect.Type;

public class JsonConverter {

    public static class ChipLayoutSerializer implements JsonSerializer<ColorGroup.ChipLayout> {

        @Override
        public JsonElement serialize(ColorGroup.ChipLayout chipLayout, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(chipLayout.toString());
        }
    }

    public static class ChipLayoutDeserializer implements JsonDeserializer<ColorGroup.ChipLayout> {
        @Override
        public ColorGroup.ChipLayout deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ColorGroup.ChipLayout.valueOf(jsonElement.getAsString());
        }
    }
}
