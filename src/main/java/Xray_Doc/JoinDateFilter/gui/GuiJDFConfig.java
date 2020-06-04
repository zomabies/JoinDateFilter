package Xray_Doc.JoinDateFilter.gui;

import Xray_Doc.JoinDateFilter.Main;
import Xray_Doc.JoinDateFilter.config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiJDFConfig extends GuiConfig {
	public GuiJDFConfig(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), Main.MOD_ID, false, false, "Join Date Filter", "General");
	}

	private static List<IConfigElement> getConfigElements() {
		// make CONFIG_CATEGORY at top before other property
		return new ArrayList<>(ConfigElement.from(config.class).getChildElements());
	}
}
