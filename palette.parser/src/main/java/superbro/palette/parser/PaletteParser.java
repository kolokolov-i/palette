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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaletteParser {

    enum Variant {
        Classic("https://colorscheme.ru/ral-colors/ral-classic.html", "save/RAL_CLASSIC.html", "ral-classic.json"),
        Design("https://colorscheme.ru/ral-colors/ral-design.html", "save/RAL_DESIGN.html", "ral-design.json");

        String url, inFile,  outFile;

        Variant(String url, String inFile, String outFile) {
            this.url = url;
            this.inFile = inFile;
            this.outFile = outFile;
        }
    }

    public static void main(String[] args) throws IOException {
        parseVariant(Variant.Classic);
        parseVariant(Variant.Design);
    }

    private static void parseVariant(Variant vare) throws IOException {
        Palette palette;
        switch (vare){
            case Classic:
                palette = parseRalClassic(); break;
            case Design:
                palette = parseRalDesign(); break;
            default:
                return;
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Color.class, new JsonConverter.ChipLayoutSerializer())
                .create();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(vare.outFile), StandardCharsets.UTF_8);
        gson.toJson(palette, writer);
        writer.close();
    }

    private static Palette parseRalClassic() throws IOException {
        Document doc = Jsoup.parse(new File(Variant.Classic.inFile), "UTF-8", Variant.Classic.url);
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
                Element te = elm.child(1);
                chip.name = te.child(0).text();
                if(te.child(1).tagName().equalsIgnoreCase("br")){
                    chip.description = te.child(2).text();
                    chip.extra = null;
                }
                else{
                    chip.description = te.child(3).text();
                    chip.extra = te.child(1).child(0).text();
                }
                chip.colorCMYKc = elm.child(2).text();
                chip.colorCMYKu = elm.child(3).text();
                chip.colorRGB = elm.child(4).text();
                group.chips.add(chip);
            }
        }
        groups.add(group);
        return new Palette("RAL Classic", groups);
    }

    private static Palette parseRalDesign() throws IOException {
        Document doc = Jsoup.parse(new File(Variant.Design.inFile), "UTF-8", Variant.Design.url);
        Elements els = doc.select("table.color-table > tbody > tr");
        Iterator<Element> iter = els.iterator();
        Element elm = iter.next();
        List<ColorGroup> groups = new ArrayList<>();
        ColorGroup group = new ColorGroup();
        group.layout = ColorGroup.ChipLayout.Wide;
        group.chips = new ArrayList<>();
        while (iter.hasNext()) {
            elm = iter.next();
            ColorChip chip = new ColorChip();
            chip.name = elm.child(1).text();
            chip.colorCMYKc = elm.child(2).text();
            chip.colorCMYKu = elm.child(3).text();
            chip.colorRGB = elm.child(4).text();
            group.chips.add(chip);
        }
        groups.add(group);
        return new Palette("RAL Design", groups);
    }

}
