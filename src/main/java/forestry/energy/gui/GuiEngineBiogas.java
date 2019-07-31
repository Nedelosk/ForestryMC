/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.energy.gui;

import net.minecraft.entity.player.PlayerInventory;

import forestry.core.config.Constants;
import forestry.core.gui.widgets.TankWidget;
import forestry.energy.tiles.TileEngineBiogas;

public class GuiEngineBiogas extends GuiEngine<ContainerEngineBiogas, TileEngineBiogas> {
	public GuiEngineBiogas(PlayerInventory inventory, TileEngineBiogas tile, int windowid) {	//TODO windowid
		super(Constants.TEXTURE_PATH_GUI + "/bioengine.png", new ContainerEngineBiogas(inventory, tile, windowid), inventory, tile);
		widgetManager.add(new TankWidget(widgetManager, 89, 19, 0));
		widgetManager.add(new TankWidget(widgetManager, 107, 19, 1));

		widgetManager.add(new BiogasSlot(widgetManager, 30, 47, 2));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(var1, mouseX, mouseY);

		int temperature = tile.getOperatingTemperatureScaled(16);
		if (temperature > 16) {
			temperature = 16;
		}
		if (temperature > 0) {
			blit(guiLeft + 53, guiTop + 47 + 16 - temperature, 176, 60 + 16 - temperature, 4, temperature);
		}
	}
}