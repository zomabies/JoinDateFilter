package Xray_Doc.JoinDateFilter;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Main.MOD_ID, name = "JDF_Config", type = Type.INSTANCE)
public class config {
	@Name("Mod Enable")
	public static boolean mod_enabled = true;

	@Name("Cut-Off Year")
	@RangeInt(min = 2016, max = 2100)
	public static int cutoff_year = 2016;

	@Name("Cut-Off Month")
	@RangeInt(min = 1, max = 12)
	public static int cutoff_month = 1;

	@Name("Cut-Off Day")
	@RangeInt(min = 1, max = 31)
	public static int cutoff_day = 1;

	@Config.Comment("Untested and breaking!")
	public static Experimental experimental = new Experimental();

	public static class Experimental {

		@Name("Audit Mode")
		@Config.Comment("Disables filter only, while collecting joindates.")
		public boolean auditMode = false;

		@Name("Debug Mode")
		@Config.Comment("Show added player in chat. Will CAUSE lots of output.\n" +
				"Only works on new filter function.")
		public boolean debugMode = false;

		@Name("New Filter Function")
		@Config.Comment("This REPLACES the original filter function. " +
				"Will not responsible for any effects caused by it.")
		public boolean newFilter = false;
	}

	@Mod.EventBusSubscriber(modid = Main.MOD_ID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Main.MOD_ID)) {
				ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}
