public class FloatDetail{
	public static void main(String[] args){
		/*
			1. 浮点数的默认类型，以及隐式转换
		 */
		//float num1 = 1.1;		//1.1默认为double，占8个字节，而float只占4个字节，会报错
		float num1 = 1.1F;
		double num2 = 1.1;
		double num3 = 1.1F;//可行，小的可以隐式转为大的类型

		/*
			2. 浮点数的十进制表示法，和科学计数法
		 */
		//十进制表示法
		double num4 = .123;//0.123，零可以省略，小数点不可省略
		//科学计数法
		double num5 = 5.12e2;//5.12 * 10 ^ 2，512.0
		double num6 = 5.12e-2;//5.12 * 10 ^ (-2)，0.0512
		System.out.println("num5 = " + num5);//num5 = 512.0
		System.out.println("num6 = " + num6);//num6 = 0.0512

		/*
			3. 通常情况下选择double，精度更高
		 */
		float  num7 = 2.123456789f;
		double num8 = 2.123456789;
		System.out.println("num7 = " + num7);//num7 = 2.1234567，损失精度
		System.out.println("num8 = " + num8);//num8 = 2.123456789

		/*
			4. 浮点数的使用陷阱：2.7 和 8.1 / 3 比较
		 */
		double num9 = 2.7;
		double num10 = 8.1 / 3;
		System.out.println("num9 = " + num9);//num9 = 2.7
		System.out.println("num10 = " + num10);//num10 = 2.6999999999999997
		//WARNING: 对运算之后的小数进行比较时非常危险的，因为计算机中的小数只是一个近似值，是有精度的
		//判断方式应该为：两个数差值的绝对值在某个精度范围之内
		System.out.println(num10 == num9);//false
		System.out.println(Math.abs(num10 - num9));//4.440892098500626E-16
		System.out.println(Math.abs(num10 - num9) < 0.000001);//true
		//细节:如果是直接查询得的的小数或者直接赋值，是可以判断相等
	}
}