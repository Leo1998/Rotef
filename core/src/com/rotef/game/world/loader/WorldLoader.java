package com.rotef.game.world.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
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

			kryo.register(Array.class, new Serializer<Array>() {
				{
					setAcceptsNull(true);
				}

				private Class genericType;

				@Override
				public void setGenerics(Kryo kryo, Class[] generics) {
					if (kryo.isFinal(generics[0]))
						genericType = generics[0];
				}

				@Override
				public void write(Kryo kryo, Output output, Array array) {
					int length = array.size;
					output.writeInt(length, true);
					if (length == 0) {
						genericType = null;
						return;
					}
					if (genericType != null) {
						Serializer serializer = kryo.getSerializer(genericType);
						genericType = null;
						for (Object element : array)
							kryo.writeObjectOrNull(output, element, serializer);
					} else {
						for (Object element : array)
							kryo.writeClassAndObject(output, element);
					}
				}

				@Override
				public Array read(Kryo kryo, Input input, Class<Array> type) {
					Array array = new Array();
					kryo.reference(array);
					int length = input.readInt(true);
					array.ensureCapacity(length);
					if (genericType != null) {
						Class elementClass = genericType;
						Serializer serializer = kryo.getSerializer(genericType);
						genericType = null;
						for (int i = 0; i < length; i++)
							array.add(kryo.readObjectOrNull(input, elementClass, serializer));
					} else {
						for (int i = 0; i < length; i++)
							array.add(kryo.readClassAndObject(input));
					}
					return array;
				}
			});

			kryo.register(IntArray.class, new Serializer<IntArray>() {
				{
					setAcceptsNull(true);
				}

				@Override
				public void write(Kryo kryo, Output output, IntArray array) {
					int length = array.size;
					output.writeInt(length, true);
					if (length == 0)
						return;
					for (int i = 0, n = array.size; i < n; i++)
						output.writeInt(array.get(i), true);
				}

				@Override
				public IntArray read(Kryo kryo, Input input, Class<IntArray> type) {
					IntArray array = new IntArray();
					kryo.reference(array);
					int length = input.readInt(true);
					array.ensureCapacity(length);
					for (int i = 0; i < length; i++)
						array.add(input.readInt(true));
					return array;
				}
			});

			kryo.register(FloatArray.class, new Serializer<FloatArray>() {
				{
					setAcceptsNull(true);
				}

				@Override
				public void write(Kryo kryo, Output output, FloatArray array) {
					int length = array.size;
					output.writeInt(length, true);
					if (length == 0)
						return;
					for (int i = 0, n = array.size; i < n; i++)
						output.writeFloat(array.get(i));
				}

				@Override
				public FloatArray read(Kryo kryo, Input input, Class<FloatArray> type) {
					FloatArray array = new FloatArray();
					kryo.reference(array);
					int length = input.readInt(true);
					array.ensureCapacity(length);
					for (int i = 0; i < length; i++)
						array.add(input.readFloat());
					return array;
				}
			});

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
