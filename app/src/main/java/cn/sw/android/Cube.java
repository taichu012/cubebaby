package cn.sw.android;

public class Cube {

	// 每个cube所处与正面或反面状态
	public final static int ON_FRONT = 1;
	public final static int ON_BACK = 2;

	// 每个cube所处于完成或者未完成状态
	public final static int IS_DISABLED = 8;
	public final static int IS_ENABLED = 16;

	// 状态变量
	private int posStatus;// 正反面2态
	private int workStatus;// 可否操作（比如：点击）2态
	private int value; // cube值（比如：1,2,3,4）

	// 位置变量
	private int row;
	private int col;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	// public void setRow(int row) {
	// this.row = row;
	// }

	public int getCol() {
		return col;
	}

	//
	// public void setCol(int col) {
	// this.col = col;
	// }

	public Cube(int posStatus, int workStatus, int row, int col) {
		super();
		this.setPosStatus(posStatus);
		this.setWorkStatus(workStatus);
		this.row = row;
		this.col = col;
	}

	public Cube() {
		this(ON_BACK, IS_ENABLED, 0, 0);
	}

	public void setPosStatus(int posStatus) {
		this.posStatus = posStatus;
	}

	public int getPosStatus() {
		return posStatus;
	}

	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}

	public int getWorkStatus() {
		return workStatus;
	}

}
