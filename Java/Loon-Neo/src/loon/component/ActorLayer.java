/**
 * 
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loon
 * @author cping
 * @email：javachenpeng@yahoo.com
 * @version 0.1.1
 */
package loon.component;

import java.util.Iterator;

import loon.action.ActionBind;
import loon.action.ArrowTo;
import loon.action.CircleTo;
import loon.action.ColorTo;
import loon.action.FadeTo;
import loon.action.FireTo;
import loon.action.JumpTo;
import loon.action.MoveTo;
import loon.action.RotateTo;
import loon.action.ScaleTo;
import loon.action.ShakeTo;
import loon.action.map.Field2D;
import loon.action.sprite.ISprite;
import loon.canvas.LColor;
import loon.event.SysInput;
import loon.geom.RectBox;
import loon.utils.MathUtils;
import loon.utils.TArray;

public abstract class ActorLayer extends LContainer {

	private final static int min_size = 48;

	private Field2D tmpField;

	private boolean isBounded;

	protected int cellSize;

	CollisionChecker collisionChecker;

	ActorTreeSet objects;

	long elapsedTime;

	private int tileSize = 32;

	public ActorLayer(int x, int y, int layerWidth, int layerHeight,
			int cellSize) {
		this(x, y, layerWidth, layerHeight, cellSize, true);
	}

	public ActorLayer(int x, int y, int layerWidth, int layerHeight,
			int cellSize, boolean bounded) {
		super(x, y, layerWidth, layerHeight);
		this.collisionChecker = new CollisionManager();
		this.objects = new ActorTreeSet();
		this.cellSize = cellSize;
		this.initialize(layerWidth, layerHeight, cellSize);
		this.isBounded = bounded;
	}

	private void initialize(int width, int height, int cellSize) {
		this.cellSize = cellSize;
		this.collisionChecker.initialize(cellSize);
	}

	public SysInput screenInput() {
		return input;
	}

	public int getCellSize() {
		return this.cellSize;
	}

	public void setCellSize(int cellSize) {
		synchronized (collisionChecker) {
			this.cellSize = cellSize;
			this.collisionChecker.initialize(cellSize);
		}
	}

	/**
	 * 让指定对象执行MoveTo事件
	 * 
	 * @param field
	 * @param o
	 * @param flag
	 * @param x
	 * @param y
	 * @return
	 */
	public MoveTo callMoveTo(Field2D field, ActionBind o, boolean flag, int x,
			int y) {
		if (isClose) {
			return null;
		}
		MoveTo move = new MoveTo(field, x, y, flag);
		addActionEvent(move, o);
		return move;
	}

	/**
	 * 让指定对象执行MoveTo事件
	 * 
	 * @param field
	 * @param o
	 * @param x
	 * @param y
	 * @return
	 */
	public MoveTo callMoveTo(Field2D field, ActionBind o, int x, int y) {
		return callMoveTo(field, o, true, x, y);
	}

	/**
	 * 让指定对象执行MoveTo事件
	 * 
	 * @param o
	 * @param flag
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public MoveTo callMoveTo(ActionBind o, boolean flag, int x, int y, int w,
			int h) {
		if (isClose) {
			return null;
		}
		if (tmpField == null) {
			tmpField = createArrayMap(w, h);
		}
		MoveTo move = new MoveTo(tmpField, x, y, flag);
		addActionEvent(move, o);
		return move;
	}

	/**
	 * 让指定对象执行MoveTo事件
	 * 
	 * @param o
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public MoveTo callMoveTo(ActionBind o, int x, int y, int w, int h) {
		return callMoveTo(o, true, x, y, w, h);
	}

	/**
	 * 让指定对象执行MoveTo事件
	 * 
	 * @param o
	 * @param x
	 * @param y
	 * @return
	 */
	public MoveTo callMoveTo(ActionBind o, int x, int y) {
		return callMoveTo(o, x, y, tileSize, tileSize);
	}

	/**
	 * 让指定对象执行MoveTo事件
	 * 
	 * @param o
	 * @param x
	 * @param y
	 * @param flag
	 * @return
	 */
	public MoveTo callMoveTo(ActionBind o, int x, int y, boolean flag) {
		return callMoveTo(o, flag, x, y, tileSize, tileSize);
	}

