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
import java.io.File;
import java.io.IOException;
import java.util.List;

import static superbro.palette.generator.GeometryUtil.*;

class DocumentBuilder {

    static PDDocument document;

    static PDDocument build(Palette palette) throws IOException {
        document = new PDDocument();
        RenderUtil.font = PDType0Font.load(document, new File("fonts/bahnschrift.ttf"));
        PDDocumentInformation docInfo = document.getDocumentInformation();
        docInfo.setTitle(palette.getName());
        List<PageData> palettePages = ItemDistributor.distributeChips(palette);
        buildPagesPalette(palettePages);
        List<PageIndex> indexPages = ItemDistributor.distributeIndex(palettePages);
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
            String number = pageIndex.number + "/" + indexPages.size();
            RenderUtil.renderIndexTableCover(title, content, number);
            // table
            content.setStrokingColor(Color.BLACK);
            content.addRect(RenderUtil.col1Line.x, marginDown, toPix(180), toPix(259));
            content.stroke();
            float curY = RenderUtil.col1Text.y;
            boolean lastWasGroup = true; // first row is black
            for (IndexItem record : pageIndex.records) {
                if(!(record instanceof ExtraItem)){
                    if(lastWasGroup){
                        content.setStrokingColor(Color.BLACK);
                    }
                    else{
                        content.setStrokingColor(Color.LIGHT_GRAY);
                    }
                    content.moveTo(RenderUtil.col1Line.x, curY);
                    content.lineTo(RenderUtil.col6Line.x, curY);
                    content.stroke();
                }
                curY -= toPix(record.getGap());
                record.render(curY, content);
                lastWasGroup = record instanceof GroupItem;
            }
            content.setStrokingColor(Color.BLACK);
            content.moveTo(RenderUtil.col1Line.x, curY);
            content.lineTo(RenderUtil.col6Line.x, curY);
            content.stroke();
            content.close();
            document.addPage(pdPage);
        }
    }

    private static void buildPagesPalette(List<PageData> pages) throws IOException {
        for (PageData pageData : pages) {
            PDPage pdPage = new PDPage(PDRectangle.A4);
            PDPageContentStream content = new PDPageContentStream(document, pdPage);
            RenderUtil.renderPalettePage(pageData, content);
            content.close();
            document.addPage(pdPage);
        }
    }

    static class PageData {
        String title;
        String number;
        List<ColorChip> chips;
        ColorGroup.ChipLayout layout;
    }

    static class PageIndex {
        int number;
        List<IndexItem> records;
    }
}
