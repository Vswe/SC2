package vswe.stevesvehicles.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import vswe.stevesvehicles.buoy.EntityBuoy;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.client.gui.ColorHelper;
import vswe.stevesvehicles.container.ContainerBuoy;
import vswe.stevesvehicles.localization.ILocalizedText;
import vswe.stevesvehicles.localization.PlainText;
import vswe.stevesvehicles.module.data.ModuleDataPair;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class GuiBuoy extends GuiBase {
    private EntityBuoy selected;
    private EntityBuoy buoy;
    private long lastUpdateTime;
    private static final int REFRESH_DELAY = 3000;
    private List<MapElement> mapElements = new ArrayList<MapElement>();
    private List<ListElement> listElements = new ArrayList<ListElement>();
    private int scroll;
    private boolean isScrolling;

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
        }else{
            for (MapElement element : mapElements) {
                element.target = null;
            }
            for (Iterator<ListElement> iterator = listElements.iterator(); iterator.hasNext(); ) {
                ListElement listElement = iterator.next();
                if (!listElement.isVisible()) {
                    iterator.remove();
                }
            }
        }
        if (selected != null && selected.isDead) {
            selected = null;
        }
    }

    private void reloadElements() {
        mapElements.clear();
        listElements.clear();

        List list = buoy.worldObj.getEntitiesWithinAABBExcludingEntity(buoy, AxisAlignedBB.getBoundingBox(
                buoy.posX - VISIBLE_BLOCKS / 2, buoy.posY - VISIBLE_Y_OFFSET / 2, buoy.posZ - VISIBLE_BLOCKS / 2,
                buoy.posX + VISIBLE_BLOCKS / 2, buoy.posY + VISIBLE_Y_OFFSET / 2, buoy.posZ + VISIBLE_BLOCKS / 2
        ));

        mapElements.add(createBuoyElement(buoy, true));
        for (Object obj : list) {
            if (((Entity)obj).isDead) continue;

            if (obj instanceof EntityBuoy) {
                mapElements.add(createBuoyElement((EntityBuoy) obj, false));
                listElements.add(new ListElement((EntityBuoy) obj));
            }else if(obj instanceof EntityBoat) {
                mapElements.add(createBoatElement((EntityBoat) obj));
            }
        }


        Collections.sort(listElements);

        lastUpdateTime = Minecraft.getSystemTime();
    }

    @Override
    public void drawGuiForeground(int x, int y) {
        //getFontRenderer().drawString(buoy.getBuoyType().getName(), 8, 5, 0x404040);
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

        for (MapElement element : mapElements) {
            if (element.isVisible()) {
                int[] target = element.getTarget();
                int color = element.c;
                float multiplier = 0.6F;
                if (inRect(x, y, MAP_TARGET) && inRect(x, y, target)) {
                    multiplier = 0.8F;
                    info = element.info;
                }

                if (element.entity.equals(selected)) {
                    multiplier = 1;
                }

                applyColor(color, multiplier);
                drawTexturedModalRect(left + target[0], top + target[1], element.u, element.v, target[2], target[3]);
            }
        }
        GL11.glColor4f(1, 1, 1, 1);
        stopScissor();


        if (listElements.size() <= VISIBLE_LIST_ITEMS) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 0.5F);
            scroll = 0;
        }
        drawTexturedModalRect(left + SCROLL_X, top + LIST_Y + scroll, SCROLL_BAR_SRC_X, ELEMENT_SRC_Y, SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_BLEND);


        int start = getScrollStart();
        int end = Math.min(start + VISIBLE_LIST_ITEMS, listElements.size());
        for (int i = start; i < end; i++) {
            ListElement element = listElements.get(i);
            int[] target = getListElement(i - start);

            int textureId = element.isSelected() ? 2 : inRect(x, y, target) ? 1 : 0;

            drawTexturedModalRect(left + target[0], top + target[1], LIST_ELEMENT_SRC_X, LIST_ELEMENT_SRC_Y + textureId * (target[3] + 1), target[2], target[3]);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, element.item, left + target[0], top + target[1] + 1);

            getFontRenderer().drawString(element.distance + "m " + element.direction.translate(), left + target[0] + 20, top + target[1] + 6, 0x404040);
            ResourceHelper.bindResource(TEXTURE);
            GL11.glColor4f(1, 1, 1, 1);
        }

        if (info != null) {
            drawMouseOver(info.getInfo(), x + getGuiLeft(), y + getGuiTop());
        }
    }

    private RenderItem renderItem = new RenderItem();

    private int[] getListElement(int i) {
        return new int[] {LIST_X, LIST_Y + i * LIST_ELEMENT_HEIGHT, LIST_ELEMENT_WIDTH, LIST_ELEMENT_HEIGHT};
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



    private static final int LIST_X = 10;
    private static final int LIST_Y = 15;
    private static final int VISIBLE_LIST_ITEMS = 8;
    private static final int LIST_ELEMENT_SRC_X = 1;
    private static final int LIST_ELEMENT_SRC_Y = 187;
    private static final int LIST_ELEMENT_WIDTH = 73;
    private static final int LIST_ELEMENT_HEIGHT = 18;

    private static final int SCROLL_HEIGHT = 144;
    private static final int SCROLL_BAR_WIDTH = 7;
    private static final int SCROLL_BAR_HEIGHT = 11;
    private static final int SCROLL_BAR_SRC_X = 1;
    private static final int SCROLL_X = 86;
    private static final int[] SCROLL_TARGET = {SCROLL_X, LIST_Y, SCROLL_BAR_WIDTH, SCROLL_HEIGHT};

    private static final int MAP_X = 108;
    private static final int MAP_Y = 18;
    private static final int MAP_SIZE = 138;
    private static final int[] MAP_TARGET = {MAP_X, MAP_Y, MAP_SIZE, MAP_SIZE};

    private class MapElement {
        private Entity entity;
        private int[] target;

        private boolean isVisible() {
            return entity != null && !entity.isDead;
        }

        private int[] getTarget() {
            if (target == null) {
                int x = (int)((0.5 + (entity.posX - buoy.posX) / VISIBLE_BLOCKS) * MAP_SIZE);
                int z = (int)((0.5 + (entity.posZ - buoy.posZ) / VISIBLE_BLOCKS) * MAP_SIZE);

                target = new int[] {MAP_X + x - w / 2, MAP_Y + z - h / 2, w, h};
            }

            return target;
        }

        private int w;
        private int h;
        private int u;
        private int v;
        private int c;
        private IInfoText info;

        private MapElement(Entity entity, int u, int v, int w, int h, int c, IInfoText info) {
            this.entity = entity;
            this.w = w;
            this.h = h;
            this.u = u;
            this.v = v;
            this.c = c;
            this.info = info;
        }
    }

    //TODO localize
    private static final ILocalizedText[] DIRECTIONS = {
        new PlainText("W"),
        new PlainText("NW"),
        new PlainText("N"),
        new PlainText("NE"),
        new PlainText("E"),
        new PlainText("SE"),
        new PlainText("S"),
        new PlainText("SW")
    };

    private class ListElement implements Comparable<ListElement> {
        private EntityBuoy buoy;
        private String name;
        private ItemStack item;
        private int distance;
        private ILocalizedText direction;
        private boolean isVisible() {
            return buoy != null && !buoy.isDead;
        }
        private ListElement(EntityBuoy buoy) {
            this.buoy = buoy;
            item = buoy.getBuoyItem();
            this.name = buoy.getBuoyType().getName();
            distance = (int)GuiBuoy.this.buoy.getDistanceToEntity(this.buoy);
            double differenceX = this.buoy.posX - GuiBuoy.this.buoy.posX;
            double differenceZ = this.buoy.posZ - GuiBuoy.this.buoy.posZ;
            double angle = Math.atan2(differenceZ, differenceX);
            direction = DIRECTIONS[(int)Math.round(4 * (angle + Math.PI) / Math.PI) % 8];
        }

        @Override
        public int compareTo(ListElement other) {
            return ((Integer)distance).compareTo(other.distance);
        }

        private boolean isSelected() {
            return buoy.equals(selected);
        }
    }


    private MapElement createBuoyElement(final EntityBuoy buoy, final boolean owner) {
        return new MapElement(
                buoy,
                owner ? SELECTED_BUOY_SRC_X : BUOY_SRC_X,
                ELEMENT_SRC_Y,
                owner ? SELECTED_BUOY_SIZE : BUOY_SIZE,
                owner ? SELECTED_BUOY_SIZE : BUOY_SIZE,
                buoy.getBuoyType().getColor(),
                new IInfoText() {
                    @Override
                    public List<String> getInfo() {
                        List<String> info = new ArrayList<String>();
                        info.add(buoy.getBuoyType().getName());
                        if (owner) {
                            info.add(ColorHelper.YELLOW + "Owner");    //TODO localize
                        }else if(buoy.equals(selected)) {
                            info.add(ColorHelper.GREEN + "Selected"); //TODO localize
                        }
                        return info;
                    }
                }
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


        return new MapElement(boat, BOAT_SRC_X, ELEMENT_SRC_Y, BOAT_SIZE, BOAT_SIZE, color, info);
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

    @Override
    public void mouseClick(int x, int y, int button) {
        x -= getGuiLeft();
        y -= getGuiTop();
        if (inRect(x, y, SCROLL_TARGET)) {
            isScrolling = true;
            updateScrolling(y);
            return;
        }

        if (inRect(x, y, MAP_TARGET)) {
            for (MapElement mapElement : mapElements) {
                if (inRect(x, y, mapElement.target) && mapElement.entity instanceof EntityBuoy && !mapElement.entity.equals(buoy)) {
                    EntityBuoy buoy = (EntityBuoy)mapElement.entity;
                    selected = buoy.equals(selected) ? null : buoy;
                    return;
                }
            }
        }

        int start = getScrollStart();
        int end = Math.min(start + VISIBLE_LIST_ITEMS, listElements.size());
        for (int i = start; i < end; i++) {
            ListElement element = listElements.get(i);
            int[] target = getListElement(i - start);
            if (inRect(x, y, target)) {
                selected = element.buoy.equals(selected) ? null : element.buoy;
                return;
            }
        }
    }

    private void updateScrolling(int y) {
        scroll = y - LIST_Y;
        if (scroll < 0) {
            scroll = 0;
        }else if(scroll > SCROLL_HEIGHT - SCROLL_BAR_HEIGHT) {
            scroll = SCROLL_HEIGHT - SCROLL_BAR_HEIGHT;
        }
    }

    @Override
    public void mouseMoved(int x, int y, int button) {
        if (isScrolling) {
            updateScrolling(y - getGuiTop());
        }

        if (button == -1) {
            isScrolling = false;
        }
    }

    private int getScrollStart() {
        float part = (float)scroll / (SCROLL_HEIGHT - SCROLL_BAR_HEIGHT);
        return listElements.size() > VISIBLE_LIST_ITEMS ? Math.round((listElements.size() - VISIBLE_LIST_ITEMS) * part) : 0;
    }

}