	/**
	 * 让指定对象执行FadeTo事件
	 * 
	 * @param o
	 * @param type
	 * @param speed
	 * @return
	 */
	public FadeTo callFadeTo(ActionBind o, int type, int speed) {
		if (isClose) {
			return null;
		}
		FadeTo fade = new FadeTo(type, speed);
		addActionEvent(fade, o);
		return fade;
	}

	/**
	 * 让指定对象执行FadeTo淡入事件
	 * 
	 * @param o
	 * @param speed
	 * @return
	 */
	public FadeTo callFadeInTo(ActionBind o, int speed) {
		return callFadeTo(o, ISprite.TYPE_FADE_IN, speed);
	}

	/**
	 * 让指定对象执行FadeTo淡出事件
	 * 
	 * @param o
	 * @param speed
	 * @return
	 */
	public FadeTo callFadeOutTo(ActionBind o, int speed) {
		return callFadeTo(o, ISprite.TYPE_FADE_OUT, speed);
	}

	/**
	 * 让指定对象执行RotateTo旋转事件
	 * 
	 * @param o
	 * @param angle
	 * @param speed
	 * @return
	 */
	public RotateTo callRotateTo(ActionBind o, float angle, float speed) {
		if (isClose) {
			return null;
		}
		RotateTo rotate = new RotateTo(angle, speed);
		addActionEvent(rotate, o);
		return rotate;
	}

	/**
	 * 让指定对象执行JumpTo跳跃事件
	 * 
	 * @param o
	 * @param j
	 * @param g
	 * @return
	 */
	public JumpTo callJumpTo(ActionBind o, int j, float g) {
		if (isClose) {
			return null;
		}
		JumpTo jump = new JumpTo(j, g);
		addActionEvent(jump, o);
		return jump;
	}

	/**
	 * 让指定角色根据指定半径以指定速度循环转动
	 * 
	 * @param o
	 * @param radius
	 * @param velocity
	 * @return
	 */
	public CircleTo callCircleTo(ActionBind o, int radius, int velocity) {
		if (isClose) {
			return null;
		}
		CircleTo circle = new CircleTo(radius, velocity);
		addActionEvent(circle, o);
		return circle;
	}

	/**
	 * 向指定坐标以指定速度让指定角色做为子弹发射
	 * 
	 * @param o
	 * @param x
	 * @param y
	 * @param speed
	 * @return
	 */
	public FireTo callFireTo(ActionBind o, float x, float y, float speed) {
		if (isClose) {
			return null;
		}
		FireTo fire = new FireTo(x, y, speed);
		addActionEvent(fire, o);
		return fire;
	}

	/**
	 * 让角色缩放指定大小
	 * 
	 * @param o
	 * @param sx
	 * @param sy
	 * @return
	 */
	public ScaleTo callScaleTo(ActionBind o, float sx, float sy) {
		if (isClose) {
			return null;
		}
		ScaleTo scale = new ScaleTo(sx, sy);
		addActionEvent(scale, o);
		return scale;
	}

	/**
	 * 让角色缩放指定大小
	 * 
	 * @param o
	 * @param s
	 * @return
	 */
	public ScaleTo callScaleTo(ActionBind o, float s) {
		return callScaleTo(o, s, s);
	}

	/**
	 * 让指定角色做箭状发射(抛物线)
	 * 
	 * @param o
	 * @param tx
	 * @param ty
	 * @return
	 */
	public ArrowTo callArrowTo(ActionBind o, float tx, float ty) {
		if (isClose) {
			return null;
		}
		ArrowTo arrow = new ArrowTo(tx, ty);
		addActionEvent(arrow, o);
		return arrow;
	}

	/**
	 * 渐变对象到指定颜色
	 * 
	 * @param o
	 * @param start
	 * @param end
	 * @return
	 */
	public ColorTo callColorTo(ActionBind o, LColor start, LColor end) {
		if (isClose) {
			return null;
		}
		ColorTo color = new ColorTo(start, end, 1f);
		addActionEvent(color, o);
		return color;
	}

	/**
	 * 渐变对象到指定颜色
	 * 
	 * @param o
	 * @param end
	 * @param duration
	 * @return
	 */
	public ColorTo callColorTo(ActionBind o, LColor end, float duration) {
		if (isClose) {
			return null;
		}
		ColorTo color = new ColorTo(o.getColor(), end, duration);
		addActionEvent(color, o);
		return color;
	}

