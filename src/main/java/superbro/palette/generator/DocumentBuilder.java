package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;
import superbro.palette.model.Palette;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static superbro.palette.generator.GeometryUtil.*;

class DocumentBuilder {

    static PDDocument document;
    static PDFont font;

    static Point2D.Float tittlePos = new Point2D.Float(marginLeft + toPix(2), pageHeight - marginUp - toPix(8));
    static Point2D.Float numberPos = new Point2D.Float(pageWidth - marginRight - toPix(18), pageHeight - marginUp - toPix(8));
    static Point2D.Float col1Line = new Point2D.Float(marginLeft, pageHeight - marginUp - toPix(10));
    static Point2D.Float col2Line = new Point2D.Float(col1Line.x + toPix(30), col1Line.y);
    static Point2D.Float col3Line = new Point2D.Float(col2Line.x + toPix(70), col1Line.y);
    static Point2D.Float col4Line = new Point2D.Float(col3Line.x + toPix(30), col1Line.y);
    static Point2D.Float col5Line = new Point2D.Float(col4Line.x + toPix(30), col1Line.y);
    static Point2D.Float col6Line = new Point2D.Float(pageWidth - marginRight, col1Line.y);
    static Point2D.Float col1Text = new Point2D.Float(col1Line.x + toPix(2), col1Line.y - toPix(8));
    static Point2D.Float col2Text = new Point2D.Float(col2Line.x + toPix(1), col1Text.y);
    static Point2D.Float col3Text = new Point2D.Float(col3Line.x + toPix(1), col1Text.y);
    static Point2D.Float col4Text = new Point2D.Float(col4Line.x + toPix(1), col1Text.y);
    static Point2D.Float col5Text = new Point2D.Float(col5Line.x + toPix(1), col1Text.y);

    static PDDocument build(Palette palette) throws IOException {
        document = new PDDocument();
        font = PDType0Font.load(document, new File("fonts/bahnschrift.ttf"));
        PDDocumentInformation docInfo = document.getDocumentInformation();
        docInfo.setTitle(palette.getName());
        List<PageData> palettePages = distributeChips(palette);
        buildPagesPalette(palettePages);
        List<PageIndex> indexPages = distributeIndex(palettePages);
        buildPagesIndex(indexPages, palette.getName());
        return document;
    }

    private static void buildPagesIndex(List<PageIndex> indexPages, String title) throws IOException {
        for (PageIndex pageIndex : indexPages) {
            PDPage pdPage = new PDPage(PDRectangle.A4);
            PDPageContentStream content = new PDPageContentStream(document, pdPage);
            content.setNonStrokingColor(Color.BLACK);
            content.setStrokingColor(Color.BLACK);
            content.setLineWidth(1);
            // page title
            content.setFont(font, 24);
            content.beginText();
            content.newLineAtOffset(tittlePos.x, tittlePos.y);
            content.showText(title + " Index");
            content.endText();
            content.beginText();
            content.newLineAtOffset(numberPos.x, numberPos.y);
            content.showText(pageIndex.number + "/" + indexPages.size());
            content.endText();
            // table cover
            content.addRect(col1Line.x, col1Text.y, toPix(180), toPix(8));
            content.stroke();
            content.setStrokingColor(Color.GRAY);
            content.moveTo(col2Line.x, col2Line.y);
            content.lineTo(col2Line.x, col2Text.y);
            content.moveTo(col3Line.x, col3Line.y);
            content.lineTo(col3Line.x, col4Text.y);
            content.moveTo(col4Line.x, col4Line.y);
            content.lineTo(col4Line.x, col3Text.y);
            content.moveTo(col5Line.x, col5Line.y);
            content.lineTo(col5Line.x, col5Text.y);
            content.stroke();
            content.setFont(font, 14);
            content.beginText();
            content.newLineAtOffset(col1Text.x, col1Text.y + toPix(2));
            content.showText("\u041D\u0430\u0437\u0432\u0430\u043D\u0438\u0435");
            content.endText();
            content.beginText();
            content.newLineAtOffset(col2Text.x, col2Text.y + toPix(2));
            content.showText("\u041E\u043F\u0438\u0441\u0430\u043D\u0438\u0435");
            content.endText();
            content.beginText();
            content.newLineAtOffset(col3Text.x, col3Text.y + toPix(2));
            content.showText("CMYK(C)");
            content.endText();
            content.beginText();
            content.newLineAtOffset(col4Text.x, col4Text.y + toPix(2));
            content.showText("CMYK(U)");
            content.endText();
            content.beginText();
            content.newLineAtOffset(col5Text.x, col5Text.y + toPix(2));
            content.showText("RGB");
            content.endText();
            // table
            content.setStrokingColor(Color.BLACK);
            content.addRect(col1Line.x, marginDown, toPix(180), toPix(259));
            content.stroke();
            float curY = col1Text.y;
            for(IndexItem record : pageIndex.records){
                if(record instanceof GroupItem){
                    curY -= toPix(8);
                    content.setFont(font, 14);
                    content.beginText();
                    content.newLineAtOffset(col1Text.x, curY + toPix(2));
                    content.showText(((GroupItem) record).title);
                    content.endText();
                    content.beginText();
                    content.newLineAtOffset(numberPos.x, curY + toPix(2));
                    content.showText(((GroupItem) record).page);
                    content.endText();
                    content.setStrokingColor(Color.BLACK);
                    content.addRect(col1Line.x, curY, toPix(180), toPix(8));
                    content.stroke();
                }
                else if(record instanceof ChipItem){
                    curY-=toPix(6);
                    content.setFont(font, 12);
                    content.beginText();
                    content.newLineAtOffset(col1Text.x, curY + toPix(1));
                    ColorChip chip = ((ChipItem) record).chip;
                    content.showText(chip.name);
                    content.endText();
                    content.beginText();
                    content.newLineAtOffset(col2Text.x, curY + toPix(1));
                    content.showText(chip.description);
                    content.endText();
                    content.setFont(font, 12);
                    content.beginText();
                    content.newLineAtOffset(col3Text.x, curY + toPix(1));
                    content.showText(chip.colorCMYKc.trim());
                    content.endText();
                    content.beginText();
                    content.newLineAtOffset(col4Text.x, curY + toPix(1));
                    content.showText(chip.colorCMYKu.trim());
                    content.endText();
                    content.beginText();
                    content.newLineAtOffset(col5Text.x, curY + toPix(1));
                    content.showText(chip.colorRGB.trim());
                    content.endText();
                }
            }
            content.close();
            document.addPage(pdPage);
        }
    }

