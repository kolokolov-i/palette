package superbro.palette.model;

import java.util.List;

public class Palette {
    private String name;
    private List<ColorGroup> groups;

    public Palette(String name, List<ColorGroup> groups) {
        this.name = name;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public List<ColorGroup> getGroups() {
        return groups;
    }
}
