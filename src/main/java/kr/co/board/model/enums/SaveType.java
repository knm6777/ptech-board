package kr.co.board.model.enums;

public enum SaveType {
    ATTACHMENT("첨부파일"),
    SUMMERNOTE("써머노트");

    private final String title;

    SaveType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}

