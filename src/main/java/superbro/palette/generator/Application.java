package superbro.palette.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import superbro.palette.model.*;
import superbro.palette.util.JsonConverter;

import java.awt.*;
import java.io.*;

public class Application {

    public static void main(String[] args) throws IOException {
        Palette palette = readFrom("ral-classic.json");
        PDDocument document = DocumentBuilder.build(palette);
        // TODO gui dialogs
        document.save(palette.getName()+".pdf");
        document.close();
    }

    private static Palette readFrom(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new JsonConverter.ChipGroupDeserializer())
                .create();
        return gson.fromJson(new InputStreamReader(new FileInputStream(fileName), "UTF-8"), Palette.class);
    }
}
