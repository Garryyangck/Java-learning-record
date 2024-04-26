public class ArrayCopy {
	public static void main(String[] args) {
		int[] arr1 = {10,20,30}; 
		int[] arr2 = arr1;//浅拷贝，直接把arr1的地址传给arr2
		for(int i = 0; i < arr2.length; i++) {
			arr2[i] += 100;
		}
		System.out.println("====arr1元素====");
		for(int i = 0; i < arr1.length; i++) {
			System.out.println(arr1[i]);//110，120，130
		}

	}
}