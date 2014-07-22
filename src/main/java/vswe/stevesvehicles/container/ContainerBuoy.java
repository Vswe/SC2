package vswe.stevesvehicles.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import vswe.stevesvehicles.buoy.EntityBuoy;
import vswe.stevesvehicles.client.gui.screen.GuiBuoy;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.network.PacketType;
import vswe.stevesvehicles.tileentity.TileEntityBase;


public class ContainerBuoy extends ContainerBase {
    private EntityBuoy entityBuoy;

    public ContainerBuoy(EntityBuoy entityBuoy) {
        this.entityBuoy = entityBuoy;
    }

    @Override
    public IInventory getMyInventory() {
        return null;
    }

    @Override
    public TileEntityBase getTileEntity() {
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSqToEntity(entityBuoy) <= 64;
    }

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        super.addCraftingToCrafters(player);
        if (player instanceof EntityPlayer) {
            DataWriter dw = PacketHandler.getDataWriter(PacketType.BUOY);

            PacketHandler.sendPacketToPlayer(dw, (EntityPlayer)player);
        }
    }

    @SideOnly(Side.CLIENT)
    public GuiBuoy gui;

    public void receiveInfo(DataReader dr) {

    }
}
