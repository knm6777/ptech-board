package kr.co.board.model.enums;

public enum Author {
    ADMIN("관리자"),
    MEMBER("일반회원");

    private final String title;

    Author(String title) {
        this.title = title;
    }

    public String authority() {
        return "ROLE_" + this.name();
    }

    public String getTitle() {
        return this.title;
    }
}
