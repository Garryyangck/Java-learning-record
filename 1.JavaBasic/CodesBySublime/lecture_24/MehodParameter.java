public class MethodParameter {
	public static void main(String[] args) {
		B b = new B;
		Person p = new Person();
		p.name = "jack";
		p.age = 10;
		b.test200(p);
		//������, ��� test200 ִ�е��� p = null ,����Ľ���� 10
		//������, ��� test200 ִ�е��� p = new Person();..., ����������� 10
		System.out.println("main �� p.age=" + p.age);//10
	}
}