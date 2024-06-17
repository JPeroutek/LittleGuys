package com.partatoes.littleguys.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModEntities {
    public static final Identifier LITTLEGUY_ID = Identifier.of(LittleGuys.MOD_ID, "littleguy");
    public static final Identifier LITTLEHORSE_ID = Identifier.of(LittleGuys.MOD_ID, "littlehorse");

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
    public static final BiMap<DyeColor, Identifier> COLOR_LITTLEGUY_IDS_BIMAP = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                    (color) -> color,
                    (color) -> Identifier.of(LittleGuys.MOD_ID, "littleguy_entity_" + color.toString()),
                    (a,b) -> a,
                    HashBiMap::create));
    public static final BiMap<DyeColor, EntityType<LittleGuyEntity>> COLOR_LITTLEGUY_BIMAP = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                    (color) -> color,
                    (color) -> {
                        return Registry.register(
                                Registries.ENTITY_TYPE,
                                COLOR_LITTLEGUY_IDS_BIMAP.get(color),
                                EntityType.Builder.create(
                                        (EntityType<LittleGuyEntity> t, World w) -> {
                                            LittleGuyEntity lg = new LittleGuyEntity(t, w);
                                            lg.setColor(color);
                                            return lg;
                                        }, SpawnGroup.MISC)
                                    .dimensions(.3f, .6f)
                                    .build());
                    },
                    (a,b) -> a,
                    HashBiMap::create));

    public static void registerModEntities() {
        LittleGuys.LOGGER.info("Registering ModEntities for " + LittleGuys.MOD_ID);
    }
}
