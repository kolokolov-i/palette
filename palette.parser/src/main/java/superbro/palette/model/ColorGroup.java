package superbro.palette.model;

import java.util.List;

public class ColorGroup {
    public String name;
    public ChipLayout layout;
    public List<ColorChip> chips;

    public enum ChipLayout{
        Narrow, Wide
    }
}
