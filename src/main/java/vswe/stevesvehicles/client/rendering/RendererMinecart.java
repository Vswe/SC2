package vswe.stevesvehicles.client.rendering;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import vswe.stevesvehicles.vehicles.VehicleBase;
import vswe.stevesvehicles.vehicles.entities.EntityModularCart;


public class RendererMinecart extends RendererVehicle {
    @Override
    protected void applyMatrixUpdates(VehicleBase vehicle, MatrixObject matrix, float partialTickTime) {
        EntityModularCart cart = (EntityModularCart)vehicle.getEntity();
        //calculate the current positions and the current pitch(since the cart should still be rendered between ticks)
        double partialPosX = cart.lastTickPosX + (cart.posX - cart.lastTickPosX) * (double)partialTickTime;
        double partialPosY = cart.lastTickPosY + (cart.posY - cart.lastTickPosY) * (double)partialTickTime;
        double partialPosZ = cart.lastTickPosZ + (cart.posZ - cart.lastTickPosZ) * (double)partialTickTime;
        matrix.pitch = cart.prevRotationPitch + (cart.rotationPitch - cart.prevRotationPitch) * partialTickTime;

		/*Vec3 rotations = engine.getRenderRotation((float)partialPosX,(float)partialPosY,(float)partialPosZ,partialTickTime);
		if (rotations != null) {
			yaw = (float)rotations.xCoord * 180F / (float)Math.PI;
		}*/

        Vec3 posFromRail = cart.func_70489_a(partialPosX, partialPosY, partialPosZ);

        //if cart is on a rail the yaw and the pitch should be calculated accordingly(instead of just use given values)
        if (posFromRail != null && cart.canUseRail())
        {
            //predict the last and next position of the cart with the given prediction time span
            double predictionLength = 0.30000001192092896D;
            Vec3 lastPos = cart.func_70495_a(partialPosX, partialPosY, partialPosZ, predictionLength);
            Vec3 nextPos = cart.func_70495_a(partialPosX, partialPosY, partialPosZ, -predictionLength);

            //if the last pos wasn't on the rail
            if (lastPos == null)
            {
                lastPos = posFromRail;
            }

            //if the next pos won't be on the rail
            if (nextPos == null)
            {
                nextPos = posFromRail;
            }

            //fix the coordinates accordingly to the rail
            matrix.x += posFromRail.xCoord - partialPosX;
            matrix.y += (lastPos.yCoord + nextPos.yCoord) / 2.0D - partialPosY;
            matrix.z += posFromRail.zCoord - partialPosZ;

            //get the difference beetween the next and the last pos
            Vec3 difference = nextPos.addVector(-lastPos.xCoord, -lastPos.yCoord, -lastPos.zCoord);

            //if there exist any difference
            if (difference.lengthVector() != 0.0D)
            {
                difference = difference.normalize();

                //calculate the yaw and the pitch
                matrix.yaw = (float)(Math.atan2(difference.zCoord, difference.xCoord) * 180.0D / Math.PI);
                matrix.pitch = (float)(Math.atan(difference.yCoord) * 73.0D);
            }
        }

        matrix.yaw = 180F - matrix.yaw;
        matrix.pitch *= -1;

        //calculate and apply the rotation caused by the cart being damaged
        float damageRot = (float)cart.getRollingAmplitude() - partialTickTime;
        float damageTime = (float)cart.getDamage() - partialTickTime;
        float damageDir = (float)cart.getRollingDirection();

        if (damageTime < 0.0F)
        {
            damageTime = 0.0F;
        }

        matrix.flip = (cart.motionX > 0) != (cart.motionZ > 0);
        if (cart.cornerFlip) {
            matrix.flip = !matrix.flip;
        }

        if (cart.getRenderFlippedYaw(matrix.yaw + (matrix.flip ? 0F : 180F)) ) {
            matrix.flip = !matrix.flip;
        }

        if (damageRot > 0.0F) {
            matrix.roll = MathHelper.sin(damageRot) * damageRot * damageTime / 10.0F * damageDir;
        }
    }
}