	/**
	 * 渐变对象到指定颜色
	 * 
	 * @param o
	 * @param start
	 * @param end
	 * @param duration
	 * @param delay
	 * @return
	 */
	public ColorTo callColorTo(ActionBind o, LColor start, LColor end,
			float duration, float delay) {
		if (isClose) {
			return null;
		}
		ColorTo color = new ColorTo(start, end, duration, delay);
		addActionEvent(color, o);
		return color;
	}

	/**
	 * 振动指定对象
	 * 
	 * @param o
	 * @param shakeX
	 * @param shakeY
	 * @return
	 */
	public ShakeTo callShakeTo(ActionBind o, float shakeX, float shakeY) {
		if (isClose) {
			return null;
		}
		ShakeTo shake = new ShakeTo(shakeX, shakeY);
		addActionEvent(shake, o);
		return shake;
	}

	/**
	 * 振动指定对象
	 * 
	 * @param o
	 * @param shakeX
	 * @param shakeY
	 * @param duration
	 * @param delay
	 * @return
	 */
	public ShakeTo callShakeTo(ActionBind o, float shakeX, float shakeY,
			float duration, float delay) {
		if (isClose) {
			return null;
		}
		ShakeTo shake = new ShakeTo(shakeX, shakeY, duration, delay);
		addActionEvent(shake, o);
		return shake;
	}

	/**
	 * 以指定瓦片大小创建数组地图
	 * 
	 * @param tileWidth
	 * @param tileHeight
	 * @return
	 */
	public Field2D createArrayMap(int tileWidth, int tileHeight) {
		if (isClose) {
			return null;
		}
		tmpField = new Field2D(
				new int[(int) (getHeight() / tileHeight)][(int) (getWidth() / tileWidth)],
				tileWidth, tileHeight);
		return tmpField;
	}

	/**
	 * 设定Layer对应的二维数组地图
	 * 
	 * @param map
	 */
	public void setField2D(Field2D field) {
		if (isClose) {
			return;
		}
		if (field == null) {
			return;
		}
		if (tmpField != null) {
			if ((field.getMap().length == tmpField.getMap().length)
					&& (field.getTileWidth() == tmpField.getTileWidth())
					&& (field.getTileHeight() == tmpField.getTileHeight())) {
				tmpField.set(field.getMap(), field.getTileWidth(),
						field.getTileHeight());
			}
		} else {
			tmpField = field;
		}
	}

	/**
	 * 返回Layer对应的二维数据地图
	 * 
	 * @param map
	 */
	@Override
	public Field2D getField2D() {
		return tmpField;
	}

	/**
	 * 添加角色到Layer(在Layer中添加的角色将自动赋予碰撞检查)
	 * 
	 * @param object
	 * @param x
	 * @param y
	 */
	public void addObject(Actor object, float x, float y) {
		if (isClose) {
			return;
		}
		synchronized (objects) {
			if (this.objects.add(object)) {
				object.addLayer(x, y, this);
				this.collisionChecker.addObject(object);
				object.addLayer(this);
			}
		}
	}

	/**
	 * 添加角色到Layer
	 * 
	 * @param object
	 */
	public void addObject(Actor object) {
		if (isClose) {
			return;
		}
		addObject(object, object.x(), object.y());
	}

	/**
	 * 将指定角色于Layer前置
	 * 
	 * @param actor
	 */
	void sendToFront(Actor actor) {
		if (isClose) {
			return;
		}
		if (objects != null) {
			synchronized (objects) {
				if (objects != null) {
					objects.sendToFront(actor);
				}
			}
		}
	}

	/**
	 * 将指定角色于Layer后置
	 * 
	 * @param actor
	 */
	void sendToBack(Actor actor) {
		if (isClose) {
			return;
		}
		if (objects != null) {
			synchronized (objects) {
				if (objects != null) {
					objects.sendToBack(actor);
				}
			}
		}
	}

