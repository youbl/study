package beinet.cn.demomall;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private int id;
    private String name;
    private int num;
    private LocalDateTime creationTime;

    private String mallName;
    private LocalDateTime mallTime;
}
