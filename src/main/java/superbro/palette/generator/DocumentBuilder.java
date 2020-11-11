package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;
import superbro.palette.model.Palette;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        for(PageData pageData : pages){
            PDPage pdPage = new PDPage(PDRectangle.A4);
            PDPageContentStream content = new PDPageContentStream(document, pdPage);
            PDFont font = PDType0Font.load(document, new File("GOTHIC.TTF"));
            content.setFont(font, 24);
            content.setNonStrokingColor(Color.BLACK);
            content.beginText();
            content.newLineAtOffset(toPix(0), toPix(0));
            content.showText(pageData.title);
            content.endText();
//
//            float curX, curY;
//            curY = pageHeight - marginUp;
//            for (ColorGroup group : palette.getGroups()) {
//                curX = marginLeft;
//                float chipW = ctChipsCW / group.getChips().size();
//                curX += ctGroupCW;
//                for (ColorChip chip : group.getChips()) {
//                    content.setNonStrokingColor(chip.getColor());
//                    content.addRect(curX, curY - ctRowH, chipW - 5, ctRowH - 5);
//                    content.fill();
//                    curX += chipW;
//                }
//                curY -= ctRowH;
//            }
            content.close();
            document.addPage(pdPage);
        }
    }

    private static List<PageData> distributeChips(Palette palette) {
        List<PageData> result = new ArrayList<>();
        List<ColorGroup> groups = palette.getGroups();
        int groupNumber = 1;
        for(ColorGroup group : groups){
            int pageCapacity = 0;
            switch(group.layout){
                case Narrow:
                    pageCapacity = 66;
                    break;
                case Wide:
                    pageCapacity = 56;
                    break;
                default:
            }
            List<ColorChip> chips = group.chips;
            int totalPages = (int) Math.ceil(chips.size() / (double) pageCapacity);
            for(int i = 0, pageNumber = 1; i<chips.size(); i+=pageCapacity, pageNumber++){
                PageData pageData = new PageData();
                if(totalPages == 1){
                    pageData.title = group.name;
                    pageData.number = groupNumber + "G";
                }
                else{
                    pageData.title = String.format("%s ( %d / %d )", group.name, pageNumber, totalPages);
                    pageData.number = groupNumber + "G" + pageNumber;
                }
                int lastChipIndex = i + pageCapacity - 1;
                lastChipIndex = lastChipIndex >= chips.size() ?  chips.size() - 1 : lastChipIndex;
                pageData.chips = chips.subList(i, lastChipIndex).toArray(new ColorChip[0]);
                result.add(pageData);
            }
            groupNumber++;
        }
        return result;
    }

    static class PageData{
        String title;
        String number;
        ColorChip[] chips;
    }
}
