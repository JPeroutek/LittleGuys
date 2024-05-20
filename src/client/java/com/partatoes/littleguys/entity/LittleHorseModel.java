package com.partatoes.littleguys.entity;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class LittleHorseModel<T extends LittleHorseEntity> extends HorseEntityModel<T> {
    public LittleHorseModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(HorseEntityModel.getModelData(Dilation.NONE), 16, 16);
    }
}
