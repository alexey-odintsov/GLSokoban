package com.alekso.glsokoban;

public class Level {
	int id;
	String rawData;
	char data[][][];
	int bestTime;
	int isCompleted;
	int score;

	/**
	 * 
	 * @param id
	 * @param rawData
	 * @param bestTime
	 * @param bestMoves
	 * @param bestPushes
	 */
	public Level(int id, String rawData, int bestTime, int isCompleted,
			int score) {
		this.id = id;
		this.rawData = rawData;
		this.bestTime = bestTime;
		this.isCompleted = isCompleted;
		this.score = score;

		generateLevel();
	}

	/**
	 * 
	 */
	private void generateLevel() {
		String lines[] = rawData.split("\\|");
		data = new char[2][lines.length][];
		for (int i = 0; i < lines.length; i++) {
			data[1][i] = new char[lines[i].length()];
			data[1][i] = lines[i].toCharArray();
			for (int j = 0; j < data[1][i].length; j++) {
				if (data[1][i][j] == '+' || data[1][i][j] == '*') {
					data[1][i][j] = ' ';
				}
			}
			data[0][i] = new char[lines[i].length()];
			data[0][i] = lines[i].toCharArray();
			for (int j = 0; j < data[0][i].length; j++) {
				if (data[0][i][j] != '+' && data[0][i][j] != '*') {
					data[0][i][j] = ' ';
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public char[][][] getLevelCopy() {
		char[][][] copy;
		copy = new char[data.length][][];
		for (int i = 0; i < data.length; i++) {
			copy[i] = new char[data[i].length][];
			for (int j = 0; j < data[i].length; j++) {
				copy[i][j] = new char[data[i][j].length];
				System.arraycopy(data[i][j], 0, copy[i][j], 0,
						data[i][j].length);
			}
		}
		return copy;

	}
}
