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
		//System.out.println("T() �Ĺ�����");
		this("����", 33);//���Ҫ�ڹ������е��������Ĺ�������������ڵ�һ�����
	}
	public T(String name, int age) {
		System.out.println("T(String name, int age) �Ĺ�����");
		this.name = name;
		this.age = age;
	}

	public void func1() {
		System.out.println("����func1()");
	}
	public void func2() {
		System.out.println("����func2()");
		//��һ�ַ���
		func1();
		//�ڶ��ַ���
		this.func1();
	}
}