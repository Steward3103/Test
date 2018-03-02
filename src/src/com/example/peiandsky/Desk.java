package com.example.peiandsky;

import com.example.peiandsky.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;

public class Desk {
	public static int winId = -1;
	final public int SIZE_CLOCK = 16;
	Bitmap pokeImage;
	Bitmap buyao;
	Bitmap chupai;
	Bitmap clock;
	Bitmap end;

	
	public static int[] personScore = new int[3];

	public static int threePokes[] = new int[3];// ���ŵ���
	private int threePokesPos[][] = new int[][] { { 283, 25 }, { 367, 25 },
			{ 450, 25 } };
	private int[][] rolePos = { { 91, 464 }, { 97, 35 }, { 645, 35 }, };

	public static Person[] persons = new Person[3];// �������
	public static int[] deskPokes = new int[54];// һ���˿���
	public static int currentScore = 3;// ��ǰ����
	public static int boss = 0;// ����
	/**
	 * -2:����<br>
	 * -1:�������<br>
	 * 0:��Ϸ�� <br>
	 * 1:��Ϸ�����������������˳�<br>
	 */
	private int op = -1;// ��Ϸ�Ľ��ȿ���
	public static int currentPerson = 0;// ��ǰ��������
	public static int currentCircle = 0;// ���ִ���
	public static Card currentCard = null;// ���µ�һ����

	public int[][] personPokes = new int[3][17];

	// gaming
	private int timeLimite = 400;
	private int[][] timeLimitePos = { { 217, 307 }, { 196, 114 }, { 545, 114 } };
	private int opPosX = 400;
	private int opPosY = 260;

	DDZ ddz;

	public Desk(DDZ ddz) {
		this.ddz = ddz;
		pokeImage = BitmapFactory.decodeResource(ddz.getResources(),
				R.drawable.poker6493);
		buyao = BitmapFactory
				.decodeResource(ddz.getResources(), R.drawable.cp1);
		chupai = BitmapFactory.decodeResource(ddz.getResources(),
				R.drawable.cp2);
		clock = BitmapFactory.decodeResource(ddz.getResources(),
				R.drawable.clock);
		end = BitmapFactory.decodeResource(ddz.getResources(),
				R.drawable.game_over);
		// init();
	}

	public void gameLogic() {
		switch (op) {
		case -2:
			break;
		case -1:
			init();
			op = 0;
			break;
		case 0:
			gaming();
			break;
		case 1:
			break;
		case 2:
			break;
		}
	}

	// �洢��ǰһ���ʤ���÷���Ϣ
	int rs[] = new int[3];

	private void gaming() {
		for (int k = 0; k < 3; k++) {
			// ��������������һ�����Ƶ�����Ϊ0������Ϸ����
			if (persons[k].pokes.length == 0) {
				// �л�����Ϸ����״̬
				op = 1;
				// �õ����ȳ�ȥ���˵�id
				winId = k;
				// �ж��ķ���ʤ
				if (boss == winId) {
					// ��������ʤ��Ļ��ֲ���
					for (int i = 0; i < 3; i++) {
						if (i == boss) {
							// ������Ҫ����������
							rs[i] = currentScore * 2;
							personScore[i] += currentScore * 2;
						} else {
							// ũ����Ҫ����
							rs[i] = -currentScore;
							personScore[i] -= currentScore;
						}
					}
				} else {
					// ���ũ��ʤ��
					for (int i = 0; i < 3; i++) {
						if (i != boss) {
							// ũ�񷽼ӷ�
							rs[i] = currentScore;
							personScore[i] += currentScore;
						} else {
							// ����������
							rs[i] = -currentScore * 2;
							personScore[i] -= currentScore * 2;
						}
					}
				}
				return;
			}
		}

		// ��Ϸû�н�����������
		// �������ID��NPC����ִ������еĲ���
		if (currentPerson == 1 || currentPerson == 2) {
			if (timeLimite <= 200) {
				// ��ȡ���е������ܹ������ǰ����
				Card tempcard = persons[currentPerson].chupaiAI(currentCard);
				if (tempcard != null) {
					// �����д�����ƣ����
					currentCircle++;
					currentCard = tempcard;
					nextPerson();
				} else {
					// û�д�����ƣ���Ҫ
					buyao();
				}
			}

		}
		// ʱ�䵹��ʱ
		timeLimite -= 2;

	}

