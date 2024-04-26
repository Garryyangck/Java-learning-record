public class LogicOperator{
	public static void main(String[] args){
		int a = 4;
		int b = 9;
		//对于&&短路与而言，如果第一个条件为 false ,后面的条件不再判断
		if(a < 1 && ++b < 50) {
			System.out.println("ok300");
		}
		System.out.println("a=" + a + " b=" + b);// 4 9

		//对于&逻辑与而言，如果第一个条件为 false ,后面的条件仍然会判断
		if(a < 1 & ++b < 50) {
			System.out.println("ok300");
		}
		System.out.println("a=" + a + " b=" + b);// 4 10
	}
}