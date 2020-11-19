package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import superbro.palette.model.ColorChip;

import java.io.IOException;

import static superbro.palette.generator.GeometryUtil.toPix;

public class ChipItem extends IndexItem {
    ColorChip chip;

    public ChipItem(ColorChip chip) {
        this.chip = chip;
    }

    int getGap() {
        return 6;
    }

    @Override
    void render(float curY, PDPageContentStream content) throws IOException {
        content.setFont(RenderUtil.font, 12);
        content.beginText();
        content.newLineAtOffset(RenderUtil.col1Text.x, curY + toPix(1));
        content.showText(chip.name);
        content.endText();
        if(chip.description != null){
            content.beginText();
            content.newLineAtOffset(RenderUtil.col2Text.x, curY + toPix(1));
            content.showText(chip.description);
            content.endText();
        }
        content.beginText();
        content.newLineAtOffset(RenderUtil.col3Text.x, curY + toPix(1));
        content.showText(chip.colorCMYKc.trim());
        content.endText();
        content.beginText();
        content.newLineAtOffset(RenderUtil.col4Text.x, curY + toPix(1));
        content.showText(chip.colorCMYKu.trim());
        content.endText();
        content.beginText();
        content.newLineAtOffset(RenderUtil.col5Text.x, curY + toPix(1));
        content.showText(chip.colorRGB.trim());
        content.endText();
    }
}
