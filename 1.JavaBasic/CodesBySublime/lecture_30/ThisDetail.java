public class ThisDetail {
	public static void main(String[] args) {
		T t1 = new T();
		t1.func2();
	}
}

class T{
	public String name;
	public int age;

	public T() {
		//System.out.println("T() 的构造器");
		this("张三", 33);//如果要在构造器中调用其它的构造器，必须放在第一条语句
	}
	public T(String name, int age) {
		System.out.println("T(String name, int age) 的构造器");
		this.name = name;
		this.age = age;
	}

	public void func1() {
		System.out.println("调用func1()");
	}
	public void func2() {
		System.out.println("调用func2()");
		//第一种方法
		func1();
		//第二种方法
		this.func1();
	}
}