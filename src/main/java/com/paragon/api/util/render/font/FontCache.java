package com.paragon.api.util.render.font;

import org.lwjgl.opengl.GL11;

/**
 * @author Cosmos
 */
public class FontCache {
    int displayList;
    long lastUsage;
    boolean deleted = false;

    public FontCache(int displayList, long lastUsage, boolean deleted) {
        this.displayList = displayList;
        this.lastUsage = lastUsage;
        this.deleted = deleted;
    }

    public FontCache(int displayList, long lastUsage) {
        this.displayList = displayList;
        this.lastUsage = lastUsage;
    }

    protected void finalize() {
        if (!this.deleted)
            GL11.glDeleteLists(this.displayList, 1);
    }

    public int getDisplayList() {
        return this.displayList;
    }

    public long getLastUsage() {
        return this.lastUsage;
    }

    public void setLastUsage(long lastUsage) {
        this.lastUsage = lastUsage;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
