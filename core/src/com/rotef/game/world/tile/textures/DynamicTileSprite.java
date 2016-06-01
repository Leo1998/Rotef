package com.rotef.game.world.tile.textures;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rotef.game.assets.Sprite;
import com.rotef.game.world.tile.Tile;

public class DynamicTileSprite extends TileSprite {

	private Sprite map;

	private int xTex = -1;
	private int yTex = -1;
	private Sprite currentSprite;

	public DynamicTileSprite(Tile tile, Sprite map) {
		super(tile);

		this.map = map;
	}

	@Override
	public void update() {
		int xTexNew = 1;
		int yTexNew = 1;

		if (!tile.hasTileAbove() && tile.hasTileRight() && tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 1;
			yTexNew = 0;
		} else if (tile.hasTileAbove() && !tile.hasTileRight() && tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 2;
			yTexNew = 1;
		} else if (tile.hasTileAbove() && tile.hasTileRight() && !tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 1;
			yTexNew = 2;
		} else if (tile.hasTileAbove() && tile.hasTileRight() && tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 0;
			yTexNew = 1;
		} else if (!tile.hasTileAbove() && tile.hasTileRight() && tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 0;
			yTexNew = 0;
		} else if (!tile.hasTileAbove() && !tile.hasTileRight() && tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 2;
			yTexNew = 0;
		} else if (tile.hasTileAbove() && !tile.hasTileRight() && !tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 2;
			yTexNew = 2;
		} else if (tile.hasTileAbove() && tile.hasTileRight() && !tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 0;
			yTexNew = 2;
		} else if (!tile.hasTileAbove() && !tile.hasTileRight() && tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 3;
			yTexNew = 0;
		} else if (tile.hasTileAbove() && !tile.hasTileRight() && tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 3;
			yTexNew = 1;
		} else if (tile.hasTileAbove() && !tile.hasTileRight() && !tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 3;
			yTexNew = 2;
		} else if (!tile.hasTileAbove() && tile.hasTileRight() && !tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 0;
			yTexNew = 3;
		} else if (!tile.hasTileAbove() && tile.hasTileRight() && !tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 1;
			yTexNew = 3;
		} else if (!tile.hasTileAbove() && !tile.hasTileRight() && !tile.hasTileBeneath() && tile.hasTileLeft()) {
			xTexNew = 2;
			yTexNew = 3;
		} else if (!tile.hasTileAbove() && !tile.hasTileRight() && !tile.hasTileBeneath() && !tile.hasTileLeft()) {
			xTexNew = 3;
			yTexNew = 3;
		}

		if (xTex != xTexNew || yTex != yTexNew) {
			xTex = xTexNew;
			yTex = yTexNew;
			TextureRegion tex = new TextureRegion(map.getTex(), xTex * Tile.TILE_TEXTURE_SIZE, yTex * Tile.TILE_TEXTURE_SIZE, Tile.TILE_TEXTURE_SIZE, Tile.TILE_TEXTURE_SIZE);
			if (this.currentSprite == null) {
				this.currentSprite = new Sprite(tex);
			} else {
				this.currentSprite.setTex(tex);
			}
		}
	}

	@Override
	public Sprite getSprite() {
		return currentSprite;
	}
}
