package superbro.palette.model;

import java.util.List;

public class ColorGroup {
    public String name;
    public ChipLayout layout;
    public List<ColorChip> chips;

    public enum ChipLayout {
        Narrow, Wide;

        public int getPageCapacity(){
            switch (this) {
                case Narrow:
                    return 66;
                case Wide:
                    return 56;
                default:
                    return 0;
            }
        }

        public int getRowCapacity() {
            switch (this) {
                case Narrow:
                    return 6;
                case Wide:
                    return 4;
                default:
                    return 0;
            }
        }

        public float getGridW() {
            switch (this) {
                case Narrow:
                    return 30;
                case Wide:
                    return 45;
                default:
                    return 0;
            }
        }

        public float getGridH() {
            switch (this) {
                case Narrow:
                    return 23;
                case Wide:
                    return 18;
                default:
                    return 0;
            }
        }

        public float getChipW() {
            switch (this) {
                case Narrow:
                    return 25;
                case Wide:
                    return 40;
                default:
                    return 0;
            }
        }

        public float getChipH() {
            switch (this) {
                case Narrow:
                    return 15;
                case Wide:
                    return 10;
                default:
                    return 0;
            }
        }
    }

}
