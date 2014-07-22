package vswe.stevesvehicles.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.buoy.EntityBuoy;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.gui.ColorHelper;
import vswe.stevesvehicles.container.ContainerBuoy;
import vswe.stevesvehicles.module.data.ModuleDataPair;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GuiBuoy extends GuiBase {
    private EntityBuoy buoy;
    private long lastUpdateTime;
    private static final int REFRESH_DELAY = 500;
    private List<MapElement> elements = new ArrayList<MapElement>();

    private static final int VISIBLE_BLOCKS = 100; //TODO let the player zoom
    private static final int VISIBLE_Y_OFFSET = 20;

    public GuiBuoy(EntityBuoy entityBuoy) {
        super(new ContainerBuoy(entityBuoy));
        this.buoy = entityBuoy;
        setXSize(256);
        setYSize(174);
        reloadElements();
    }

    private void reloadElementsIfNecessary() {
        if (Minecraft.getSystemTime() - lastUpdateTime >= REFRESH_DELAY) {
            reloadElements();
        }
    }

    private void reloadElements() {
        elements.clear();

        List list = buoy.worldObj.getEntitiesWithinAABBExcludingEntity(buoy, AxisAlignedBB.getBoundingBox(
                buoy.posX - VISIBLE_BLOCKS / 2, buoy.posY - VISIBLE_Y_OFFSET / 2, buoy.posZ - VISIBLE_BLOCKS / 2,
                buoy.posX + VISIBLE_BLOCKS / 2, buoy.posY + VISIBLE_Y_OFFSET / 2, buoy.posZ + VISIBLE_BLOCKS / 2
        ));

        elements.add(createBuoyElement(buoy, true));
        for (Object obj : list) {

            if (obj instanceof EntityBuoy) {
                elements.add(createBuoyElement((EntityBuoy)obj, false));
            }else if(obj instanceof EntityBoat) {
                elements.add(createBoatElement((EntityBoat)obj));
            }
        }


        lastUpdateTime = Minecraft.getSystemTime();
    }

    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/buoy.png");
    @Override
    public void drawGuiBackground(float f, int x, int y) {
        reloadElementsIfNecessary();

        GL11.glColor4f(1, 1, 1, 1);


        int left = getGuiLeft();
        int top = getGuiTop();
        ResourceHelper.bindResource(TEXTURE);
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        x -= getGuiLeft();
        y -= getGuiTop();

        IInfoText info = null;
        setupScissor(MAP_X, MAP_Y, MAP_SIZE, MAP_SIZE);
        startScissor();
        GL11.glEnable(GL11.GL_BLEND);
        for (MapElement element : elements) {
            int color = element.c;
            int alpha = 0xFF;
            if (inRect(x, y, MAP_TARGET) && inRect(x, y, element.target)) {
                alpha = 0xAA;
                info = element.info;
            }
            color |= alpha << 24;
            applyColor(color);
            drawTexturedModalRect(left + element.target[0], top + element.target[1], element.u, element.v, element.target[2], element.target[3]);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
        stopScissor();

        if (info != null) {
            drawMouseOver(info.getInfo(), x + getGuiLeft(), y + getGuiTop());
        }
    }

    private static final int MAP_X = 108;
    private static final int MAP_Y = 18;
    private static final int MAP_SIZE = 138;
    private static final int[] MAP_TARGET = {MAP_Y, MAP_Y, MAP_SIZE, MAP_SIZE};

    private class MapElement {
        private int[] target;
        private int u;
        private int v;
        private int c;
        private IInfoText info;
    }

    private static final int ELEMENT_SRC_Y = 175;

    private static final int SELECTED_BUOY_SIZE = 7;
    private static final int SELECTED_BUOY_SRC_X = 9;

    private static final int BUOY_SIZE = 5;
    private static final int BUOY_SRC_X = 17;

    private static final int BOAT_SIZE = 3;
    private static final int BOAT_SRC_X = 23;

    private static final int MODULAR_BOAT_COLOR = 0xFFFFFF;
    private static final int VANILLA_BOAT_COLOR = 0xA67C52;

    private MapElement createBuoyElement(final EntityBuoy buoy, boolean selected) {
        return createElement(
                buoy,
                selected ? SELECTED_BUOY_SRC_X : BUOY_SRC_X,
                ELEMENT_SRC_Y,
                selected ? SELECTED_BUOY_SIZE : BUOY_SIZE,
                selected ? SELECTED_BUOY_SIZE : BUOY_SIZE,
                buoy.getBuoyType().getColor(),
                new StaticText(buoy.getBuoyType().getName() + (selected ? "\n" + ColorHelper.YELLOW + "Selected Buoy" : "")) //TODO localize
        );
    }

    private MapElement createBoatElement(EntityBoat boat) {
        IInfoText info;
        int color;
        if (boat instanceof EntityModularBoat) {
            color = MODULAR_BOAT_COLOR;
            final VehicleBase vehicle = ((EntityModularBoat)boat).getVehicle();
            info = new IInfoText() {
                @Override
                public List<String> getInfo() {
                    List<String> info = new ArrayList<String>();
                    info.add(vehicle.getVehicleName());
                    if (GuiScreen.isShiftKeyDown()) {
                        for (ModuleDataPair moduleDataPair : vehicle.getModuleCounts()) {
                            info.add(ColorHelper.GRAY + moduleDataPair.toString());
                        }
                    }else{
                        info.add(ColorHelper.GRAY + "Hold shift for more info"); //TODO localize
                    }
                    return info;
                }
            };
        }else{
            color = VANILLA_BOAT_COLOR;
            info = new StaticText("Boat"); //TODO localize
        }


        return createElement(boat, BOAT_SRC_X, ELEMENT_SRC_Y, BOAT_SIZE, BOAT_SIZE, color, info);
    }

    private MapElement createElement(Entity entity, int u, int v, int w, int h, int c, IInfoText info) {
        int x = (int)((0.5 + (entity.posX - buoy.posX) / VISIBLE_BLOCKS) * MAP_SIZE);
        int z = (int)((0.5 + (entity.posZ - buoy.posZ) / VISIBLE_BLOCKS) * MAP_SIZE);

        MapElement element = new MapElement();
        element.target = new int[] {MAP_X + x - w / 2, MAP_Y + z - h / 2, w, h};
        element.u = u;
        element.v = v;
        element.c = c;
        element.info = info;

        return element;
    }

    private interface IInfoText {
        List<String> getInfo();
    }

    private class StaticText implements IInfoText {
        private List<String> info;

        private StaticText(String text) {
            info = new ArrayList<String>();
            Collections.addAll(info, text.split("\n"));
        }

        @Override
        public List<String> getInfo() {
            return info;
        }
    }
}
