package com.example.peiandsky;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Card {
	int value=0;
	int pokeType=0;
	int[] pokes;
	Bitmap pokeImage;
	int personID;
	public Card(int[] pokes,Bitmap pokeImage,int id)
	{
		this.personID=id;
		this.pokes=pokes;
		this.pokeImage=pokeImage;
		pokeType=Poke.getPokeType(pokes);
		value=Poke.getPokeTypeValue(pokes, pokeType);
		//��ʾ����ȷ����
//		�����ը���Ƴ��֣���������
		if(pokeType==PokeType.huojian||pokeType==PokeType.zhadan)
		{
			Desk.currentScore*=2;
		}
	}
	
	public void paint(Canvas canvas,int left,int top,int dir)
	{
		Rect src = new Rect();
		Rect des = new Rect();
		for (int i = 0; i < pokes.length; i++) {
			int row = Poke.getImageRow(pokes[i]);
			int col = Poke.getImageCol(pokes[i]);
			if (dir == PokeType.dirV) {
				row = Poke.getImageRow(pokes[i]);
				col = Poke.getImageCol(pokes[i]);
				src.set(col * 64, row * 93, col * 64 + 64, row * 93 + 93);
				des.set(left, top + i * 26, left + 64, top + 93 + i * 26);
			} else {
				row = Poke.getImageRow(pokes[i]);
				col = Poke.getImageCol(pokes[i]);
				int select = 0;
				src.set(col * 64, row * 93, col * 64 + 64, row * 93 + 93);
				des.set(left + i * 26, top - select, left + 64 + i * 26, top
						- select + 93);
			}
			canvas.drawBitmap(pokeImage, src, des, null);
		}
	}
}
