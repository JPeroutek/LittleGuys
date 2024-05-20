package com.partatoes.littleguys;

import com.partatoes.littleguys.entity.ModEntities;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import com.partatoes.littleguys.item.ModItemGroups;
import com.partatoes.littleguys.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LittleGuys implements ModInitializer {
	public static final String MOD_ID = "little-guys";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("little-guys");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();

		FabricDefaultAttributeRegistry.register(ModEntities.LITTLEGUY_ENTITY, LittleGuyEntity.createLittleGuyAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.LITTLEHORSE_ENTITY, LittleHorseEntity.createLittleHorseAttributes());
		ModEntities.registerModEntities();
	}
}