package beinet.cn.springjpastudy.dto;

import java.time.LocalDateTime;

/**
 * Aaa的dto对象
 *
 * @author youbl
 * @version 1.0
 * @date 2021/05/25 14:22
 */
public class AaaDto {
    private long id;
    private long restXXXId;
    private int dishhour;
    private long dishId;
    private int num;
    private LocalDateTime creationTime;

    public AaaDto() {
    }

    public long getId() {
        return this.id;
    }

    public int getDishhour() {
        return this.dishhour;
    }

    public long getDishId() {
        return this.dishId;
    }

    public int getNum() {
        return this.num;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDishhour(int dishhour) {
        this.dishhour = dishhour;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public long getRestXXXId() {
        return restXXXId;
    }

    public void setRestXXXId(long restXXXId) {
        this.restXXXId = restXXXId;
    }
}
