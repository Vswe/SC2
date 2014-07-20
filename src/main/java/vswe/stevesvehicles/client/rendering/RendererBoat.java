package vswe.stevesvehicles.client.rendering;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.MathHelper;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityBoatBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;


public class RendererBoat extends RendererVehicle {
    @Override
    protected void applyMatrixUpdates(VehicleBase vehicle, MatrixObject matrix, float partialTickTime) {
        EntityModularBoat boat = (EntityModularBoat)vehicle.getEntity();

        float damageTime = boat.getTimeSinceHit() - partialTickTime;
        float damage = boat.getDamageTaken() - partialTickTime;

        if (damage < 0) {
            damage = 0;
        }

        if (damageTime > 0) {
            matrix.roll = MathHelper.sin(damageTime) * damageTime * damage / 10.0F * boat.getForwardDirection();
        }

        matrix.yaw = 180 - matrix.yaw;

    }
}
