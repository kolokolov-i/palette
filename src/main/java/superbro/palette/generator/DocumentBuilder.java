package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import superbro.palette.dto.Palette;

public class DocumentBuilder {
    public static PDDocument build(Palette palette) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        return document;
    }
}
