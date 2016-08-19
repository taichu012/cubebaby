/**
 * 
 */
package cn.sw.android.base.tool;

import java.util.Random;

/**
 * @author zl
 *
 */
public class T {
	
	private static Random R = new Random();// 时间相关作为种子，不会每次重复！
	
	//随机生成[0,max)内的整数
	public static int getRadomInt(int max){
		return R.nextInt(max);
	}
	
	//随机产生以系统比分秒移位后的大整形int为上界和0之间的一个随机整数int
	public static int getRadomInt(){
		//long型最多左移6位，百分秒的不同而导致feed随机种子的不同！
		return R.nextInt(Integer.parseInt((System.currentTimeMillis()<<6)+""));
	}

	/**
	 * 
	 */
	public T() {
		// TODO Auto-generated constructor stub
	}
	
	public static void pln(String str){
		System.out.println(str);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[][] m = new int[4][4];
		m= new int[][] { { 0, 1, 0, 3 }, { 2, 3, 0, 1 },
				{ 3, 1, 0, 3 }, { 2, 1, 2, 2 } };
		
		pln("int[][].length="+m.length+"");
	}
	
	/**
	 * 
	 * @param matrix
	 * @return 处理后的matrix，随机洗牌num次，每次对调2对<x,y>的值
	 */
	public static int[][] SwapBlockByRandom(int[][] matrix, int swapTime) {
		
		int max = matrix.length;//基于matrix初始化的情况，比如int[x][y]，则max=x
		Random r = new Random();// 时间相关作为种子，不会每次重复！
		for (int i = 0; i < swapTime; i++) {
			// 随机生成4个值x1,y1,x2,y2,都是0到max的值，然后把<x1,y1>和<x2,y2>对调值！完成一次洗牌。

			// e、生成[0,max)区间的整数
			int x1 = r.nextInt(max); // 产生[0,max）的整数方法之一
			// r1 = Math.abs(r.nextInt()%max);//产生[0,max）的整数方法之一
			int x2 = r.nextInt(max);
			int y1 = r.nextInt(max);
			int y2 = r.nextInt(max);

			int mid = matrix[x1][y1];
			matrix[x1][y1] = matrix[x2][y2];
			matrix[x2][y2] = mid;
		}
		return matrix;
	}

}
