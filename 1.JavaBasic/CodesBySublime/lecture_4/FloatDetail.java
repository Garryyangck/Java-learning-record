public class FloatDetail{
	public static void main(String[] args){
		/*
			1. ��������Ĭ�����ͣ��Լ���ʽת��
		 */
		//float num1 = 1.1;		//1.1Ĭ��Ϊdouble��ռ8���ֽڣ���floatֻռ4���ֽڣ��ᱨ��
		float num1 = 1.1F;
		double num2 = 1.1;
		double num3 = 1.1F;//���У�С�Ŀ�����ʽתΪ�������

		/*
			2. ��������ʮ���Ʊ�ʾ�����Ϳ�ѧ������
		 */
		//ʮ���Ʊ�ʾ��
		double num4 = .123;//0.123�������ʡ�ԣ�С���㲻��ʡ��
		//��ѧ������
		double num5 = 5.12e2;//5.12 * 10 ^ 2��512.0
		double num6 = 5.12e-2;//5.12 * 10 ^ (-2)��0.0512
		System.out.println("num5 = " + num5);//num5 = 512.0
		System.out.println("num6 = " + num6);//num6 = 0.0512

		/*
			3. ͨ�������ѡ��double�����ȸ���
		 */
		float  num7 = 2.123456789f;
		double num8 = 2.123456789;
		System.out.println("num7 = " + num7);//num7 = 2.1234567����ʧ����
		System.out.println("num8 = " + num8);//num8 = 2.123456789

		/*
			4. ��������ʹ�����壺2.7 �� 8.1 / 3 �Ƚ�
		 */
		double num9 = 2.7;
		double num10 = 8.1 / 3;
		System.out.println("num9 = " + num9);//num9 = 2.7
		System.out.println("num10 = " + num10);//num10 = 2.6999999999999997
		//WARNING: ������֮���С�����бȽ�ʱ�ǳ�Σ�յģ���Ϊ������е�С��ֻ��һ������ֵ�����о��ȵ�
		//�жϷ�ʽӦ��Ϊ����������ֵ�ľ���ֵ��ĳ�����ȷ�Χ֮��
		System.out.println(num10 == num9);//false
		System.out.println(Math.abs(num10 - num9));//4.440892098500626E-16
		System.out.println(Math.abs(num10 - num9) < 0.000001);//true
		//ϸ��:�����ֱ�Ӳ�ѯ�õĵ�С������ֱ�Ӹ�ֵ���ǿ����ж����
	}
}