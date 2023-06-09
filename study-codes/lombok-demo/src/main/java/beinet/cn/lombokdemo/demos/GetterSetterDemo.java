package beinet.cn.lombokdemo.demos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author youbl
 * @since 2023/5/30 17:02
 */
public class GetterSetterDemo {
    @Getter
    public static class Dto1 {
        private int id;

        private String name;
    }

    @Setter
    public static class Dto2 {
        private int id;

        private String name;
    }

    public static class Dto3 {
        @Getter
        private int id;
        @Setter
        private String name;
    }
}

/*
反编译得到的代码，可以看到lombok注入的方法明细：
public class GetterSetterDemo {
    public static class Dto1 {
        private int id;
        private String name;

        public int getId() {
            return this.id;
        }
        public String getName() {
            return this.name;
        }
    }

    public static class Dto2 {
        private int id;
        private String name;

        public void setId(final int id) {
            this.id = id;
        }
        public void setName(final String name) {
            this.name = name;
        }
    }

    public static class Dto3 {
        private int id;
        private String name;

        public int getId() {
            return this.id;
        }
        public void setName(final String name) {
            this.name = name;
        }
    }
}
* */