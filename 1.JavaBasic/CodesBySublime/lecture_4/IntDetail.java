public class IntDetail{
	public static void main(String[] args){
		int n1 = 1;//4个字节
		//int n2 = 1L;//int只分配4字节的空间，而1L有8字节的空间，因此编译器会报错
		long n2 = 1L;//8个字节
	}
}