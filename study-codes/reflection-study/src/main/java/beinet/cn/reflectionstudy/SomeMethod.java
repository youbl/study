package beinet.cn.reflectionstudy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SomeMethod {
//    public long LongMethod1() {
//        return 1;
//    }
//
//    public Long LongMethod2() {
//        return 1L;
//    }

    public List<Long> LongMethodArr1() {
        List<Long> ret = new ArrayList<>();
        ret.add(123L);
        ret.add(1234L);
        return ret;
    }

    public Long[] LongMethodArr2() {
        Long[] ret = new Long[2];
        ret[0] = 123L;
        ret[1] = 456L;
        return ret;
    }

    public long[] LongMethodArr3() {
        long[] ret = new long[2];
        ret[0] = 123L;
        ret[1] = 456L;
        return ret;
    }

    public List<Dto> DtoMethodArr1() {
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

    public Dto[] DtoMethodArr2() {
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
