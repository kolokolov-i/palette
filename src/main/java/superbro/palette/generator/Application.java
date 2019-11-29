package superbro.palette.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import superbro.palette.dto.*;
import superbro.palette.util.JsonConverter;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Palette palette = readFrom("data.json");
        PDDocument document = DocumentBuilder.build(palette);
        // TODO gui dialogs
        document.save(palette.getName()+".pdf");
        document.close();
    }

    private static Palette readFrom(String fileName) throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new JsonConverter.ColorDesializer())
                .create();
        return gson.fromJson(new FileReader(fileName), Palette.class);
    }
}
