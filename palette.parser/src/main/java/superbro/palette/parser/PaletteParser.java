package superbro.palette.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;
import superbro.palette.model.Palette;
import superbro.palette.util.JsonConverter;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaletteParser {

    static final String urlClassic = "https://colorscheme.ru/ral-colors/ral-classic.html";
    static final String urlDesign = "https://colorscheme.ru/ral-colors/ral-design.html";

    public static void main(String[] args) throws IOException {
        Palette palette = parseRal();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Color.class, new JsonConverter.ChipLayoutSerializer())
                .create();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("ral-classic.json"), StandardCharsets.UTF_8);
        gson.toJson(palette, writer);
        writer.close();
    }

    private static Palette parseRal() throws IOException {
        Document doc = Jsoup.connect(urlClassic).get();
        Elements els = doc.select("table.color-table > tbody > tr");
        Iterator<Element> iter = els.iterator();
        Element elm = iter.next();
        List<ColorGroup> groups = new ArrayList<>();
        ColorGroup group = new ColorGroup();
        group.name = elm.selectFirst("th").text();
        group.layout = ColorGroup.ChipLayout.Narrow;
        group.chips = new ArrayList<>();
        while (iter.hasNext()) {
            elm = iter.next();
            if (elm.hasClass("br")) {
                groups.add(group);
                group = new ColorGroup();
                group.name = elm.selectFirst("th").text();
                group.layout = ColorGroup.ChipLayout.Narrow;
                group.chips = new ArrayList<>();
            } else {
                ColorChip chip = new ColorChip();
                chip.name = elm.child(1).selectFirst("b").text();
                chip.description = elm.child(1).selectFirst("i").text();
                chip.colorCMYKc = elm.child(2).text();
                chip.colorCMYKu = elm.child(3).text();
                chip.colorRGB = elm.child(4).text();
                group.chips.add(chip);
            }
        }
        groups.add(group);
        return new Palette("RAL Classic", groups);
    }

}