    private static List<PageIndex> distributeIndex(List<PageData> pages) {
        List<PageIndex> result = new ArrayList<>();
        int curY = 0; // from 0 to 259
        int curPage = 1;
        PageIndex pageIndex = new PageIndex();
        pageIndex.number = curPage;
        pageIndex.records = new ArrayList<>();
        for (PageData colorPage : pages) {
            IndexItem item = new GroupItem(colorPage.title, colorPage.number);
            if (curY + 8 >= 259) {
                result.add(pageIndex);
                curPage++;
                pageIndex = new PageIndex();
                pageIndex.number = curPage;
                pageIndex.records = new ArrayList<>();
                curY = 0;
            }
            pageIndex.records.add(item);
            curY += 8;
            for (ColorChip chip : colorPage.chips) {
                item = new ChipItem(chip);
                if (curY + 6 >= 259) {
                    result.add(pageIndex);
                    curPage++;
                    pageIndex = new PageIndex();
                    pageIndex.number = curPage;
                    pageIndex.records = new ArrayList<>();
                    curY = 0;
                }
                pageIndex.records.add(item);
                curY += 6;
            }
        }
        result.add(pageIndex);
        return result;
    }

    private static void buildPagesPalette(List<PageData> pages) throws IOException {
        for (PageData pageData : pages) {
            PDPage pdPage = new PDPage(PDRectangle.A4);
            PDPageContentStream content = new PDPageContentStream(document, pdPage);
            PDFont font = PDType0Font.load(document, new File("fonts/bahnschrift.ttf"));
            content.setFont(font, 24);
            content.setNonStrokingColor(Color.BLACK);
            content.beginText();
            content.newLineAtOffset(tittlePos.x, tittlePos.y);
            content.showText(pageData.title);
            content.endText();
            content.setStrokingColor(Color.BLACK);
            content.setLineWidth(1);
            content.moveTo(marginLeft, pageHeight - marginUp - toPix(10));
            content.lineTo(pageWidth - marginRight, pageHeight - marginUp - toPix(10));
            content.stroke();
            content.beginText();
            content.newLineAtOffset(numberPos.x, numberPos.y);
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
                    if (group.name != null && !group.name.isEmpty()) {
                        pageData.title = String.format("%s ( %d / %d )", group.name, pageNumber, totalPages);
                    } else {
                        pageData.title = String.format("%d / %d", pageNumber, totalPages);
                    }
                    pageData.number = groupNumber + "G" + pageNumber;
                }
                int lastChipIndex = i + pageCapacity;
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

    static class IndexItem {
    }

    static class ChipItem extends IndexItem {
        ColorChip chip;

        public ChipItem(ColorChip chip) {
            this.chip = chip;
        }
    }

    static class GroupItem extends IndexItem {
        String title, page;

        public GroupItem(String title, String page) {
            this.title = title;
            this.page = page;
        }
    }

    static class PageIndex {
        int number;
        List<IndexItem> records;
    }
}
