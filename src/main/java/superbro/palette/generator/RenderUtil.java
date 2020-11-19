package superbro.palette.generator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import superbro.palette.model.ColorChip;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;

import static superbro.palette.generator.GeometryUtil.*;
import static superbro.palette.generator.GeometryUtil.toPix;

public class RenderUtil {
    public static PDFont font;
    public static Point2D.Float tittlePos = new Point2D.Float(marginLeft + toPix(2), pageHeight - marginUp - toPix(8));
    public static Point2D.Float numberPos = new Point2D.Float(pageWidth - marginRight - toPix(18), pageHeight - marginUp - toPix(8));
    public static Point2D.Float col1Line = new Point2D.Float(marginLeft, pageHeight - marginUp - toPix(10));
    public static Point2D.Float col2Line = new Point2D.Float(col1Line.x + toPix(30), col1Line.y);
    public static Point2D.Float col3Line = new Point2D.Float(col2Line.x + toPix(70), col1Line.y);
    public static Point2D.Float col4Line = new Point2D.Float(col3Line.x + toPix(30), col1Line.y);
    public static Point2D.Float col5Line = new Point2D.Float(col4Line.x + toPix(30), col1Line.y);
    public static Point2D.Float col6Line = new Point2D.Float(pageWidth - marginRight, col1Line.y);
    public static Point2D.Float col1Text = new Point2D.Float(col1Line.x + toPix(2), col1Line.y - toPix(8));
    public static Point2D.Float col2Text = new Point2D.Float(col2Line.x + toPix(1), col1Text.y);
    public static Point2D.Float col3Text = new Point2D.Float(col3Line.x + toPix(1), col1Text.y);
    public static Point2D.Float col4Text = new Point2D.Float(col4Line.x + toPix(1), col1Text.y);
    public static Point2D.Float col5Text = new Point2D.Float(col5Line.x + toPix(1), col1Text.y);

    public static void renderIndexTableCover(String title, PDPageContentStream content, String number) throws IOException {
        // page title
        content.setFont(font, 24);
        content.beginText();
        content.newLineAtOffset(tittlePos.x, tittlePos.y);
        content.showText(title + " Index");
        content.endText();
        content.beginText();
        content.newLineAtOffset(numberPos.x, numberPos.y);
        content.showText(number);
        content.endText();
        // table cover
        content.addRect(col1Line.x, col1Text.y, toPix(180), toPix(8));
        content.stroke();
        content.setStrokingColor(Color.GRAY);
//        content.moveTo(col2Line.x, col2Line.y);
//        content.lineTo(col2Line.x, col2Text.y);
//        content.moveTo(col3Line.x, col3Line.y);
//        content.lineTo(col3Line.x, col4Text.y);
//        content.moveTo(col4Line.x, col4Line.y);
//        content.lineTo(col4Line.x, col3Text.y);
//        content.moveTo(col5Line.x, col5Line.y);
//        content.lineTo(col5Line.x, col5Text.y);
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
    }

    public static void renderPalettePage(DocumentBuilder.PageData pageData, PDPageContentStream content) throws IOException {
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
            content.newLineAtOffset(curX, curY + toPix(2));
            content.showText(colorChip.extra != null ? colorChip.name + " (!)" : colorChip.name);
            content.endText();
            content.setNonStrokingColor(Color.decode(colorChip.colorRGB));
            content.addRect(curX, curY - toPix(chipH), toPix(chipW), toPix(chipH));
            content.fill();
            curX += toPix(gridW);
            curColumn++;
        }
    }
}
