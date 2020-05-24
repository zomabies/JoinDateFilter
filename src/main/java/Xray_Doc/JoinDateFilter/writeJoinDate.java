package Xray_Doc.JoinDateFilter;

import net.minecraft.client.Minecraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class writeJoinDate {
	public void newDateAppend(String name, String date) throws IOException, InterruptedException {
		String path = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/JoinDateFilter/dates.txt";
		File file = new File(path);

		FileWriter fw = new FileWriter(path, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.print(name + "\n" + date + "\n");
		out.close();
		//wait(3000); //prevent getting kicked for spam (was causing errors?)

		return;
	}
}
