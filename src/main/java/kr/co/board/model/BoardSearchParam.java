package kr.co.board.model;

import com.mysql.cj.util.StringUtils;
import kr.co.board.model.enums.SearchType;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

@Getter
@Setter
public class BoardSearchParam {
    private SearchType searchType;
    private String searchWord;
    private Long institutionId;
    private boolean isHidden = true;

    public String getQuery() {
        StringJoiner stringJoiner = new StringJoiner("&");
        try {
            if (this.searchType != null) {
                stringJoiner.add(String.format("searchType=%s", this.searchType));
            }
            if (!StringUtils.isNullOrEmpty(this.searchWord)) {
                stringJoiner.add(String.format("searchWord=%s", URLEncoder.encode(this.searchWord, "UTF-8")));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringJoiner.toString();
    }
}