	public void init() {
		deskPokes = new int[54];
		personPokes = new int[3][17];
		threePokes = new int[3];

		winId = -1;
		currentScore = 3;
		currentCard = null;
		currentCircle = 0;
		currentPerson = 0;

		for (int i = 0; i < deskPokes.length; i++) {
			deskPokes[i] = i;
		}
		Poke.shuffle(deskPokes);
		fenpai(deskPokes);
		randDZ();
		Poke.sort(personPokes[0]);
		Poke.sort(personPokes[1]);
		Poke.sort(personPokes[2]);
		persons[0] = new Person(personPokes[0], 337, 135, PokeType.dirH, 0,
				this, ddz);
		persons[1] = new Person(personPokes[1], 60, 30, PokeType.dirV, 1, this,
				ddz);
		persons[2] = new Person(personPokes[2], 60, 710, PokeType.dirV, 2,
				this, ddz);
		persons[0].setPosition(persons[1], persons[2]);
		persons[1].setPosition(persons[2], persons[0]);
		persons[2].setPosition(persons[0], persons[1]);
		AnalyzePoke ana = AnalyzePoke.getInstance();

		for (int i = 0; i < persons.length; i++) {
			boolean b = ana.testAnalyze(personPokes[i]);
			if (!b) {
				init();
				break;
			}
		}
		for (int i = 0; i < 3; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append("chushipai---" + i + ":");
			for (int j = 0; j < personPokes[i].length; j++) {
				sb.append(personPokes[i][j] + ",");
			}
		}
	}

	// ��������������ŵ��Ƹ�����
	private void randDZ() {
		boss = Poke.getDZ();
		currentPerson = boss;
		int[] newPersonPokes = new int[20];
		for (int i = 0; i < 17; i++) {
			newPersonPokes[i] = personPokes[boss][i];
		}
		newPersonPokes[17] = threePokes[0];
		newPersonPokes[18] = threePokes[1];
		newPersonPokes[19] = threePokes[2];
		personPokes[boss] = newPersonPokes;
	}

	public void fenpai(int[] pokes) {
		for (int i = 0; i < 51;) {
			personPokes[i / 17][i % 17] = pokes[i++];
		}
		threePokes[0] = pokes[51];
		threePokes[1] = pokes[52];
		threePokes[2] = pokes[53];
	}

	public void result() {

	}

	public void paint(Canvas canvas) {

		switch (op) {
		case -2:
			break;
		case -1:
			break;
		case 0:
			paintGaming(canvas);
			break;
		case 1:
			paintResult(canvas);
			break;
		case 2:
			break;
		}

	}

	private void paintResult(Canvas canvas) {
		Paint paint = new Paint();
		Rect src = new Rect();
		Rect dst = new Rect();
		int wid = end.getWidth();
		int hei = end.getHeight();
		int left = (800 - wid) / 2;
		int up = (480 - hei) / 2;
		paint.setColor(Color.WHITE);
		paint.setTextSize(60);
		src.set(0, 0, end.getWidth(), end.getHeight());
		dst.set(left, up, left + wid, up + hei);
		canvas.drawBitmap(end, src, dst, null);
		for (int i = 0; i < 3; i++) {
			canvas.drawText("" + rs[i], left + 290, 215 + i * 95, paint);
		}

	}

	private void paintGaming(Canvas canvas) {
		persons[0].paint(canvas);
		persons[1].paint(canvas);
		persons[2].paint(canvas);
		paintThreePokes(canvas);
		paintRoleAndScore(canvas);
		if (currentPerson == 0) {
			Rect src = new Rect();
			Rect dst = new Rect();
			if (currentCircle != 0) {
				src.set(0, 0, buyao.getWidth(), buyao.getHeight());
				dst.set(opPosX - buyao.getWidth() - 2, opPosY, opPosX - 2,
						opPosY + buyao.getHeight());
				canvas.drawBitmap(buyao, src, dst, null);
				src.set(0, 0, chupai.getWidth(), chupai.getHeight());
				dst.set(opPosX + 2, opPosY, opPosX + chupai.getWidth() + 2,
						opPosY + chupai.getHeight());
				canvas.drawBitmap(chupai, src, dst, null);
			} else {
				src.set(0, 0, chupai.getWidth(), chupai.getHeight());
				dst.set(opPosX - chupai.getWidth() / 2, opPosY,
						opPosX + chupai.getWidth() / 2,
						opPosY + chupai.getHeight());
				canvas.drawBitmap(chupai, src, dst, null);
			}
		}

		if (persons[0].card != null) {
			persons[0].card.paint(canvas, 217, 210, PokeType.dirH);
		}
		if (persons[1].card != null) {
			persons[1].card.paint(canvas, 122, 84, PokeType.dirV);
		}
		if (persons[2].card != null) {
			persons[2].card.paint(canvas, 607, 84, PokeType.dirV);
		}

		paintTimeLimite(canvas);
		Paint paint = new Paint();
		paint.setTextAlign(Align.LEFT);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(SIZE_CLOCK * 5 / 4);
		canvas.drawText("      ��ʼ������" + currentScore, 280, 465, paint);
	}

