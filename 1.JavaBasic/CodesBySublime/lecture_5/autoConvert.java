public class autoConvert{
	public static void main(String[] args){
		int num = 'a';//char�Զ�תint
		double d1 = 80;//int�Զ�תdouble

		/*
			1. �������ݻ������ʱ���Զ�ת�����������������ͣ��ٽ��м���
		 */
		float f1 = num + 1.1f + (short)10;
		//int n2 = 1.1;//����
		
		/*
			2. �󾫶Ȳ����Զ�ת��ΪС���ȵ�
		 */
		byte b1 = 10;//����
		//byte b2 = num;//���У�int 4�ֽ�

		/*
			3. char��byte��short�䲻�����Զ�����ת��
		 */
		// char c1 = b1;//����byte�����Զ�ת��Ϊchar
		 
		/*
			4. char��byte��short�����ǵ��������໥���㣬��Ҫ��ת��Ϊint��
		 */
		byte b2 = 1;
		byte b3 = 2;
		short s1 = 1;
		//short s2 = b2 + s1;//��, b2 + s1 => int
		int s2 = b2 + s1;//��, b2 + s1 => int
		//byte b4 = b2 + b3; //����: b2 + b3 => int **�ص�**
		
		/*
			5. �Զ�����ԭ��
		 */
		byte b4 = 1;
		short s3 = 100;
		int num200 = 1;
		float num300 = 1.1F;
		double num500 = b4 + s3 + num200 + num300; //float -> double
	}
}