	/**
	 * 参考指定大小根据Layer范围生成一组不重复的随机坐标
	 * 
	 * @param act
	 * @param count
	 * @return
	 */
	public RectBox[] getRandomLayerLocation(int nx, int ny, int nw, int nh,
			int count) {
		if (isClose) {
			return null;
		}
		if (count <= 0) {
			throw new RuntimeException("count <= 0 !");
		}
		int layerWidth = (int) getWidth();
		int layerHeight = (int) getHeight();
		int actorWidth = nw > min_size ? nw : min_size;
		int actorHeight = nh > min_size ? nh : min_size;
		int x = nx / actorWidth;
		int y = ny / actorHeight;
		int row = layerWidth / actorWidth;
		int col = layerHeight / actorHeight;
		RectBox[] randoms = new RectBox[count];
		int oldRx = 0, oldRy = 0;
		int index = 0;
		for (int i = 0; i < count * 100; i++) {
			if (index >= count) {
				return randoms;
			}
			int rx = MathUtils.random(row);
			int ry = MathUtils.random(col);
			if (oldRx != rx && oldRy != ry && rx != x && ry != y
					&& rx * actorWidth != nx && ry * actorHeight != ny) {
				boolean stop = false;
				for (int j = 0; j < index; j++) {
					if (randoms[j].x == rx && randoms[j].y == ry && oldRx != x
							&& oldRy != y && rx * actorWidth != nx
							&& ry * actorHeight != ny) {
						stop = true;
						break;
					}
				}
				if (stop) {
					continue;
				}
				randoms[index] = new RectBox(rx * actorWidth, ry * actorHeight,
						actorWidth, actorHeight);
				oldRx = rx;
				oldRy = ry;
				index++;
			}
		}
		return null;
	}

	/**
	 * 参考指定大小根据Layer范围生成一组不重复的随机坐标
	 * 
	 * @param actorWidth
	 * @param actorHeight
	 * @param count
	 * @return
	 */
	public RectBox[] getRandomLayerLocation(int actorWidth, int actorHeight,
			int count) {
		if (isClose) {
			return null;
		}
		return getRandomLayerLocation(0, 0, actorWidth, actorHeight, count);
	}

	/**
	 * 参考指定角色根据Layer范围生成一组不重复的随机坐标
	 * 
	 * @param actor
	 * @param count
	 * @return
	 */
	public RectBox[] getRandomLayerLocation(Actor actor, int count) {
		if (isClose) {
			return null;
		}
		RectBox rect = actor.getRectBox();
		return getRandomLayerLocation((int) rect.x, (int) rect.y, rect.width,
				rect.height, count);
	}

	/**
	 * 参考指定Actor大小根据Layer生成一个不重复的随机坐标
	 * 
	 * @param actor
	 * @return
	 */
	public RectBox getRandomLayerLocation(Actor actor) {
		if (isClose) {
			return null;
		}
		RectBox[] rects = getRandomLayerLocation(actor, 1);
		if (rects != null) {
			return rects[0];
		}
		return null;
	}

	/**
	 * 删除指定的角色
	 * 
	 * @param object
	 */
	public void removeObject(Actor object) {
		if (isClose) {
			return;
		}
		if (objects.size() == 0) {
			return;
		}
		synchronized (objects) {
			if (this.objects.remove(object)) {
				this.collisionChecker.removeObject(object);
			}
			removeActionEvents(object);
			object.setLayer((ActorLayer) null);
		}
	}

	/**
	 * 删除指定集合中的所有角色
	 * 
	 * @param objects
	 */
	public void removeObjects(TArray<Actor> objects) {
		if (isClose) {
			return;
		}
		synchronized (objects) {
			Iterator<Actor> iter = objects.iterator();
			while (iter.hasNext()) {
				Actor actor = iter.next();
				this.removeObject(actor);
			}
		}
	}

	/**
	 * 删除指定集合中的所有角色
	 * 
	 * @param flagName
	 */
	public void removeObject(String flagName) {
		if (isClose) {
			return;
		}
		synchronized (objects) {
			Iterator<Actor> it = objects.iterator();
			while (it.hasNext()) {
				Actor actor = it.next();
				if (actor == null) {
					continue;
				}
				String flag = actor.getFlag();
				if (flagName == null || flagName == flag
						|| flagName.equals(flag)) {
					if (this.objects.remove(actor)) {
						this.collisionChecker.removeObject(actor);
					}
					removeActionEvents(actor);
					actor.setLayer((ActorLayer) null);
				}
			}
		}
	}

	/**
	 * 获得含有指定角色碰撞的List集合
	 * 
	 * @param actor
	 * @return
	 */
	public TArray<Actor> getCollisionObjects(Actor actor) {
		if (isClose) {
			return null;
		}
		return getCollisionObjects(actor.getFlag());
	}

