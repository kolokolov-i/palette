package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

class GeometryUtil {

    // PAGE
    final static float pageWidth = PDRectangle.A4.getWidth();
    final static float pageHeight = PDRectangle.A4.getHeight();

    // MARGINS
    final static float marginLeft = toPix(20);
    final static float marginRight = toPix(10);
    final static float marginUp = toPix(10);
    final static float marginDown = toPix(10);

    // COLOR TABLE
    final static float ctGroupCW = toPix(20);
    final static float ctChipsCW = pageWidth - marginLeft - marginRight - ctGroupCW;
    final static float ctRowH = toPix(15);

    static float toPix(float mm){
        return (float) (mm/25.4*72);
    }
}
