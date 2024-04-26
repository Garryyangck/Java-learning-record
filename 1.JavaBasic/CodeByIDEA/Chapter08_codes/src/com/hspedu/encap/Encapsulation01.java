package com.hspedu.encap;

public class Encapsulation01 {
    public static void main(String[] args) {
        Person zhangSan = new Person("张三", 33, 3333);
        System.out.println(zhangSan.info());
    }
}

class Person {
    public  String name; //名字公开
    private int age; //age 私有化
    private double salary; //..

    public void say(int n,String name) {

    }
    //构造器 alt+insert
    public Person() {
    }
    //有三个属性的构造器
    public Person(String name, int age, double salary) {
//        this.name = name;
//        this.age = age;
//        this.salary = salary;
        //我们可以将set方法写在构造器中，这样仍然可以验证
        setName(name);
        setAge(age);
        setSalary(salary);
    }

    //自己写setXxx 和 getXxx 太慢，我们使用快捷键
    //然后根据要求来完善我们的代码.
    public String getName() {
        return name;
    }
    public void setName(String name) {
        //加入对数据的校验,相当于增加了业务逻辑
        if(name.length() >= 2 && name.length() <=6 ) {
            this.name = name;
        }else {
            System.out.println("名字的长度不对，需要(2-6)个字符，默认名字");
            this.name = "无名人";
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        //判断
        if(age >= 1 && age <= 120) {//如果是合理范围
            this.age = age;
        } else {
            System.out.println("你设置年龄不对，需要在 (1-120), 给默认年龄18 ");
            this.age = 18;//给一个默认年龄
        }
    }

    public double getSalary() {
        //可以这里增加对当前对象的权限判断
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    //写一个方法，返回属性信息
    public String info() {
        return "信息为 name=" + name  + " age=" + age + " 薪水=" + salary;
    }
}