	/**
	 * 刷新缓存数据，重置世界
	 * 
	 */
	public void reset() {
		if (isClose) {
			return;
		}
		if (objects != null) {
			synchronized (objects) {
				if (objects != null) {
					objects.clear();
					objects = null;
				}
				if (collisionChecker != null) {
					collisionChecker.dispose();
					collisionChecker.clear();
					collisionChecker = null;
				}
				this.collisionChecker = new CollisionManager();
				this.objects = new ActorTreeSet();
			}
		}
	}

	/**
	 * 获得指定类所产生角色碰撞的List集合
	 * 
	 * @param flag
	 * @return
	 */
	public TArray<Actor> getCollisionObjects(String flag) {
		if (isClose) {
			return null;
		}
		return this.collisionChecker.getObjects(flag);
	}

	/**
	 * 返回指定角色类在指定位置的List集合
	 * 
	 * @param x
	 * @param y
	 * @param flag
	 * @return
	 */
	public TArray<Actor> getCollisionObjectsAt(float x, float y, String flag) {
		if (isClose) {
			return null;
		}
		return this.collisionChecker.getObjectsAt(x, y, flag);
	}

	/**
	 * 返回指定区域对应的单一Actor
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Actor getOnlyCollisionObjectsAt(float x, float y) {
		if (isClose) {
			return null;
		}
		return objects.getOnlyCollisionObjectsAt(x, y);
	}

	/**
	 * 返回指定区域和标记对应的单一Actor
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Actor getOnlyCollisionObjectsAt(float x, float y, Object tag) {
		if (isClose) {
			return null;
		}
		return objects.getOnlyCollisionObjectsAt(x, y, tag);
	}

	/**
	 * 角色对象总数
	 * 
	 * @return
	 */
	public int size() {
		if (isClose) {
			return 0;
		}
		return this.objects.size();
	}

	public abstract void action(long elapsedTime);

	@Override
	public boolean isBounded() {
		return this.isBounded;
	}

	Actor getSynchronizedObject(float x, float y) {
		if (isClose) {
			return null;
		}
		return objects.getSynchronizedObject(x, y);
	}

	TArray<Actor> getIntersectingObjects(Actor actor, String flag) {
		if (isClose) {
			return null;
		}
		return this.collisionChecker.getIntersectingObjects(actor, flag);
	}

	Actor getOnlyIntersectingObject(Actor object, String flag) {
		if (isClose) {
			return null;
		}
		return this.collisionChecker.getOnlyIntersectingObject(object, flag);
	}

	TArray<Actor> getObjectsInRange(float x, float y, float r, String flag) {
		if (isClose) {
			return null;
		}
		return this.collisionChecker.getObjectsInRange(x, y, r, flag);
	}

	TArray<Actor> getNeighbours(Actor actor, float distance, boolean d,
			String flag) {
		if (isClose) {
			return null;
		}
		if (distance < 0) {
			throw new RuntimeException("distance < 0");
		} else {
			return this.collisionChecker
					.getNeighbours(actor, distance, d, flag);
		}
	}

	int getHeightInPixels() {
		return (int) (this.getHeight() * this.cellSize);
	}

	int getWidthInPixels() {
		return (int) (this.getWidth() * this.cellSize);
	}

	int toCellCeil(float pixel) {
		return MathUtils.ceil(pixel / this.cellSize);
	}

	int toCellFloor(float pixel) {
		return MathUtils.floor(pixel / this.cellSize);
	}

	float getCellCenter(float c) {
		float cellCenter = (c * this.cellSize) + this.cellSize / 2.0f;
		return cellCenter;
	}

	TArray<Actor> getCollisionObjects(float x, float y) {
		if (isClose) {
			return null;
		}
		return collisionChecker.getObjectsAt(x, y, null);
	}

	void updateObjectLocation(Actor object, float oldX, float oldY) {
		if (isClose) {
			return;
		}
		this.collisionChecker.updateObjectLocation(object, oldX, oldY);
	}

	void updateObjectSize(Actor object) {
		if (isClose) {
			return;
		}
		this.collisionChecker.updateObjectSize(object);
	}

	Actor getOnlyObjectAt(Actor object, float dx, float dy, String flag) {
		if (isClose) {
			return null;
		}
		return this.collisionChecker.getOnlyObjectAt(object, dx, dy, flag);
	}

	ActorTreeSet getObjectsListInPaintO() {
		if (isClose) {
			return null;
		}
		return this.objects;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

}
