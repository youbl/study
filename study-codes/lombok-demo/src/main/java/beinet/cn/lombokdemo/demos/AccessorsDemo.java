package beinet.cn.lombokdemo.demos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/1 11:26
 */
@Data
@Accessors(chain = true)
public class AccessorsDemo {

    private int id;

    private String name;
}

/**
public class AccessorsDemo {
    private int id;
    private String name;

    public AccessorsDemo setId(final int id) {
        this.id = id;
        return this;
    }

    public AccessorsDemo setName(final String name) {
        this.name = name;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof AccessorsDemo) {
            AccessorsDemo other = (AccessorsDemo) o;
            if (other.canEqual(this) && getId() == other.getId()) {
                Object this$name = getName();
                Object other$name = other.getName();
                return this$name == null ? other$name == null : this$name.equals(other$name);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AccessorsDemo;
    }

    public int hashCode() {
        int result = (1 * 59) + getId();
        Object $name = getName();
        return (result * 59) + ($name == null ? 43 : $name.hashCode());
    }

    public String toString() {
        return "AccessorsDemo(id=" + getId() + ", name=" + getName() + ")";
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}

 */