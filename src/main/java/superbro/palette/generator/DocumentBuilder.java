package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;
import superbro.palette.model.Palette;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static superbro.palette.generator.GeometryUtil.*;

class DocumentBuilder {
    static PDDocument build(Palette palette) throws IOException {
        List<PageData> pages = distributeChips(palette);
        PDDocument document = new PDDocument();
        PDDocumentInformation docInfo = document.getDocumentInformation();
        docInfo.setTitle(palette.getName());
        buildPages(document, pages);
        return document;
    }

    private static void buildPages(PDDocument document, List<PageData> pages) throws IOException {
        for (PageData pageData : pages) {
            PDPage pdPage = new PDPage(PDRectangle.A4);
            PDPageContentStream content = new PDPageContentStream(document, pdPage);
            PDFont font = PDType0Font.load(document, new File("GOTHIC.TTF"));
            content.setFont(font, 24);
            content.setNonStrokingColor(Color.BLACK);
            content.beginText();
            content.newLineAtOffset(marginLeft + toPix(2), pageHeight - marginUp - toPix(8));
            content.showText(pageData.title);
            content.endText();
            content.setStrokingColor(Color.BLACK);
            content.setLineWidth(1);
            content.moveTo(marginLeft, pageHeight - marginUp - toPix(10));
            content.lineTo(pageWidth - marginRight, pageHeight - marginUp - toPix(10));
            content.stroke();
            content.beginText();
            content.newLineAtOffset(pageWidth - marginRight - toPix(25), pageHeight - marginUp - toPix(8));
            content.showText(pageData.number);
            content.endText();
            content.setFont(font, 14);
            float curX, curY, gridW, gridH, chipW, chipH;
            int curColumn = 0;
            gridW = pageData.layout.gridW;
            gridH = pageData.layout.gridH;
            chipW = pageData.layout.chipW;
            chipH = pageData.layout.chipH;
            curX = marginLeft;
            curY = pageHeight - marginUp - toPix(18);
            for (ColorChip colorChip : pageData.chips) {
                if (curColumn >= pageData.layout.rowCapacity) {
                    curColumn = 0;
                    curX = marginLeft;
                    curY -= toPix(gridH);
                }
                content.setNonStrokingColor(Color.BLACK);
                content.beginText();
                content.newLineAtOffset(curX + toPix(2), curY + toPix(2));
                content.showText(colorChip.name);
                content.endText();
                content.setNonStrokingColor(Color.decode(colorChip.colorRGB));
                content.addRect(curX, curY - toPix(chipH), toPix(chipW), toPix(chipH));
                content.fill();
                curX += toPix(gridW);
                curColumn++;
            }
            content.close();
            document.addPage(pdPage);
        }
    }

    private static List<PageData> distributeChips(Palette palette) {
        List<PageData> result = new ArrayList<>();
        List<ColorGroup> groups = palette.getGroups();
        int groupNumber = 1;
        for (ColorGroup group : groups) {
            int pageCapacity = group.layout.pageCapacity;
            List<ColorChip> chips = group.chips;
            int totalPages = (int) Math.ceil(chips.size() / (double) pageCapacity);
            for (int i = 0, pageNumber = 1; i < chips.size(); i += pageCapacity, pageNumber++) {
                PageData pageData = new PageData();
                pageData.layout = group.layout;
                if (totalPages == 1) {
                    pageData.title = group.name;
                    pageData.number = groupNumber + "G" + pageNumber;
                } else {
                    pageData.title = String.format("%s ( %d / %d )", group.name, pageNumber, totalPages);
                    pageData.number = groupNumber + "G" + pageNumber;
                }
                int lastChipIndex = i + pageCapacity - 1;
                lastChipIndex = Math.min(lastChipIndex, chips.size());
                pageData.chips = chips.subList(i, lastChipIndex);
                result.add(pageData);
            }
            groupNumber++;
        }
        return result;
    }

    static class PageData {
        String title;
        String number;
        List<ColorChip> chips;
        ColorGroup.ChipLayout layout;
    }
}
