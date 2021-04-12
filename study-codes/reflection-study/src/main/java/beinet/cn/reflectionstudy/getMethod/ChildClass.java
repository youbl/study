package beinet.cn.reflectionstudy.getMethod;

public class ChildClass extends ParentClass {
    public int getInt1() {
        return 234;
    }

    @Override
    protected String getStr1() {
        return "567";
    }

    private Long getLong1() {
        return 111L;
    }

    public int getInt2() {
        return 222;
    }
}
