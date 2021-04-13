package beinet.cn.reflectionstudy;

import lombok.Data;

@Data
public class DtoGeneric<T> {
    private int id;
    private String name;

    private T obj;
}
