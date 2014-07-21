package vswe.stevesvehicles.client.rendering.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.buoy.EntityBuoy;

import java.util.ArrayList;
import java.util.List;


public class ModelBuoy extends ModelBase {

    private List<ModelRenderer> parts = new ArrayList<ModelRenderer>();
    private ModelRenderer lamp;

    private static final int BASE_COUNT = 8;
    private static final int PIN_COUNT = 5;
    private static final int HEAD_PIN_OFFSET = 3;

    public ModelBuoy() {
        for (int i = 0;  i < BASE_COUNT; i++) {
            ModelRenderer base = new ModelRenderer(this, 0, 0);

            base.addBox(
                    -3,
                    -3.5F,
                    1.3F,
                    6,
                    7,
                    6
            );
            base.setRotationPoint(
                    0,
                    0.001F * (i - BASE_COUNT / 2),
                    0
            );


            base.rotateAngleY = i * 2 * (float)Math.PI / BASE_COUNT;
            parts.add(base);
        }

        for (int i = 0;  i < PIN_COUNT; i++) {
            ModelRenderer pin = new ModelRenderer(this, 0, 13);

            pin.addBox(
                    -1,
                    0F,
                    2.4F,
                    2,
                    17,
                    2
            );
            pin.setRotationPoint(
                    0,
                    -18.5F,
                    0
            );


            pin.rotateAngleY = i * 2 * (float)Math.PI / PIN_COUNT;
            pin.rotateAngleX = (float)Math.PI / 20;
            parts.add(pin);
        }

        ModelRenderer headBot = new ModelRenderer(this, 24, 0);

        headBot.addBox(
                -4,
                -1,
                -4,
                8,
                2,
                8
        );
        headBot.setRotationPoint(
                0,
                -19.5F,
                0
        );
        parts.add(headBot);


        for (int x = -1; x <= 1; x += 2) {
            for (int z = -1; z <= 1; z += 2) {

                ModelRenderer headPin = new ModelRenderer(this, 8, 13);

                headPin.addBox(
                        -0.5F,
                        -3,
                        -0.5F,
                        1,
                        6,
                        1
                );
                headPin.setRotationPoint(
                        x * HEAD_PIN_OFFSET,
                        -23.5F,
                        z * HEAD_PIN_OFFSET
                );
                parts.add(headPin);

            }
        }

        ModelRenderer headTop = new ModelRenderer(this, 24, 0);

        headTop.addBox(
                -4,
                -1,
                -4,
                8,
                2,
                8
        );
        headTop.setRotationPoint(
                0,
                -27.5F,
                0
        );
        headTop.rotateAngleX = (float)Math.PI;
        parts.add(headTop);


        ModelRenderer hat = new ModelRenderer(this, 32, 10);

        hat.addBox(
                -2.5F,
                -1,
                -2.5F,
                5,
                2,
                5
        );
        hat.setRotationPoint(
                0,
                -29.5F,
                0
        );
        parts.add(hat);


        ModelRenderer hatPin = new ModelRenderer(this, 8, 20);

        hatPin.addBox(
                -0.5F,
                -1,
                -0.5F,
                1,
                2,
                1
        );
        hatPin.setRotationPoint(
                0,
                -31.5F,
                0
        );
        parts.add(hatPin);


        lamp = new ModelRenderer(this, 12, 13);

        lamp.addBox(
                -2.5F,
                -3,
                -2.5F,
                5,
                6,
                5
        );
        lamp.setRotationPoint(
                0,
                -23.5F,
                0
        );

    }

    @Override
    public void render(Entity entity, float ignored1, float ignored2, float ignored3, float ignored4, float ignored5, float multiplier) {
        GL11.glColor4f(1, 1, 1, 1);
        for (ModelRenderer part : parts) {
            part.render(multiplier);
        }

        EntityBuoy buoy = (EntityBuoy)entity;
        GL11.glColor4f(buoy.getLampColor(), 0, 0, 1);
        lamp.render(multiplier);
        GL11.glColor4f(1, 1, 1, 1);
    }
}
