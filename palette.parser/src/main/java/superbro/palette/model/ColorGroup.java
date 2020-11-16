package superbro.palette.model;

import java.util.List;

public class ColorGroup {
    public String name;
    public ChipLayout layout;
    public List<ColorChip> chips;

    public enum ChipLayout {
        Narrow(66, 6, 30, 23, 25, 15),
        Wide(56, 4, 45, 18, 40, 10);

        public final int pageCapacity, rowCapacity;
        public final float gridW, gridH, chipW, chipH;

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
