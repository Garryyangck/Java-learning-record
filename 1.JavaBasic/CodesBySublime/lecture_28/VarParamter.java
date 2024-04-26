public class VarParamter {
	public static void main(String[] args) {
		HspMethod obj = new HspMethod();
		System.out.println(obj.sum(1, 5, 100));
	}
}

class HspMethod{
	public int sum(int... nums){
		System.out.println("接收的参数个数=" + nums.length);
		int res = 0;
		for(int i = 0; i < nums.length; i++) {
			res += nums[i];
		}
		return res;
	}
}