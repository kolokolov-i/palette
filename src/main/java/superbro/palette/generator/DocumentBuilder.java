package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;
import superbro.palette.model.Palette;

import java.awt.*;
import java.io.IOException;

import static superbro.palette.generator.GeometryUtil.*;

class DocumentBuilder {
    static PDDocument build(Palette palette) throws IOException {
        PDDocument document = new PDDocument();
        PDDocumentInformation docInfo = document.getDocumentInformation();
        docInfo.setTitle(palette.getName());
        PDPage page = new PDPage(PDRectangle.A4);
        PDPageContentStream content = new PDPageContentStream(document, page);
        content.setFont(PDType1Font.COURIER_BOLD, 18);
        float curX, curY;
        curY = pageHeight - marginUp;
        for (ColorGroup group : palette.getGroups()) {
            curX = marginLeft;
            content.setNonStrokingColor(Color.BLACK);
            content.beginText();
            content.newLineAtOffset(curX, curY - 30);
            content.showText(group.getName());
            content.endText();
            float chipW = ctChipsCW / group.getChips().size();
            curX += ctGroupCW;
            for (ColorChip chip : group.getChips()) {
                content.setNonStrokingColor(chip.getColor());
                content.addRect(curX, curY - ctRowH, chipW - 5, ctRowH - 5);
                content.fill();
                curX += chipW;
            }
            curY -= ctRowH;
        }
        content.close();
        document.addPage(page);
        return document;
    }
}
