public class ArrayCopy {
	public static void main(String[] args) {
		int[] arr1 = {10,20,30}; 
		int[] arr2 = arr1;//ǳ������ֱ�Ӱ�arr1�ĵ�ַ����arr2
		for(int i = 0; i < arr2.length; i++) {
			arr2[i] += 100;
		}
		System.out.println("====arr1Ԫ��====");
		for(int i = 0; i < arr1.length; i++) {
			System.out.println(arr1[i]);//110��120��130
		}

	}
}