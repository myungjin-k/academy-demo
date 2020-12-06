package my.myungjin.academyDemo.web.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@ToString
public class PageRequest {
    private int page;
    private int size;
    private Sort.Direction direction;

    public void setPage(int page){
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size){
        final int DEFAULT_SIZE = 5;
        final int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Sort.Direction direction){
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of(){
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createAt");
    }
}
