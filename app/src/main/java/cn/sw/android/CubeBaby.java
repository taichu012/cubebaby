package cn.sw.android;

import java.lang.reflect.Field;
import java.util.Random;

import cn.sw.android.base.tool.T;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.R.color;
import android.view.View;
import android.view.View.OnClickListener;

public class CubeBaby extends Activity {

	// public Cube c =new Cube();
	private CubeMatrix cm = new CubeMatrix();
	private TableLayout tl = null;
	private int[] valueList = new int[4];
	private int swearCount = 0;
	private boolean canShowSwear=false;



	// Create an anonymous implementation of OnClickListenerprivate
	OnClickListener buttonListener = new OnClickListener() {
		public void onClick(View v) {
			// do something when the button is clicked

			// AlertDialog ad1 = getAlertDialog().create().ad1.show();

			ImageButton ib = (ImageButton) v;
			Cube cb = (Cube) ib.getTag();
			cm.ExchageRule1(cm, cb);

			// try {
			// this.wait(500);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// // e.printStackTrace();
			// }
			refreshAll(tl);
			checkGameFinished();
//			test(cm.matrix.length+"ttt");
			if (canShowSwear==true){
				getAlertDialog(getSwearWord()).show();
				canShowSwear=false;
			}
		}

		private void test(String str) {

			getAlertDialog(str).show();
			
		}
	};

	private int[] getPicFileVarientByCubeValue() {
		Resources res = getResources();
		int picFileVarientMax = Integer.parseInt(res.getString(R.string.picFileVarientMax));
		int picFileVarientMin = Integer.parseInt(res.getString(R.string.picFileVarientMin));
		int fileVarientCount = picFileVarientMax-picFileVarientMin + 1;
		int v1 = 0;
		int v2 = 0;
		int v3 = 0;
		int v4 = 0;
		// 随机生成[0,fileVarientCount)内的整数！
		v1 = T.getRadomInt(fileVarientCount) + picFileVarientMin;
		do {
			v2 = T.getRadomInt(fileVarientCount) + picFileVarientMin;
		} while (v2 == v1);
		do {
			v3 = T.getRadomInt(fileVarientCount) +picFileVarientMin;
		} while (v3 == v1 || v3 == v2);
		do {
			v4 = T.getRadomInt(fileVarientCount) + picFileVarientMin;
		} while (v4 == v1 || v4 == v2 || v4 == v3);

		// 因为matrix内部value总是从0,1,2,3,...所以，下面从候选图片varient号中随机的选出与value的一一对应！
		int[] valueList = new int[4];
		valueList[0] = v1;
		valueList[1] = v2;
		valueList[2] = v3;
		valueList[3] = v4;

		return valueList;
	}

