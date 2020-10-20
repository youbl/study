package beinet.cn.logdemoredis.redis.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class AspectBase {

    protected static ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

    protected abstract org.slf4j.Logger getLog();

    protected void doLog(Dto dto) {
        if (dto.getExp() != null) {
            getLog().warn(serial(dto));
        } else {
            getLog().debug(serial(dto));
        }
    }

    protected String serial(Object[] obj) {
        if (obj == null || obj.length <= 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (Object item : obj) {
            if (sb.length() > 0)
                sb.append(",");
            sb.append(serial(item));
        }
        sb.insert(0, "[").append("]");
        return sb.toString();
    }

    protected String serial(Object obj) {
        if (obj == null)
            return "";
        try {
            if (obj instanceof byte[]) {
                obj = new String((byte[]) obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            getLog().error(String.format("%s serialize error: %s", obj.getClass().getName(), ex.toString()));
            return obj.toString();
        }
    }

}
