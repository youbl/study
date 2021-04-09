package beinet.cn.reflectionstudy;

import java.lang.reflect.Array;
import java.util.*;

public class SomeMethod {
//    public long LongMethod1() {
//        return 1;
//    }
//
//    public Long LongMethod2() {
//        return 1L;
//    }

    public Map<Long, Dto> LongMethodMap1() {
        return LongMethodMap2();
    }

    public HashMap<Long, Dto> LongMethodMap2() {
        HashMap<Long, Dto> ret = new HashMap<>();
        Dto dto = new Dto();
        dto.setId(123);
        dto.setName("aaa");
        ret.put(123L, dto);

        dto = new Dto();
        dto.setId(456);
        dto.setName("bbb");
        ret.put(1234L, dto);
        return ret;
    }

    public List<Long> LongMethodList() {
        List<Long> ret = new ArrayList<>();
        ret.add(123L);
        ret.add(1234L);
        return ret;
    }

    public Long[] LongMethodArray1() {
        Long[] ret = new Long[2];
        ret[0] = 123L;
        ret[1] = 456L;
        return ret;
    }

    public long[] LongMethodArray2() {
        long[] ret = new long[2];
        ret[0] = 123L;
        ret[1] = 456L;
        return ret;
    }

    public List<Dto> DtoMethodList() {
        Dto[] ret = new Dto[2];
        ret[0] = new Dto();
        ret[0].setId(123);
        ret[0].setName("aaa");
        ret[1] = new Dto();
        ret[1].setId(456);
        ret[1].setName("bbb");

        List<Dto> arr = new ArrayList<>(ret.length);
        Collections.addAll(arr, ret);//        arr.addAll(ret);
        return arr;// Arrays.asList(ret);
    }

    public Dto[] DtoMethodArray() {
        Dto[] ret = new Dto[2];
        ret[0] = new Dto();
        ret[0].setId(123);
        ret[0].setName("aaa");
        ret[1] = new Dto();
        ret[1].setId(456);
        ret[1].setName("bbb");
        return ret;
    }
}
