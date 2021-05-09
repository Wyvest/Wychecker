package net.wyvest.wychecker.checker;

public class NeededMod {
    public final String name;
    public final String fileName;
    public final String url;
    public final String modId;

    public NeededMod(String name, String modId, String fileName, String url) {
        this.name = name;
        this.fileName = fileName;
        this.url = url;
        this.modId = modId;
    }

}
