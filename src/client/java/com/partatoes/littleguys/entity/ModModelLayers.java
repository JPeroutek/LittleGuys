package com.partatoes.littleguys.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.DyeColor;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModModelLayers {
    public static final EntityModelLayer LITTLEGUY = new EntityModelLayer(ModEntities.LITTLEGUY_ID, "main");
    public static final EntityModelLayer LITTLEHORSE = new EntityModelLayer(ModEntities.LITTLEHORSE_ID, "main");
    public static final BiMap<DyeColor, EntityModelLayer> COLOR_LITTLEGUY_MODEL_LAYERS = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                    (color) -> color,
                    (color) -> new EntityModelLayer(ModEntities.COLOR_LITTLEGUY_IDS_BIMAP.get(color), "main"),
                    (a,b) -> a,
                    HashBiMap::create));
}
