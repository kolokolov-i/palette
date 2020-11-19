package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

import static superbro.palette.generator.GeometryUtil.toPix;

public class ExtraItem extends IndexItem {
    String text;

    public ExtraItem(String text) {
        this.text = text;
    }

    int getGap() {
        return 6;
    }

    @Override
    void render(float curY, PDPageContentStream content) throws IOException {
        content.setFont(RenderUtil.font, 12);
        content.beginText();
        content.newLineAtOffset(RenderUtil.col2Text.x, curY + toPix(1));
        content.showText("\u0421\u043F\u0435\u0446.\u043A\u0440\u0430\u0441\u043A\u0430: " + text);
        content.endText();
    }
}
