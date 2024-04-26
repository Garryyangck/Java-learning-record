public class autoConvert{
	public static void main(String[] args){
		int num = 'a';//char自动转int
		double d1 = 80;//int自动转double

		/*
			1. 多种数据混合运算时，自动转成容量最大的数据类型，再进行计算
		 */
		float f1 = num + 1.1f + (short)10;
		//int n2 = 1.1;//报错
		
		/*
			2. 大精度不能自动转换为小精度的
		 */
		byte b1 = 10;//可以
		//byte b2 = num;//不行，int 4字节

		/*
			3. char，byte，short间不存在自动类型转换
		 */
		// char c1 = b1;//报错，byte不能自动转换为char
		 
		/*
			4. char，byte，short不管是单独还是相互运算，都要先转换为int型
		 */
		byte b2 = 1;
		byte b3 = 2;
		short s1 = 1;
		//short s2 = b2 + s1;//错, b2 + s1 => int
		int s2 = b2 + s1;//对, b2 + s1 => int
		//byte b4 = b2 + b3; //错误: b2 + b3 => int **重点**
		
		/*
			5. 自动提升原则
		 */
		byte b4 = 1;
		short s3 = 100;
		int num200 = 1;
		float num300 = 1.1F;
		double num500 = b4 + s3 + num200 + num300; //float -> double
	}
}