package org.test;

import loon.LTransition;
import loon.Screen;
import loon.component.LClickButton;
import loon.component.layout.VerticalLayout;
import loon.event.GameTouch;
import loon.opengl.GLEx;
import loon.utils.res.ResourceLocal;
import loon.utils.timer.LTimerContext;

public class JSonResTest extends Screen {

	@Override
	public LTransition onTransition() {
		return LTransition.newEmpty();
	}

	@Override
	public void draw(GLEx g) {

	}

	@Override
	public void onLoad() {
		// 加载json配置的资源文件（loon中默认识别的，是一种flash游戏开发中常见的json资源格式，很多flash小游戏都采取这种格式，方便移植，然后你懂的……）
		ResourceLocal local = getResourceConfig("resource");
		LClickButton quitgame = LClickButton.make(local.getTexture("quitgame")
				.img());
		quitgame.setGrayButton(true);
	
		LClickButton tryagain = LClickButton.make(local.getTexture("tryagain")
				.img());
		tryagain.setGrayButton(true);

		LClickButton gushimoshi = LClickButton.make(local.getTexture(
				"gushimoshi").img());
		gushimoshi.setGrayButton(true);

		add(quitgame, tryagain, gushimoshi);

		// 设定留空大小，分别为屏幕上方25%，右15%，下15%，左32%
		getRootConstraints().setPadding("15%", "25%", "15%", "32%");

		// 布局器为竖立方式
		VerticalLayout layout = new VerticalLayout();
		// 不需要自动改变对象大小
		layout.setChangeSize(false);
		// 执行布局
		layoutElements(layout, quitgame, tryagain, gushimoshi);

		add(MultiScreenTest.getBackButton(this,0));
	}

	@Override
	public void alter(LTimerContext timer) {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void touchDown(GameTouch e) {

	}

	@Override
	public void touchUp(GameTouch e) {

	}

	@Override
	public void touchMove(GameTouch e) {

	}

	@Override
	public void touchDrag(GameTouch e) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void close() {

	}

}
