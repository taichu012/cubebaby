package cn.sw.android;

import java.util.*;

import cn.sw.android.base.tool.T;

public class CubeMatrix {

	// 最大行列设定
	public final static int ROW_MAX = 7;
	public final static int COL_MAX = 8;
	// 默认4行4列
	public final static int ROW_DEFAULT = 4;
	public final static int COL_DEFAULT = 4;

	public Cube[][] matrix = new Cube[ROW_MAX][COL_MAX];
	private Cube lastVisitedCube = null;

	public final static int rowNum = ROW_DEFAULT;
	public final static int colNum = COL_DEFAULT;

	// 最大CUBE值
	public final static int VALUE_MAX = 3;

	// value 产生模式
	public final static int VALUE_GENERATED_BY_RANDOM = 8;
	public final static int VALUE_GENERATED_BY_FIXED = 16;

	// private int valueGenMode;

	public Cube getCube(int row, int col) {
		return matrix[row][col];
	}

	public void setCubeStatus(int row, int col, Cube c) {
		matrix[row][col] = c;
	}

	public CubeMatrix(int rowNum, int colNum, int valueGenMode) {
		for (int row = 0; row < rowNum; row++) {
			for (int col = 0; col < colNum; col++) {
				matrix[row][col] = new Cube();
			}
		}
		// this.valueGenMode=valueGenMode;
		this.genValue(valueGenMode);
		this.MappingValue2Matrix(rowNum, colNum, matrix, value);
	}

	public CubeMatrix() {
		this(ROW_DEFAULT, COL_DEFAULT, VALUE_GENERATED_BY_RANDOM);
	}

	private int[][] value = new int[ROW_MAX][COL_MAX];

	private void MappingValue2Matrix(int rowNum, int colNum, Cube[][] matrix,
			int[][] value) {

		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				matrix[i][j].setValue(value[i][j]);
			}
		}
	};

	private void genValue(int valueGenMode) {
		if (valueGenMode == CubeMatrix.VALUE_GENERATED_BY_RANDOM) {
			// generate random value matrix
			// 算法：固定初态+随机洗牌=随机终态
			// generate fixed value matrix
			this.value = new int[][] { { 0, 1, 0, 3 }, { 2, 3, 0, 1 },
					{ 3, 1, 0, 3 }, { 2, 1, 2, 2 } };
			//this.value = this.random2Matrix(value);
			this.value=T.SwapBlockByRandom(value,4);
		} else if (valueGenMode == CubeMatrix.VALUE_GENERATED_BY_FIXED) {
			// generate fixed value matrix
			this.value = new int[][] { { 0, 1, 0, 3 }, { 2, 3, 0, 1 },
					{ 3, 1, 0, 3 }, { 2, 1, 2, 2 } };
		} else {
			// same = VALUE_GENERATED_BY_FIXED
			this.value = new int[][] { { 0, 1, 0, 3 }, { 2, 3, 0, 1 },
					{ 3, 1, 0, 3 }, { 2, 1, 2, 2 } };
		}
	}

	private int[][] random2Matrix(int[][] matrix) {
		// 随机32次洗牌
		int exchangeNum = 32;
		Random r = new Random();// 时间相关作为种子，不会每次重复！
		for (int i = 0; i < exchangeNum; i++) {
			// 随机生成4个值r1,r2,c1,c2,都是0到3的值，然后把<r1,c1>和<r2,c2>对调值！完成一次洗牌。

			// e、生成[0,4)区间的整数
			int r1 = r.nextInt(4); // 产生[0,4）的整数方法之一
			// r1 = Math.abs(r.nextInt()%4);//产生[0,4）的整数方法之一
			int r2 = r.nextInt(4);
			int c1 = r.nextInt(4);
			int c2 = r.nextInt(4);

			int mid = matrix[r1][c1];
			matrix[r1][c1] = matrix[r2][c2];
			matrix[r2][c2] = mid;
		}
		return matrix;
	}

	public CubeMatrix ExchageRule1(CubeMatrix m, Cube c) {

		if (c.getWorkStatus() == Cube.IS_DISABLED) {
			Cube cb = this.lastVisitedCube;
			if (cb.getWorkStatus() == Cube.IS_ENABLED) {
				cb.setPosStatus(Cube.ON_BACK);
				m.setCubeStatus(cb.getRow(), cb.getCol(), cb);
			}
			this.lastVisitedCube = c;
			// no change an return matirx!
			return m;
		}
		if (c.getPosStatus() == Cube.ON_FRONT) {
			// just front to back! update matrix and return!
			c.setPosStatus(Cube.ON_BACK);
			this.lastVisitedCube = c;
			m.setCubeStatus(c.getRow(), c.getCol(), c);
			return m;
		}
		// init status of c = enable && back, then we try to find LAST VISITED
		// CUBE!
		if (this.lastVisitedCube == null) {
			// 第一次点击cube，找不到匹配对！
			c.setPosStatus(Cube.ON_FRONT);
			this.lastVisitedCube = c;
			m.setCubeStatus(c.getRow(), c.getCol(), c);
			return m;
		} else {
			// 如果有最后访问的cube，则和本次cube比对！
			if (this.lastVisitedCube.getWorkStatus() == Cube.IS_DISABLED
					|| this.lastVisitedCube.getPosStatus() == Cube.ON_BACK) {
				c.setPosStatus(Cube.ON_FRONT);
				this.lastVisitedCube = c;
				m.setCubeStatus(c.getRow(), c.getCol(), c);
				return m;
			} else {
				if (this.lastVisitedCube.getValue() == c.getValue()) {
					// FIND PAIR!!!!!!!!!!!!! update 2 cubes of pair into matirx
					// and return!
					this.lastVisitedCube.setWorkStatus(Cube.IS_DISABLED);
					m.setCubeStatus(this.lastVisitedCube.getRow(),
							this.lastVisitedCube.getCol(), this.lastVisitedCube);
					c.setWorkStatus(Cube.IS_DISABLED);
					c.setPosStatus(Cube.ON_FRONT);
					this.lastVisitedCube = c; // 替换c到当前lastvisitedcube，以备下次找匹配用
					m.setCubeStatus(c.getRow(), c.getCol(), c);
					return m;
				} else {
					this.lastVisitedCube.setPosStatus(Cube.ON_BACK);
					m.setCubeStatus(this.lastVisitedCube.getRow(),
							this.lastVisitedCube.getCol(), this.lastVisitedCube);
					c.setPosStatus(Cube.ON_FRONT);
					this.lastVisitedCube = c;
					m.setCubeStatus(c.getRow(), c.getCol(), c);
					return m;
				}
			}
		}
	}

	public int checkAllCubeWorkStatus(CubeMatrix cm, int cubeWorkStatus) {
		// TODO Auto-generated method stub
		for (int row = 0; row < cm.rowNum; row++) {
			for (int col = 0; col < cm.colNum; col++) {
				if (cm.getCube(row, col).getWorkStatus() != cubeWorkStatus) {
					return -1; // at least on cube is enabled and not finished!
								// just return -1!
				}
			}
		}
		return 0; // all cubes are disabled, game finished with successfully!
					// return 0!
	}

	// private static CubeStatusMatrix instance = null;
	//
	//
	//
	// //单件获取静态实例-cube状态矩阵
	// public static CubeStatusMatrix getInstance() {
	// if (instance == null) {
	// instance = new CubeStatusMatrix();
	// };
	// return instance;
	// }
}
