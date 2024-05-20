package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final Identifier LITTLEGUY_ID = new Identifier(LittleGuys.MOD_ID, "littleguy");
    public static final Identifier LITTLEHORSE_ID = new Identifier(LittleGuys.MOD_ID, "littlehorse");

    public static final EntityType<LittleGuyEntity> LITTLEGUY_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            LITTLEGUY_ID,
            EntityType.Builder.create(LittleGuyEntity::new, SpawnGroup.MISC)
                    .dimensions(.3f, .6f)
                    .build());
    public static final EntityType<LittleHorseEntity> LITTLEHORSE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            LITTLEHORSE_ID,
            EntityType.Builder.create(LittleHorseEntity::new, SpawnGroup.MISC)
                    .dimensions(.3f, .4f)
                    .build());

    public static void registerModEntities() {
        LittleGuys.LOGGER.info("Registering ModEntities for " + LittleGuys.MOD_ID);
    }
}
