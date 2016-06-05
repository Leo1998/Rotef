package com.rotef.game.world.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rotef.game.chat.ChatMessage;
import com.rotef.game.chat.ChatMessageSerializer;
import com.rotef.game.util.FileUtils;
import com.rotef.game.world.World;
import com.rotef.game.world.WorldChunk;

public class WorldLoader {

	private ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
		@Override
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();

			kryo.register(WorldData.class, new WorldDataSerializer());
			kryo.register(WorldChunk.class, new WorldChunkSerializer(world));
			kryo.register(ChatMessage.class, new ChatMessageSerializer());

			kryo.register(Color.class, new Serializer<Color>() {
				@Override
				public Color read(Kryo kryo, Input input, Class<Color> type) {
					Color color = new Color();
					Color.rgba8888ToColor(color, input.readInt());
					return color;
				}

				@Override
				public void write(Kryo kryo, Output output, Color color) {
					output.writeInt(Color.rgba8888(color));
				}
			});

			return kryo;
		};
	};

	private FileHandle rootDir;

	private FileHandle worldFile;
	private FileHandle chunksFile;

	private boolean firstInit = false;
	private World world;

	public WorldLoader(World world) {
		this.world = world;

		rootDir = world.getDescriptor().getDirectory();
		if (!rootDir.exists()) {
			firstInit = true;
			rootDir.mkdirs();
		}

		worldFile = rootDir.child("world.bin");
		chunksFile = rootDir.child("chunks.bin");
	}

	public void writeWorldData(WorldData worldData) {
		Kryo kryo = kryos.get();
		Output output = new Output(worldFile.write(false));

		kryo.writeObject(output, worldData);

		output.close();

		FileUtils.createBackupFile(worldFile);
	}

	public WorldData readWorldData() {
		Kryo kryo = kryos.get();
		Input input = new Input(worldFile.read());

		WorldData worldData = kryo.readObject(input, WorldData.class);

		input.close();

		return worldData;
	}

	public void writeChunkData(Array<WorldChunk> chunks) {
		Kryo kryo = kryos.get();
		Output output = new Output(chunksFile.write(false));

		int l = chunks.size;
		output.writeInt(l);

		for (int i = 0; i < l; i++) {
			WorldChunk chunk = chunks.get(i);

			if (chunk != null)
				kryo.writeObject(output, chunk);
		}

		output.close();

		FileUtils.createBackupFile(chunksFile);
	}

	public Array<WorldChunk> readChunkData() {
		Kryo kryo = kryos.get();
		Input input = new Input(chunksFile.read());

		int l = input.readInt();

		Array<WorldChunk> chunks = new Array<WorldChunk>(l);

		for (int i = 0; i < l; i++) {
			WorldChunk chunk = kryo.readObject(input, WorldChunk.class);

			chunks.add(chunk);
		}

		input.close();

		return chunks;
	}

	public boolean isFirstInit() {
		return firstInit;
	}

	public World getWorld() {
		return world;
	}

}