	private void paintTimeLimite(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(SIZE_CLOCK * 3 / 2);
		Rect src = new Rect();
		Rect dst = new Rect();

		for (int i = 0; i < 3; i++) {
			if (i == currentPerson) {
				int left = timeLimitePos[i][0] - 2;
				int up = timeLimitePos[i][1] - SIZE_CLOCK;
				int right = timeLimitePos[i][0] + clock.getWidth() - 2;
				int bottom = timeLimitePos[i][1] + clock.getHeight()
						- SIZE_CLOCK;
				src.set(0, 0, clock.getWidth(), clock.getHeight());
				dst.set(left, up, right, bottom);
				canvas.drawBitmap(clock, src, dst, null);
				canvas.drawText("" + (timeLimite / 10), left + (right - left)
						/ 4, bottom - 10, paint);
				if (timeLimite < 0)
					buyao();

			}
		}
	}

	private void paintRoleAndScore(Canvas canvas) {
		Paint paint = new Paint();
		for (int i = 0; i < 3; i++) {
			if (boss == i) {
				paint.setColor(Color.BLACK);
				paint.setTextSize(20);
				canvas.drawText("����(�÷֣�" + personScore[i] + ")", rolePos[i][0],
						rolePos[i][1], paint);
			} else {
				paint.setColor(Color.WHITE);
				paint.setTextSize(20);
				canvas.drawText("ũ��(�÷֣�" + personScore[i] + ")", rolePos[i][0],
						rolePos[i][1], paint);
			}
		}
	}

	private void paintThreePokes(Canvas canvas) {
		Rect src = new Rect();
		Rect dst = new Rect();
		for (int i = 0; i < 3; i++) {
			int row = Poke.getImageRow(threePokes[i]);
			int col = Poke.getImageCol(threePokes[i]);
			src.set(col * 64, row * 93, col * 64 + 64, row * 93 + 93);
			dst.set(threePokesPos[i][0], threePokesPos[i][1],
					threePokesPos[i][0] + 64, threePokesPos[i][1] + 93);
			canvas.drawBitmap(pokeImage, src, dst, null);
		}

	}

	public void onTuch(View v, MotionEvent event) {
		int wid = buyao.getWidth(); 
		int hei = buyao.getHeight();
		for (int i = 0; i < persons.length; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(i + " : ");
			for (int j = 0; j < persons[i].pokes.length; j++) {
				sb.append(persons[i].pokes[j]
						+ (persons[i].pokes[j] >= 10 ? "" : " ") + ",");
			}
		}

		if (op == 1) {
			init();
			op = 0;
		}
		if (currentPerson != 0) {
			return;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (currentCircle != 0) {// ��Ҫ
			if (Poke.inRect(x, y, opPosX + 2, opPosY, wid,hei)) {// ����
				Card card = persons[0].chupai(currentCard);
				if (card != null) {
					currentCard = card;
					currentCircle++;
					nextPerson();
				}
			} else if (Poke.inRect(x, y, opPosX - wid - 2, opPosY, wid,
					hei)) {
				buyao();
			}
		} else if (Poke.inRect(x, y, opPosX - wid / 2, opPosY,wid,hei)) {// ����
			Card card = persons[0].chupai(currentCard);
			if (card != null) {
				currentCard = card;
				currentCircle++;
				nextPerson();
			}
		}
		persons[0].onTuch(v, event);
	}

	// ��Ҫ�ƵĲ���
	private void buyao() {
		// �ֵ���һ����
		currentCircle++;
		// ��յ�ǰ��Ҫ�Ƶ��˵����һ����
		persons[currentPerson].card = null;
		// ��λ��һ���˵�id
		nextPerson();
		// ����Ѿ�ת����������˼������ƣ�������գ���һ�ֿ�ʼ
		if (currentCard != null && currentPerson == currentCard.personID) {
			currentCircle = 0;
			currentCard = null;// ת�ص�����Ƶ��Ǹ����ٳ���
			persons[currentPerson].card = null;
		}
	}

	// ��λ��һ���˵�id�����µ���ʱ
	private void nextPerson() {
		switch (currentPerson) {
		case 0:
			currentPerson = 2;
			break;
		case 1:
			currentPerson = 0;
			break;
		case 2:
			currentPerson = 1;
			break;
		}
		timeLimite = 200;
	}
}
