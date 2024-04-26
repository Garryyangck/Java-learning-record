public class MethodParameter {
	public static void main(String[] args) {
		B b = new B;
		Person p = new Person();
		p.name = "jack";
		p.age = 10;
		b.test200(p);
		//测试题, 如果 test200 执行的是 p = null ,下面的结果是 10
		//测试题, 如果 test200 执行的是 p = new Person();..., 下面输出的是 10
		System.out.println("main 的 p.age=" + p.age);//10
	}
}