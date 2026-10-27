package com.partatoes.littleguys.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.partatoes.littleguys.LittleGuys;
import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public class ModEntities {
    public static final Identifier LITTLEGUY_ID = Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littleguy");
    public static final Identifier LITTLEHORSE_ID = Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littlehorse");

    public static final EntityType<LittleGuyEntity> LITTLEGUY_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            LITTLEGUY_ID,
            EntityType.Builder.of(LittleGuyEntity::new, MobCategory.MISC)
                    .sized(.3f, .6f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, LITTLEGUY_ID)));
    public static final EntityType<LittleHorseEntity> LITTLEHORSE_ENTITY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            LITTLEHORSE_ID,
            EntityType.Builder.of(LittleHorseEntity::new, MobCategory.MISC)
                    .sized(.3f, .4f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, LITTLEHORSE_ID)));
    public static final BiMap<DyeColor, Identifier> COLOR_LITTLEGUY_IDS_BIMAP = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                    (color) -> color,
                    (color) -> Identifier.fromNamespaceAndPath(LittleGuys.MOD_ID, "littleguy_entity_" + color.toString()),
                    (a,b) -> a,
                    HashBiMap::create));
    public static final BiMap<DyeColor, EntityType<LittleGuyEntity>> COLOR_LITTLEGUY_BIMAP = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                    (color) -> color,
                    (color) -> {
                        return Registry.register(
                                BuiltInRegistries.ENTITY_TYPE,
                                COLOR_LITTLEGUY_IDS_BIMAP.get(color),
                                EntityType.Builder.of(
                                        (EntityType<LittleGuyEntity> t, Level w) -> {
                                            LittleGuyEntity lg = new LittleGuyEntity(t, w);
                                            lg.setColor(color);
                                            return lg;
                                        }, MobCategory.MISC)
                                    .sized(.3f, .6f)
                                    .build(ResourceKey.create(Registries.ENTITY_TYPE, COLOR_LITTLEGUY_IDS_BIMAP.get(color))));
                    },
                    (a,b) -> a,
                    HashBiMap::create));

    public static void registerModEntities() {
        LittleGuys.LOGGER.info("Registering ModEntities for " + LittleGuys.MOD_ID);
    }
}
