package com.use;

import com.xiaoqiang.Dog;
//import com.xiaoming.Dog;
// 'com.xiaoqiang.Dog' is already defined in a single-type import
// 只能导入一个包的.Dog，作为此类的默认Dog，若要使用其它包的.Dog，需要使用com.xiaoming.Dog

public class Test {
    public static void main(String[] args) {
        Dog dogQiang = new Dog();
        System.out.println(dogQiang);//com.xiaoqiang.Dog@1540e19d，后面的是HashCode
        com.xiaoming.Dog dogMing = new com.xiaoming.Dog();
        System.out.println(dogMing);//com.xiaoming.Dog@677327b6
    }
}
