public class SwitchTest {
	public static void main(String[] args) {
		int num = 1;
		switch(num){
			case 0:
				System.out.println("OK0");//未输出
			case 1:
				System.out.println("OK1");//输出
			case 2:
				System.out.println("OK2");//输出
			case 3:
				System.out.println("OK3");//输出
			default:
				System.out.println("OK666");//输出
		}
	}
}