package models;

public class Link {
    private String href;
    private String rel;
    private String type;

    public Link(String href, String rel, String type) {
        this.href = href;
        this.rel = rel;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }

    public String getType() {
        return type;
    }
}
