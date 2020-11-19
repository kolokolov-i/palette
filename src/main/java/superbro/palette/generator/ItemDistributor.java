package superbro.palette.generator;

import superbro.palette.model.ColorChip;
import superbro.palette.model.ColorGroup;
import superbro.palette.model.Palette;

import java.util.ArrayList;
import java.util.List;

public class ItemDistributor {
    public static List<DocumentBuilder.PageIndex> distributeIndex(List<DocumentBuilder.PageData> pages) {
        List<DocumentBuilder.PageIndex> result = new ArrayList<>();
        int curY = 0; // from 0 to 259
        int curPage = 1;
        DocumentBuilder.PageIndex pageIndex = new DocumentBuilder.PageIndex();
        pageIndex.number = curPage;
        pageIndex.records = new ArrayList<>();
        for (DocumentBuilder.PageData colorPage : pages) {
            IndexItem item = new GroupItem(colorPage.title, colorPage.number);
            if (curY + 8 >= 259) {
                result.add(pageIndex);
                curPage++;
                pageIndex = new DocumentBuilder.PageIndex();
                pageIndex.number = curPage;
                pageIndex.records = new ArrayList<>();
                curY = 0;
            }
            pageIndex.records.add(item);
            curY += 8;
            for (ColorChip chip : colorPage.chips) {
                item = new ChipItem(chip);
                int gap = chip.extra != null ? 12 : 6;
                if (curY + gap >= 259) {
                    result.add(pageIndex);
                    curPage++;
                    pageIndex = new DocumentBuilder.PageIndex();
                    pageIndex.number = curPage;
                    pageIndex.records = new ArrayList<>();
                    curY = 0;
                }
                pageIndex.records.add(item);
                if (chip.extra != null) {
                    pageIndex.records.add(new ExtraItem(chip.extra));
                }
                curY += gap;
            }
        }
        result.add(pageIndex);
        return result;
    }

    public static List<DocumentBuilder.PageData> distributeChips(Palette palette) {
        List<DocumentBuilder.PageData> result = new ArrayList<>();
        List<ColorGroup> groups = palette.getGroups();
        int groupNumber = 1;
        for (ColorGroup group : groups) {
            int pageCapacity = group.layout.pageCapacity;
            List<ColorChip> chips = group.chips;
            int totalPages = (int) Math.ceil(chips.size() / (double) pageCapacity);
            for (int i = 0, pageNumber = 1; i < chips.size(); i += pageCapacity, pageNumber++) {
                DocumentBuilder.PageData pageData = new DocumentBuilder.PageData();
                pageData.layout = group.layout;
                if (totalPages == 1) {
                    pageData.title = group.name;
                    pageData.number = groupNumber + "G" + pageNumber;
                } else {
                    if (group.name != null && !group.name.isEmpty()) {
                        pageData.title = String.format("%s ( %d / %d )", group.name, pageNumber, totalPages);
                    } else {
                        pageData.title = String.format("%d / %d", pageNumber, totalPages);
                    }
                    pageData.number = groupNumber + "G" + pageNumber;
                }
                int lastChipIndex = i + pageCapacity;
                lastChipIndex = Math.min(lastChipIndex, chips.size());
                pageData.chips = chips.subList(i, lastChipIndex);
                result.add(pageData);
            }
            groupNumber++;
        }
        return result;
    }
}
