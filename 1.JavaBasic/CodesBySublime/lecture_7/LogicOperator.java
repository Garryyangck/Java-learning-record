public class LogicOperator{
	public static void main(String[] args){
		int a = 4;
		int b = 9;
		//����&&��·����ԣ������һ������Ϊ false ,��������������ж�
		if(a < 1 && ++b < 50) {
			System.out.println("ok300");
		}
		System.out.println("a=" + a + " b=" + b);// 4 9

		//����&�߼�����ԣ������һ������Ϊ false ,�����������Ȼ���ж�
		if(a < 1 & ++b < 50) {
			System.out.println("ok300");
		}
		System.out.println("a=" + a + " b=" + b);// 4 10
	}
}