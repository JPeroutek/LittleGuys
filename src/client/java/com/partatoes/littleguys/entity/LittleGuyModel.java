package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class LittleGuyModel<T extends LittleGuyEntityRenderState> extends HumanoidModel<T> {
    public LittleGuyModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        return LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, false), 16, 16);
    }

}
