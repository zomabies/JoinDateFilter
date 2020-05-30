package Xray_Doc.JoinDateFilter;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class JoinDateData {

	public static final Path MC_DIR = Minecraft.getMinecraft().mcDataDir.toPath();
	public static final Path JDF_ROOT = MC_DIR.resolve("JoinDateFilter");
	public static final Path DATES_TXT = JDF_ROOT.resolve("dates.txt");
	public static final Path WHITELIST = JDF_ROOT.resolve("whitelist.txt");

	private JoinDateData() {
	}

	/**
	 * Create required data files
	 */
	static void initializeFile() {

		if (Files.exists(JDF_ROOT) && Files.exists(DATES_TXT) && Files.exists(WHITELIST)) {
			return;
		}
		try {
			Files.createDirectory(JDF_ROOT);
			Files.createFile(DATES_TXT);
			Files.createFile(WHITELIST);
		} catch (IOException e) {
			Main.logger.error("Error creating join date filter files", e);
		}
	}

	/**
	 * Add a new player into the player join date file
	 *
	 * @param player New player
	 * @param date   Join date of the player
	 */
	static void newDateAppend(String player, String date) throws IOException {

		FileWriter fw = new FileWriter(DATES_TXT.toFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.print(player + "\n" + date + "\n");
		out.close();
		//wait(3000); //prevent getting kicked for spam (was causing errors?)
	}

	/**
	 * Finds the player join date.
	 *
	 * @param player Player joindate to search
	 * @return 0 if not exists, 1 is newer date, 2 is older date
	 */
	static int findJoinDate(String player) throws IOException {

		BufferedReader joinDateReader = new BufferedReader(new FileReader(DATES_TXT.toFile()));
		BufferedReader whitelistReader = new BufferedReader(new FileReader(WHITELIST.toFile()));

		String line;
		String line2;
		boolean filter;

		while ((line2 = whitelistReader.readLine()) != null) {
			if (line2.equalsIgnoreCase(player)) {
				whitelistReader.close();
				joinDateReader.close();
				return 2; // shown
			}
		}
		whitelistReader.close();

		while ((line = joinDateReader.readLine()) != null) {
			if (line.equalsIgnoreCase(player)) {

				String date = joinDateReader.readLine();
				filter = compareJoinDate(date);

				joinDateReader.close();
				if (filter) {
					return 1; // hidden
				} else {
					return 2; // shown
				}
			}
		}
		joinDateReader.close();

		Minecraft.getMinecraft().player.sendChatMessage("/joindate " + player);

		return 0;
	}

	/**
	 * Matches if the join date is greater than cutoff date
	 */
	static boolean compareJoinDate(String date) {
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		int day = Integer.parseInt(date.substring(8, 10));

		int cutoff_year = config.cutoff_year;
		int cutoff_month = config.cutoff_month;
		int cutoff_day = config.cutoff_day;

		if (year >= cutoff_year && month >= cutoff_month && day >= cutoff_day) //fuck you aholic
			return true;

		return false;
	}
}
