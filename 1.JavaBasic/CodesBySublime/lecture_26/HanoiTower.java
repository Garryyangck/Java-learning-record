import java.util.*;

public class HanoiTower {
	public static void main(String[] args) {
		char a = '1';
		char b = '2';
		char c = '3';
		int n = 0;
		System.out.print("�������һ����ŵ������������");
		Scanner scan = new Scanner(System.in);
		n = scan.nextInt();
		Tower tower = new Tower();
		System.out.println("�ƶ�" + n + "�����ӵĺ�ŵ����Ҫ" + tower.move(n, a, b, c) + "��");
	}
}

class Tower{
	//�Żؽ���b������a�����������Ƶ�c���Ĳ���
	public int move(int num, char a, char b, char c){
		if(num == 1){
			System.out.println(a + "->" + c);
			return 1;
		}
		//��a���ϵ�num�����ӿ���������num-1���͵ײ���һ��
		//�Ƚ�a�ϵ�num-1���Ƶ�b
		int num1 = move(num - 1, a, c, b);
		//�ٰѵײ����ƶ���c
		int num2 = move(1, a, b, c);
		//����b���ƶ���c
		int num3 = move(num - 1, b, a, c);
		return num1 + num2 + num3;
	}
}