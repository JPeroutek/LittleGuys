package com.partatoes.littleguys.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.DyeColor;

public class ModModelLayers {
    public static final ModelLayerLocation LITTLEGUY = new ModelLayerLocation(ModEntities.LITTLEGUY_ID, "main");
    public static final ModelLayerLocation LITTLEHORSE = new ModelLayerLocation(ModEntities.LITTLEHORSE_ID, "main");
    public static final BiMap<DyeColor, ModelLayerLocation> COLOR_LITTLEGUY_MODEL_LAYERS = Stream.of(DyeColor.values())
            .collect(Collectors.toMap(
                    (color) -> color,
                    (color) -> new ModelLayerLocation(ModEntities.COLOR_LITTLEGUY_IDS_BIMAP.get(color), "main"),
                    (a,b) -> a,
                    HashBiMap::create));
}
