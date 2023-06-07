package beinet.cn.lombokdemo.demos;

import lombok.*;

/**
 * @author youbl
 * @since 2023/5/30 17:02
 */
public class DataDemo {
    @Data
    public static class Dto1 {
        private int id;

        private String name;
    }


    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Dto2 {
        private int id;

        private String name;
    }

    public static class Dto3 {
        private int id;

        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    @Data
    public static class DtoChild1 extends Dto1 {
        private int age;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DtoChild2 extends Dto1 {
        private int age;
    }
}

/*
反编译得到的代码，可以看到lombok注入的方法明细：
package beinet.cn.lombokdemo.demos;

public class DataDemo {

    public static class Dto1 {
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Dto1)) {
                return false;
            }
            Dto1 other = (Dto1) o;
            if (!other.canEqual(this) || getId() != other.getId()) {
                return false;
            }
            Object this$name = getName();
            Object other$name = other.getName();
            return this$name == null ? other$name == null : this$name.equals(other$name);
        }

        protected boolean canEqual(Object other) {
            return other instanceof Dto1;
        }

        public int hashCode() {
            int result = (1 * 59) + getId();
            Object $name = getName();
            return (result * 59) + ($name == null ? 43 : $name.hashCode());
        }

        public String toString() {
            return "DataDemo.Dto1(id=" + getId() + ", name=" + getName() + ")";
        }

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

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Dto2)) {
                return false;
            }
            Dto2 other = (Dto2) o;
            if (!other.canEqual(this) || getId() != other.getId()) {
                return false;
            }
            Object this$name = getName();
            Object other$name = other.getName();
            return this$name == null ? other$name == null : this$name.equals(other$name);
        }

        protected boolean canEqual(Object other) {
            return other instanceof Dto2;
        }

        public int hashCode() {
            int result = (1 * 59) + getId();
            Object $name = getName();
            return (result * 59) + ($name == null ? 43 : $name.hashCode());
        }

        public String toString() {
            return "DataDemo.Dto2(id=" + getId() + ", name=" + getName() + ")";
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class Dto3 {
        private int id;
        private String name;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class DtoChild1 extends Dto1 {
        private int age;

        public void setAge(int age) {
            this.age = age;
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DtoChild1)) {
                return false;
            }
            DtoChild1 other = (DtoChild1) o;
            return other.canEqual(this) && getAge() == other.getAge();
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        protected boolean canEqual(Object other) {
            return other instanceof DtoChild1;
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        public int hashCode() {
            return (1 * 59) + getAge();
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        public String toString() {
            return "DataDemo.DtoChild1(age=" + getAge() + ")";
        }

        public int getAge() {
            return this.age;
        }
    }

    public static class DtoChild2 extends Dto1 {
        private int age;

        public void setAge(int age) {
            this.age = age;
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        public String toString() {
            return "DataDemo.DtoChild2(age=" + getAge() + ")";
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof DtoChild2)) {
                return false;
            }
            DtoChild2 other = (DtoChild2) o;
            return other.canEqual(this) && super.equals(o) && getAge() == other.getAge();
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        protected boolean canEqual(Object other) {
            return other instanceof DtoChild2;
        }

        @Override // beinet.cn.lombokdemo.demos.DataDemo.Dto1
        public int hashCode() {
            return (super.hashCode() * 59) + getAge();
        }

        public int getAge() {
            return this.age;
        }
    }
}
* */