import java.util.Scanner;//������jave.util	������Scanner	���������淶

public class Input{
	public static void main(String[] args){
		//Scanner �� ��ʾ ���ı�ɨ�������� java.util ��
		//1. ����/���� Scanner �����ڵİ�
		//2. ���� Scanner ���� , new ����һ������,���
		// myScanner ���� Scanner ��Ķ���
		Scanner myScanner = new Scanner(System.in);//inΪSystem����ֶΣ���ʾ������
		//3. �����û������ˣ� ʹ�� ��صķ���
		System.out.println("����������");
		String name = myScanner.next(); //�����û������ַ���
		System.out.println("����������");
		int age = myScanner.nextInt(); //�����û����� int
		System.out.println("������нˮ");
		double sal = myScanner.nextDouble(); //�����û����� double
		System.out.println("�˵���Ϣ����:");
		System.out.println("����=" + name + " ����=" + age + " нˮ=" + sal);
	}
}