package Xray_Doc.JoinDateFilter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mod(modid = Main.MOD_ID, name = Main.NAME, version = Main.VERSION)

public class Main {

	public static final String MOD_ID = "joindatefilter";
	public static final String NAME = "Join Date Filter";
	public static final String VERSION = "@VERSION@";

	static Logger log;

	String name = null;
	String date = null;
	int playercheck = 1;
	int filter = 0;
	int server = 0;

	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		log = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);
		JoinDateData.initializeFile();
	}

	@SubscribeEvent
	public void clientHostJoin(ClientConnectedToServerEvent event) {
		server = 0;
		//it works on client side although advertised as server only
		InetSocketAddress remoteHost = (InetSocketAddress) event.getManager().getRemoteAddress();
		String host = remoteHost.getHostString();
		if (host.equalsIgnoreCase("constantiam.net")) {
			server = 1;
		}
	}

	@SubscribeEvent
	public void clientChat(ClientChatReceivedEvent event) throws IOException {

		if (config.mod_enabled && server == 1) {

			String message = event.getMessage().getUnformattedText();
			String myName = Minecraft.getMinecraft().player.getName();

			if (message.contains("<" + myName + ">") ||
					message.contains("[P] <") ||
					(message.contains(" whispers: ") && !message.contains("<")) ||
					(message.contains("[server]") && !message.contains("<"))) {

			} else if (message.contains("<")) {
				playercheck = 0;
				int startIndex = message.indexOf("<");
				int endIndex = message.indexOf(">");
				name = message.substring(startIndex + 1, endIndex);

				filter = JoinDateData.findJoinDate(name);

				if (filter == 1) {
					playercheck = 1;
					filter = 0;
					event.setMessage(null);
				}
				if (filter == 2) {
					playercheck = 1;
				}
			} else if (message.contains("Have they joined before?")) {
				LocalDateTime currentDate = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				String strCurrentDate = currentDate.format(formatter);

				JoinDateData.newDateAppend(name, strCurrentDate);
				playercheck = 1;
				event.setMessage(null);
			} else if (message.contains("Player: ") && playercheck == 0) {
				int index = message.lastIndexOf(":");
				name = message.substring(index + 2);
				event.setMessage(null);
			} else if (message.contains("First Joined: ") && playercheck == 0) {
				int index = message.indexOf(":");
				date = message.substring(index + 2, index + 12);
				event.setMessage(null);
				JoinDateData.newDateAppend(name, date);
			} else if (message.contains("Last Seen: ") && playercheck == 0) {
				playercheck = 1;
				event.setMessage(null);
			}
		}
	}
}
