public class ContinueTest {
	public static void main(String[] args) {
		label1:
		for(int i = 0; i < 10; i++) {
			label2:
			for(int j = 0; j < 10; j++) {
				if(j == 2){
					continue label1;
				}
				System.out.println("j = " + j);
			}
		}
	}
}