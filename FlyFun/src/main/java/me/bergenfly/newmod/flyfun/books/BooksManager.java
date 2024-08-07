package me.fly.newmod.flyfun.books;

import me.bergenfly.newmod.core.api.item.ModItem;

import java.util.HashMap;
import java.util.Map;

public class BooksManager {
    private final Map<ModItem, Integer> pages = new HashMap<>();

    public void setPages(ModItem item, int pages) {
        this.pages.put(item, pages);
    }

    public int getPages(ModItem item) {
        return pages.get(item);
    }

    public boolean writable(ModItem item) {
        return pages.containsKey(item);
    }
}
