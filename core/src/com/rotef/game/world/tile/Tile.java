package com.rotef.game.world.tile;

import com.badlogic.gdx.graphics.Color;
import com.rotef.game.Game;
import com.rotef.game.template.Template;
import com.rotef.game.world.World;
import com.rotef.game.world.tile.textures.DynamicTileSprite;
import com.rotef.game.world.tile.textures.TileSprite;

public abstract class Tile {

	public enum Type {
		Solid(TileSolid.class), Fluid(TileFluid.class);

		//

		final Class<? extends Tile> clazz;

		Type(Class<? extends Tile> clazz) {
			this.clazz = clazz;
		}
	}

	public static final int TILE_SIZE = 32;
	public static final int TILE_TEXTURE_SIZE = 16;

	private final World world;

	private Template template;
	private int id;
	private Type type;
	private String name;
	private boolean solid = false;
	private TileSprite sprite;
	private Color lightColor = null;

	private int xTile;
	private int yTile;

	private boolean hasTileAbove = false;
	private boolean hasTileRight = false;
	private boolean hasTileBeneath = false;
	private boolean hasTileLeft = false;

	Tile(Template template, World world, int xTile, int yTile) {
		this.template = template;
		this.id = template.getInteger("id");
		this.type = Type.valueOf(template.getString("type"));
		this.name = template.getString("name");
		this.sprite = new DynamicTileSprite(this, Game.assets.getSprite(template.getString("spritePath")));
		this.lightColor = template.getColor("lightColor");

		this.world = world;
		this.xTile = xTile;
		this.yTile = yTile;
	}

	/**
	 * Do not use this method it is only for updating internal stuff.
	 * 
	 * @param delta
	 */
	public void internalUpdate(float delta) {
		hasTileAbove = world.getTile(xTile, yTile + 1) != null;
		hasTileRight = world.getTile(xTile + 1, yTile) != null;
		hasTileBeneath = world.getTile(xTile, yTile - 1) != null;
		hasTileLeft = world.getTile(xTile - 1, yTile) != null;

		if (this.hasSprite()) {
			sprite.update();
		}
	}

	public abstract void update(float delta);

	public Template getTemplate() {
		return template;
	}

	public int getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isSolid() {
		return solid;
	}

	public World getWorld() {
		return world;
	}

	public TileSprite getSprite() {
		return sprite;
	}

	public boolean hasSprite() {
		return sprite != null;
	}

	protected void setSprite(TileSprite sprite) {
		this.sprite = sprite;
	}

	public boolean hasTileAbove() {
		return hasTileAbove;
	}

	public boolean hasTileRight() {
		return hasTileRight;
	}

	public boolean hasTileBeneath() {
		return hasTileBeneath;
	}

	public boolean hasTileLeft() {
		return hasTileLeft;
	}

	public int getXTile() {
		return xTile;
	}

	public int getYTile() {
		return yTile;
	}

	protected void setSolid(boolean solid) {
		this.solid = solid;
	}

	public Color getLightColor() {
		return lightColor != null ? lightColor : Color.BLACK;
	}

}
