package superbro.palette.model;

import java.util.List;

public class ColorGroup {
    public String name;
    public ChipLayout layout;
    public List<ColorChip> chips;

    public enum ChipLayout {
        Narrow(25, 25),
        Wide(40, 12);

        public final int pageCapacity, rowCapacity;
        public final float gridW, gridH, chipW, chipH;

        ChipLayout(float chipW, float chipH) {
            this.chipW = chipW;
            this.chipH = chipH;
            this.gridW = chipW + 5;
            this.gridH = chipH + 8;
            this.rowCapacity = (int) Math.floor(180 / gridW);
            this.pageCapacity = (int) Math.floor(267 / gridH) * rowCapacity;
        }

        ChipLayout(int pageCapacity, int rowCapacity, float gridW, float gridH, float chipW, float chipH) {
            this.pageCapacity = pageCapacity;
            this.rowCapacity = rowCapacity;
            this.gridW = gridW;
            this.gridH = gridH;
            this.chipW = chipW;
            this.chipH = chipH;
        }
    }
}
