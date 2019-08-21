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
package forestry.mail;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import forestry.api.mail.IMailAddress;
import forestry.api.mail.PostManager;
import forestry.core.utils.NetworkUtil;
import forestry.mail.gui.GuiMailboxInfo;
import forestry.mail.network.packets.PacketPOBoxInfoResponse;

public class EventHandlerMailAlert {
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.END &&
				Minecraft.getInstance().world != null &&
				GuiMailboxInfo.instance.hasPOBoxInfo()) {
			GuiMailboxInfo.instance.render();
		}
	}

	@SubscribeEvent
	public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();

		IMailAddress address = PostManager.postRegistry.getMailAddress(player.getGameProfile());
		//TODO I have no idea if this cast is safe. I hope that it is.
		POBox pobox = PostRegistry.getOrCreatePOBox((ServerWorld) player.world, address);
		PacketPOBoxInfoResponse packet = new PacketPOBoxInfoResponse(pobox.getPOBoxInfo());
		NetworkUtil.sendToPlayer(packet, player);
	}
}
