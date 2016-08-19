/**
 * 
 */
package cn.sw.android.base.entity;

/**
 * @author zl
 *
 */
public class Block {
	
	// FACE Number of Block； 块的面数（可能超过2个，不一定正反2面）
	// TODO 和面数相关的可能是：图片，种类，声音，状态，类别等等
	public final static int MAX_FACE4BLOCK = 16;//块最大面数
	public final int[] face; 


	// POSITION of Block； 块的行列位置
	private int row;
	private int col;
	
	//TODO 定义block状态集（状态变迁逻辑在逻辑层定义，不在这定义）
	
	//TODO 定义其他block需要内容
	

	/**
	 * 
	 */
	public Block() {
		//init instance
		face= initFace4Block(MAX_FACE4BLOCK);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private int[] initFace4Block(int faceNum ) {
		//初始化赋值！
		//TODO 替换下面的初始化过程，16个状态请考虑用“常量符号”代替int值，便于编码！
		int[] f4b= new int[faceNum];
		for (int i=1; i<=faceNum; i++){
			f4b[i]=i;
		}
		return f4b;
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	//数组初始化后不能改变值
	public int[] getFace() {
		return face;
	}
	
	
	

}
