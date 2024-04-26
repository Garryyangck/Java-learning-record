public class BinaryTest{
	public static void main(String[] args){
		System.out.println("~-2 = " + ~-2);//1
		System.out.println("~2 = " + ~2);//-3

		int temp = -2 >> 1;
		System.out.println("-2>>1 = " + temp);//-1
		temp = -2 << 1;
		System.out.println("-2<<1 = " + temp);//-4
		temp = -4 >>> 1;
		System.out.println("-4>>>1 = " + temp);//2147483647

		temp = 2 >> 1;
		System.out.println("2>>1 = " + temp);//1
		temp = 2 << 1;
		System.out.println("2<<1 = " + temp);//4
		temp = 2 >>> 1;
		System.out.println("2>>>1 = " + temp);//1
	}
}