package kr.co.board.model.enums;

public enum SortType {
    CREATED_AT("최신순", "createdAt,desc"),
    HIT("조회순", "hit,desc");

    private String title;
    private String url;

    SortType(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }
}
