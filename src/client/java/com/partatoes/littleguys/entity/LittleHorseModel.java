package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class LittleHorseModel<T extends LittleHorseEntityRenderState> extends AbstractEquineModel<T> {
    public LittleHorseModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        return LayerDefinition.create(AbstractEquineModel.createBodyMesh(CubeDeformation.NONE), 16, 16);
    }
}
