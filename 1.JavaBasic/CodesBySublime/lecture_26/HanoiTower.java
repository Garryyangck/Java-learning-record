import java.util.*;

public class HanoiTower {
	public static void main(String[] args) {
		char a = '1';
		char b = '2';
		char c = '3';
		int n = 0;
		System.out.print("请输入第一根汉诺塔的柱子数：");
		Scanner scan = new Scanner(System.in);
		n = scan.nextInt();
		Tower tower = new Tower();
		System.out.println("移动" + n + "根柱子的汉诺塔需要" + tower.move(n, a, b, c) + "步");
	}
}

class Tower{
	//放回借助b塔，将a塔所有柱子移到c塔的步骤
	public int move(int num, char a, char b, char c){
		if(num == 1){
			System.out.println(a + "->" + c);
			return 1;
		}
		//将a塔上的num根柱子看作是上面num-1根和底部的一根
		//先将a上的num-1根移到b
		int num1 = move(num - 1, a, c, b);
		//再把底部的移动到c
		int num2 = move(1, a, b, c);
		//最后把b的移动到c
		int num3 = move(num - 1, b, a, c);
		return num1 + num2 + num3;
	}
}