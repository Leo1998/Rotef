package com.rotef.game.util.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DirectFileAccess {

	private RandomAccessFile file;

	public DirectFileAccess(FileHandle fileHandle) {
		try {
			this.file = new RandomAccessFile(fileHandle.file(), "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String readString(int maxLength) throws DFAException {
		try {
			char[] s = new char[maxLength];
			for (int i = 0; i < s.length; i++) {
				s[i] = file.readChar();
			}
			return new String(s).replace('\0', ' ');
		} catch (IOException e) {
			throw new DFAException();
		}
	}

	public void writeString(String s, int maxLength) throws DFAException {
		try {
			StringBuffer buffer = null;
			if (s != null) {
				buffer = new StringBuffer(s);
			} else {
				buffer = new StringBuffer(maxLength);
			}
			buffer.setLength(maxLength);
			file.writeChars(buffer.toString());
		} catch (IOException e) {
			throw new DFAException();
		}
	}

	public int readInt() throws DFAException {
		try {
			return file.readInt();
		} catch (IOException e) {
			throw new DFAException();
		}
	}

	public void writeInt(int v) throws DFAException {
		try {
			file.writeInt(v);
		} catch (IOException e) {
			throw new DFAException();
		}
	}

	public void seek(long pos) {
		try {
			file.seek(pos);
		} catch (IOException e) {
			Gdx.app.error("DirectFileAccess", "I/O Error occured!");
		}
	}

	public long length() {
		try {
			return file.length();
		} catch (IOException e) {
			Gdx.app.error("DirectFileAccess", "I/O Error occured!");
			return -1;
		}
	}

	public void close() {
		try {
			file.close();
		} catch (IOException e) {
			Gdx.app.error("DirectFileAccess", "I/O Error occured!");
		}
	}

}
