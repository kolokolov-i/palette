package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

public abstract class IndexItem {
    abstract int getGap();

    abstract void render(float curY, PDPageContentStream content) throws IOException;
}

