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
    private String sort;


    public String getQuery() {
        StringJoiner stringJoiner = new StringJoiner("&");
        try {
            if (this.searchType != null && !StringUtils.isNullOrEmpty(this.searchWord)) {
                stringJoiner.add(String.format("searchType=%s", this.searchType));
                stringJoiner.add(String.format("searchWord=%s", URLEncoder.encode(this.searchWord, "UTF-8")));
            }
            if (this.sort != null && !this.sort.equals("")) {
                stringJoiner.add(String.format("sort=%s", URLEncoder.encode(this.sort, "UTF-8")));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringJoiner.toString();
    }
}
