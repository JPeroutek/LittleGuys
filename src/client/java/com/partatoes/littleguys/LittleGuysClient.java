package com.partatoes.littleguys;

import com.partatoes.littleguys.entity.*;
import com.partatoes.littleguys.item.ModColorProviders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class LittleGuysClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		EntityRendererRegistry.register(ModEntities.LITTLEGUY_ENTITY, LittleGuyRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.LITTLEGUY, LittleGuyModel::getTexturedModelData);

		EntityRendererRegistry.register(ModEntities.LITTLEHORSE_ENTITY, LittleHorseRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.LITTLEHORSE, LittleHorseModel::getTexturedModelData);

		ModColorProviders.registerColorProviders();
	}
}