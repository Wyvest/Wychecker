package net.wyvest.wychecker.checker;

public class BadMod {
    public final String name;
    public final String replacement;
    public final String fileName;
    public final String url;
    public final String modId;

    public BadMod(String name, String replacement, String modId, String fileName, String url) {
        this.name = name;
        this.replacement = replacement;
        this.fileName = fileName;
        this.url = url;
        this.modId = modId;
    }
}
