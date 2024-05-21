package com.partatoes.littleguys;

import com.partatoes.littleguys.entity.*;
import com.partatoes.littleguys.item.ModColorProviders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.util.DyeColor;

public class LittleGuysClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		EntityRendererRegistry.register(ModEntities.LITTLEGUY_ENTITY, LittleGuyRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.LITTLEGUY, LittleGuyModel::getTexturedModelData);

		EntityRendererRegistry.register(ModEntities.LITTLEHORSE_ENTITY, LittleHorseRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.LITTLEHORSE, LittleHorseModel::getTexturedModelData);

		for (DyeColor c : DyeColor.values()) {
			EntityRendererRegistry.register(ModEntities.COLOR_LITTLEGUY_BIMAP.get(c), LittleGuyRenderer::new);
			EntityModelLayerRegistry.registerModelLayer(ModModelLayers.COLOR_LITTLEGUY_MODEL_LAYERS.get(c), LittleGuyModel::getTexturedModelData);
		}

		ModColorProviders.registerColorProviders();
	}
}