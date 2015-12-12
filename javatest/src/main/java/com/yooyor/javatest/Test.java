package com.yooyor.javatest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuefengyang on 2015/11/22.
 */
public class Test {

    public static void main(String [] args){
        Photo p1 =new Photo();
        p1.photo="p1";

        Photo p2 =new Photo();
        p1.photo="p2";

        Photo p3 =new Photo();
        p1.photo="p3";

        List<Photo> list=new ArrayList<Photo>();
                    list.add(p1);
                    list.add(p2);
                    list.add(p3);

        Map<Photo,String> map=new HashMap<>();
                        map.put(p1,"pp1");
                        map.put(p2,"pp2");
                        map.put(p3,"pp3");

        for (int i = 0; i < list.size(); i++) {
          System.out.println(map.get(list.get(i)));
        }
    }
}