	public void onConfigurationChanged(Configuration newConfig) {  
        super.onConfigurationChanged(newConfig);  
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {  
//                // land do nothing is ok  
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {  
//                // port do nothing is ok  
//        }  
}  
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

		// TextView tv = new TextView(this);
		// int i = 1;
		// String words="";
		// for (i = 1; i <= 50; i++) {
		// words = words + "helloWorld" + i+"\n";
		// }
		// tv.setText(words);
		// ImageView iv = new ImageView(this);
		// GridView gv = new GridView(this);

		TableLayout tl = new TableLayout(this);
		for (int row = 0; row < 4; row++) {
			TableRow tr = new TableRow(this);
			for (int col = 0; col < 4; col++) {
				ImageButton ib = new ImageButton(this);
				ib.setOnClickListener(buttonListener);
				ib.setImageResource(R.drawable.back);
				tr.addView(ib, col);
			}
			tl.addView(tr);
		}

		CubeMatrixBindtoTableLayout(cm, tl);
		tl.setClickable(true);
		tl.setBackgroundColor(getResources().getColor(color.white));

		setContentView(tl);
		this.tl = tl;
		Resources res = getResources();
		getAlertDialog(String.format(res.getString(R.string.ChangeLog))).show();
		valueList = getPicFileVarientByCubeValue();

	}

	private void CubeMatrixBindtoTableLayout(CubeMatrix cm, TableLayout tl) {
		// TODO Auto-generated method stub
		for (int row = 0; row < tl.getChildCount(); row++) {
			TableRow tr = (TableRow) tl.getChildAt(row);
			for (int col = 0; col < tr.getChildCount(); col++) {
				ImageButton ib = (ImageButton) tr.getChildAt(col);
				ib.setTag(cm.getCube(row, col));
			}
		}
	}

	private int getPicFilename(int cubeValue) {
		// 得到cube的value，再用反射拼接出资源id
		Resources res = getResources();
		String picFilePrefix = res.getString(R.string.picFilePrefix);
		R.drawable d = new R.drawable();
		Field valueIcon;
		try {
			valueIcon = d.getClass().getDeclaredField(
					picFilePrefix + valueList[cubeValue]);
			valueIcon.setAccessible(true);
			return valueIcon.getInt(d);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return -99;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return -99;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return -99;
		}
	}

	private void refreshAll(TableLayout tl) {
		// TODO Auto-generated method stub
		for (int i = 0; i < tl.getChildCount(); i++) {
			TableRow tr = (TableRow) tl.getChildAt(i);
			for (int j = 0; j < tr.getChildCount(); j++) {
				ImageButton ib = (ImageButton) tr.getChildAt(j);
				refreshCube(ib);
			}
		}
	}

	private void checkGameFinished() {
		if (0 == cm.checkAllCubeWorkStatus(cm, Cube.IS_DISABLED)) {
			getAlertDialog("You Win!").show(); 
			cm = new CubeMatrix();
			CubeMatrixBindtoTableLayout(cm, tl);
			refreshAll(tl);
			valueList=getPicFileVarientByCubeValue();
			swearCount++;
			checkSwear();
		}
		
	}

	private void refreshCube(ImageButton ib) {
		Cube cb = (Cube) ib.getTag();
		if (cb.getPosStatus() == Cube.ON_BACK) {
			ib.setImageResource(R.drawable.back);
		} else {
			ib.setImageResource(getPicFilename(cb.getValue()));
		}
	}

//	private EditText ISwear(){
//		 EditText et = new EditText(this);
//		 String words= "亲爱的ZL，我的囡囡，小企鹅，小猴猴，老婆大人！\n"+
//		"请允许我在此时此刻此地：\n\n"+
//		"向所有我尊敬的，信仰的，伟大的，不朽的精神起誓；\n\n"+
//		"向自然界所有亘古不变的真理起誓；\n\n"+
//		"向今天2009年11月29日之前不可变更的逝去，以及之后无法阻挡其到来的时间起誓；\n\n"+
//		"向我们的爱，我们偶尔的争执，我们的欢笑和眼泪起誓；\n\n"+
//		"向我一生唯一的挚爱，你，ZL起誓；\n\n"+
//		"我，CC，将用我一生，尽我所能，爱你，守护你！\n\n"+
//		"直到世界回到零的原点，我将依然信守这个誓言！\n\n"+
//		"你愿意嫁给我吗？\n\n"+
//		"永远爱你的CC，你的宝宝，北极熊，大猩猩，你的老公\n\n"+
//		"2009年11月29日\n\n"+
//		"\n\n\n还记得求婚那天的誓言吗？今天是2011年5月29日，终于在经过1个星期的熬夜，重新变身为当年的计算之神后，搞定了这个软件，希望我的囡囡，小企鹅，小猴猴和老婆大人能喜欢！\n\n"; 
//		 et.setText(words);
//		 return et;
//	}
	
//	private String getSwearWord(){
//		 String words= "亲爱的ZL，我的囡囡，小企鹅，小猴猴，老婆大人！\n"+
//		"请允许我在此时此刻此地：\n\n"+
//		"向所有我尊敬的，信仰的，伟大的，不朽的精神起誓；\n\n"+
//		"向自然界所有亘古不变的真理起誓；\n\n"+
//		"向今天2009年11月29日之前不可变更的逝去，以及之后无法阻挡其到来的时间起誓；\n\n"+
//		"向我们的爱，我们偶尔的争执，我们的欢笑和眼泪起誓；\n\n"+
//		"向我一生唯一的挚爱，你，ZL起誓；\n\n"+
//		"我，CC，将用我一生，尽我所能，爱你，守护你！\n\n"+
//		"直到世界回到零的原点，我将依然信守这个誓言！\n\n"+
//		"你愿意嫁给我吗？\n\n"+
//		"永远爱你的CC，你的宝宝，北极熊，大猩猩，你的老公\n\n"+
//		"2009年11月29日\n\n"+
//		"\n\n\n还记得求婚那天的誓言吗？今天是2011年5月29日，终于在经过1个星期的熬夜，重新变身为当年的计算之神后，搞定了这个软件，希望我的囡囡，小企鹅，小猴猴和老婆大人能喜欢！\n\n"; 
//		 return words;
//	}
	
	private String getSwearWord(){
		 String words= "测试！\n"+"测试：\n\n"+System.currentTimeMillis(); 
		 return words;
	}
	
//	private void checkSwear(){
//		if (swearCount>0) {
//			canShowSwear=true;
//			getSingleChoiceDlg(new CharSequence[]{"土豆","花生酱","菜泡饭","米饭"}, "NN最喜欢吃什么？", 2).create().show();
//			getSingleChoiceDlg(new CharSequence[]{"4月8日","10月12日","1月31日","12月12日"}, "NN的生日是哪天？", 4).create().show();
//			getSingleChoiceDlg(new CharSequence[]{"大猩猩","宝宝","北极熊","肉胖胖","小人人"}, "那个不是BB的昵称？", 3).create().show();
//			getSingleChoiceDlg(new CharSequence[]{"175cm","183cm","182cm","181cm","180cm","173cm","169cm"}, "BB的身高呢？", 4).create().show();
//			getSingleChoiceDlg(new CharSequence[]{"白斩鸡","菜饭","馄饨","猪肝","肚子","西瓜","蚕豆"}, "哪样东西BB不爱吃？", 3).create().show();
//			getSingleChoiceDlg(new CharSequence[]{"11月21日","11月30日","7月8日","3月21日","12月25日","1月1日","12月31日"}, "NN和BB相识是哪天？", 1).create().show();
//			getAlertDialog("恭喜你耐心的完成了这么多次游戏！如果你是我的NN，顺利答完下面的题目，就能看到BB对你说的话！").create().show();
//			swearCount=-9999; //确保下次不会被再调用！
//		}
//	}
	
	private void checkSwear(){
		if (swearCount>=0) {
			canShowSwear=true;
			getSingleChoiceDlg(new CharSequence[]{"土豆","花生酱","菜泡饭","米饭"}, "你最喜欢吃什么？", 2).create().show();
			getAlertDialog("恭喜你耐心的完成了这么多次游戏！哈哈！").create().show();
			swearCount=-9999; //确保下次不会被再调用！
		}
	}
	
	public AlertDialog.Builder getSingleChoiceDlg(final CharSequence[] items, String title, final int answer) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setTitle(title); 
		builder.setInverseBackgroundForced(true);
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() { 
		    public void onClick(DialogInterface dialog, int item) { 
		    	if (answer!=item){canShowSwear=false;}
		        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		        dialog.dismiss();
		    };
		});
		return builder;
	}
	
	public AlertDialog.Builder getAlertDialog(String words) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setInverseBackgroundForced(true);
		builder.setMessage(words).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return builder;
	}
	
//	private void waitMe(long millis){
//	 try {
//	 this.wait(millis);
//	 } catch (InterruptedException e) {
//	 // TODO Auto-generated catch block
//	 // e.printStackTrace();
//	 }
//	}
}