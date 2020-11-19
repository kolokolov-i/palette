package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.IOException;

import static superbro.palette.generator.GeometryUtil.toPix;

public class GroupItem extends IndexItem {
    String title, page;

    public GroupItem(String title, String page) {
        this.title = title;
        this.page = page;
    }

    int getGap() {
        return 8;
    }

    @Override
    void render(float curY, PDPageContentStream content) throws IOException {
        content.setFont(RenderUtil.font, 14);
        content.beginText();
        content.newLineAtOffset(RenderUtil.col2Text.x, curY + toPix(2));
        content.showText(title);
        content.endText();
        content.beginText();
        content.newLineAtOffset(RenderUtil.col1Text.x, curY + toPix(2));
        content.showText(page);
        content.endText();
        content.setStrokingColor(Color.BLACK);
        content.addRect(RenderUtil.col1Line.x, curY, toPix(180), toPix(8));
        content.stroke();
    }
}
