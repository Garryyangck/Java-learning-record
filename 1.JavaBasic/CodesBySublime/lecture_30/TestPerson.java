public class TestPerson {
	public static void main(String[] args) {
		Person p1 = new Person("mary", 20);
		Person p2 = new Person("mary", 20);
		System.out.println("p1 和 p2 比较的结果=" + p1.compareTo(p2));
	}
}

class Person {
	String name;
	int age;
	//构造器
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
		System.out.println("构造器\tname: " + this.name + "\tage: " + this.name);
	}
	//compareTo 比较方法
	public boolean compareTo(Person p) {
		return this.name.equals(p.name) && this.age == p.age;
	}
}