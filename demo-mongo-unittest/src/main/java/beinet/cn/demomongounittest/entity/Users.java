package beinet.cn.demomongounittest.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "users") // 如果collection等于类名，可以不写Document注解
public class Users {
    @Id
    private long id;
    private String name;
    private int gender;
    private String desc;
    private LocalDateTime birthday;
}
