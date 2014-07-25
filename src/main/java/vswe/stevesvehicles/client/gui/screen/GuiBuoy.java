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
import vswe.stevesvehicles.network.DataWriter;
import vswe.stevesvehicles.network.PacketHandler;
import vswe.stevesvehicles.network.PacketType;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.vehicle.entity.EntityModularBoat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class GuiBuoy extends GuiBase {
    private EntityBuoy selected;
    private EntityBuoy buoy;

    //cached versions
    private EntityBuoy next;
    private EntityBuoy previous;

    private long lastUpdateTime;
    private static final int REFRESH_DELAY = 3000;
    private List<MapElement> mapElements = new ArrayList<MapElement>();
    private List<ListElement> listElements = new ArrayList<ListElement>();
    private List<BuoyInfo> infoPanels = new ArrayList<BuoyInfo>();
    private int scroll;
    private boolean isScrolling;

    private static final int VISIBLE_BLOCKS = 100; //TODO let the player zoom
    private static final int VISIBLE_Y_OFFSET = 20;

    public GuiBuoy(EntityBuoy entityBuoy) {
        super(new ContainerBuoy(entityBuoy));
        this.buoy = entityBuoy;
        setXSize(256);
        setYSize(256);
        reloadElements();

        //TODO localized
        infoPanels.add(new BuoyInfo(new PlainText("Active"), 8, 6) {
            @Override
            protected EntityBuoy getBuoy() {
                return buoy;
            }
        });

        infoPanels.add(new BuoyInfo(new PlainText("Selected"), 8, 40) {
            @Override
            protected EntityBuoy getBuoy() {
                return selected;
            }
        });

        infoPanels.add(new BuoyInfoProperty(new PlainText("Next"), 150, 6, true) {
            @Override
            protected EntityBuoy getBuoy() {
                return next;
            }

            @Override
            protected void setBuoy(EntityBuoy buoy) {
                next = buoy;
            }
        });

        infoPanels.add(new BuoyInfoProperty(new PlainText("Previous"), 150, 40, false) {
            @Override
            protected EntityBuoy getBuoy() {
                return previous;
            }

            @Override
            protected void setBuoy(EntityBuoy buoy) {
                previous = buoy;
            }
        });
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
        mapElements.add(createBuoyTargetElement(buoy));
        for (Object obj : list) {
            if (((Entity)obj).isDead) continue;

            if (obj instanceof EntityBuoy) {
                mapElements.add(createBuoyElement((EntityBuoy) obj, false));
                mapElements.add(createBuoyTargetElement((EntityBuoy) obj));
                listElements.add(new ListElement((EntityBuoy) obj));
            }else if(obj instanceof EntityBoat) {
                mapElements.add(createBoatElement((EntityBoat) obj));
            }
        }


        Collections.sort(listElements);

        lastUpdateTime = Minecraft.getSystemTime();
    }


    private static final ResourceLocation BACKGROUND = ResourceHelper.getResource("/gui/buoy_background.png");
    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/buoy_elements.png");
    @Override
    public void drawGuiBackground(float f, int x, int y) {
        reloadElementsIfNecessary();

        GL11.glColor4f(1, 1, 1, 1);


        int left = getGuiLeft();
        int top = getGuiTop();
        ResourceHelper.bindResource(BACKGROUND);
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        ResourceHelper.bindResource(TEXTURE);

        x -= getGuiLeft();
        y -= getGuiTop();

        IInfoText info = null;
        setupScissor(MAP_X, MAP_Y, MAP_SIZE, MAP_SIZE);
        startScissor();

        GL11.glEnable(GL11.GL_BLEND);
        for (MapElement mapElement : mapElements) {
            if (mapElement.isVisible()) {
                if (mapElement.entity.equals(buoy)) {
                    drawBuoyLine(buoy, previous, true);
                    drawBuoyLine(buoy, next, true);
                }else{
                    drawBuoyLine(mapElement.entity, mapElement.prev, false);
                    drawBuoyLine(mapElement.entity, mapElement.next, false);
                }
            }
        }
        GL11.glDisable(GL11.GL_BLEND);

        ResourceHelper.bindResource(TEXTURE);
        GL11.glColor4f(1, 1, 1, 1);

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


        ResourceHelper.bindResource(TEXTURE);
        GL11.glColor4f(1, 1, 1, 1);
        stopScissor();

        GL11.glEnable(GL11.GL_BLEND);
        if (listElements.size() <= VISIBLE_LIST_ITEMS) {

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

            drawTexturedModalRect(left + target[0], top + target[1], LIST_ELEMENT_SRC_X + textureId * (target[2] + 1), LIST_ELEMENT_SRC_Y, target[2], target[3]);

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, element.item, left + target[0], top + target[1] + 1);

            getFontRenderer().drawString(element.distance + "m " + element.direction.translate(), left + target[0] + 20, top + target[1] + 6, 0x404040);
            ResourceHelper.bindResource(TEXTURE);
            GL11.glColor4f(1, 1, 1, 1);
        }

        for (BuoyInfo infoPanel : infoPanels) {
            info = drawBuoyInfo(infoPanel, x, y, info);
        }


        if (info != null) {
            drawMouseOver(info.getInfo(), x + getGuiLeft(), y + getGuiTop());
        }
    }

    private void drawBuoyLine(Entity entity, Entity other, boolean owner) {
        if (entity != null && other != null && !other.equals(buoy)) {
            int left = getGuiLeft();
            int top = getGuiTop();

            int[] location1 = getEntityLocation(entity);
            int[] location2 = getEntityLocation(other);

            int color = 0x55BD3612;
            if (owner) {
                color |= 0xFF << 24;
            }

            drawLine(left + location1[0], top + location1[1], left + location2[0], top + location2[1], 3, color);
        }
    }

    private IInfoText drawBuoyInfo(BuoyInfo buoyInfo, int mX, int mY, IInfoText info) {
        int left = getGuiLeft();
        int top = getGuiTop();

        getFontRenderer().drawString(buoyInfo.label.translate(), left + buoyInfo.x, top + buoyInfo.y, 0x404040);
        GL11.glColor4f(1, 1, 1, 1);

        EntityBuoy buoy = buoyInfo.getBuoy();
        if (buoy != null) {
            int[] target = {buoyInfo.x, buoyInfo.y + BUTTON_Y, 16, 16};
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, buoy.getBuoyItem(), left + target[0], top + target[1]);

            if (inRect(mX, mY, target)) {
                info = new StaticText(buoy.getBuoyType().getName() + "\n" + ColorHelper.GRAY + (int)buoy.posX + ", " + (int)buoy.posY + ", " + (int)buoy.posZ);
            }
        }



        Button[] buttons = buoyInfo.getButtons();
        if (buttons != null) {
            ResourceHelper.bindResource(TEXTURE);
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            for (int i = 0; i < buttons.length; i++) {
                Button button = buttons[i];
                int[] target = {buoyInfo.x + BUTTON_X + BUTTON_OFFSET * i, buoyInfo.y + BUTTON_Y, BUTTON_SIZE, BUTTON_SIZE};
                int textureId = 0;
                if (button.isEnabled()) {
                    if (inRect(mX, mY, target)) {
                        info = new StaticText(button.text.translate());
                        textureId = 1;
                    }
                }else{
                    textureId = 2;
                }

                drawTexturedModalRect(left + target[0], top + target[1], BUTTON_SRC_X + textureId * (target[2] + 1), BUTTON_SRC_Y, target[2], target[3]);
                if (!button.isEnabled()) {
                    GL11.glColor4f(1, 1, 1, 0.5F);
                }
                drawTexturedModalRect(left + target[0] + BUTTON_ICON_OFFSET, top + target[1] + BUTTON_ICON_OFFSET, BUTTON_ICON_SRC_X + button.iconIndex * (BUTTON_ICON_SIZE + 1), BUTTON_ICON_SRC_Y, BUTTON_ICON_SIZE, BUTTON_ICON_SIZE);
                GL11.glColor4f(1, 1, 1, 1);
            }
            GL11.glDisable(GL11.GL_BLEND);
        }

        return info;
    }

    private RenderItem renderItem = new RenderItem();

    private int[] getListElement(int i) {
        return new int[] {LIST_X, LIST_Y + i * LIST_ELEMENT_HEIGHT, LIST_ELEMENT_WIDTH, LIST_ELEMENT_HEIGHT};
    }

    private static final int ELEMENT_SRC_Y = 1;

    private static final int SELECTED_BUOY_SIZE = 7;
    private static final int SELECTED_BUOY_SRC_X = 9;

    private static final int BUOY_SIZE = 5;
    private static final int BUOY_SRC_X = 17;

    private static final int BOAT_SIZE = 3;
    private static final int BOAT_SRC_X = 23;

    private static final int MODULAR_BOAT_COLOR = 0xFFFFFF;
    private static final int VANILLA_BOAT_COLOR = 0xA67C52;



    private static final int LIST_X = 10;
    private static final int LIST_Y = 104;
    private static final int VISIBLE_LIST_ITEMS = 8;
    private static final int LIST_ELEMENT_SRC_X = 1;
    private static final int LIST_ELEMENT_SRC_Y = 13;
    private static final int LIST_ELEMENT_WIDTH = 73;
    private static final int LIST_ELEMENT_HEIGHT = 18;

    private static final int SCROLL_HEIGHT = 144;
    private static final int SCROLL_BAR_WIDTH = 7;
    private static final int SCROLL_BAR_HEIGHT = 11;
    private static final int SCROLL_BAR_SRC_X = 1;
    private static final int SCROLL_X = 86;
    private static final int[] SCROLL_TARGET = {SCROLL_X, LIST_Y, SCROLL_BAR_WIDTH, SCROLL_HEIGHT};

    private static final int MAP_X = 108;
    private static final int MAP_Y = 107;
    private static final int MAP_SIZE = 138;
    private static final int[] MAP_TARGET = {MAP_X, MAP_Y, MAP_SIZE, MAP_SIZE};

    private static final int BUTTON_X = 20;
    private static final int BUTTON_Y = 10;
    private static final int BUTTON_OFFSET = 20;
    private static final int BUTTON_SRC_X = 1;
    private static final int BUTTON_SRC_Y = 32;
    private static final int BUTTON_ICON_SRC_X = 1;
    private static final int BUTTON_ICON_SRC_Y = 51;
    private static final int BUTTON_SIZE = 18;
    private static final int BUTTON_ICON_SIZE = 16;
    private static final int BUTTON_ICON_OFFSET = (BUTTON_SIZE - BUTTON_ICON_SIZE) / 2;

    private int[] getEntityLocation(Entity entity) {
        return getEntityLocation(entity.posX, entity.posZ);
    }

    private int[] getEntityLocation(double posX, double posZ) {
        int x = (int)((0.5 + (posX - buoy.posX) / VISIBLE_BLOCKS) * MAP_SIZE);
        int z = (int)((0.5 + (posZ - buoy.posZ) / VISIBLE_BLOCKS) * MAP_SIZE);

        return new int[] {MAP_X + x, MAP_Y + z};
    }

    private class MapElement {
        private Entity entity;
        private EntityBuoy next;
        private EntityBuoy prev;
        private int[] target;

        private boolean isVisible() {
            return entity != null && !entity.isDead;
        }

        protected int[] getLocation() {
            return getEntityLocation(entity);
        }

        private int[] getTarget() {
            if (target == null) {
                int[] location = getLocation();

                target = new int[] {location[0] - w / 2, location[1] - h / 2, w, h};
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
        private ItemStack item;
        private int distance;
        private ILocalizedText direction;
        private boolean isVisible() {
            return buoy != null && !buoy.isDead;
        }
        private ListElement(EntityBuoy buoy) {
            this.buoy = buoy;
            item = buoy.getBuoyItem();
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
        MapElement mapElement = new MapElement(
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
                            info.add(ColorHelper.YELLOW + "Active");    //TODO localize
                        }else if(buoy.equals(selected)) {
                            info.add(ColorHelper.GREEN + "Selected"); //TODO localize
                        }
                        return info;
                    }
                }
        );

        mapElement.next = buoy.getNextBuoy();
        mapElement.prev = buoy.getPrevBuoy();
        if (owner) {
            next = mapElement.next;
            previous = mapElement.prev;
        }
        return mapElement;
    }


    private MapElement createBuoyTargetElement(EntityBuoy buoy) {
        final double[] location = EntityModularBoat.getTarget(buoy);
        return new MapElement(buoy, BOAT_SRC_X, ELEMENT_SRC_Y, BOAT_SIZE, BOAT_SIZE, 0xFF0000, new StaticText("Boat buoy target")) {
            @Override
            protected int[] getLocation() {
                return getEntityLocation(location[0], location[1]);
            }
        };
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
    public void mouseClick(int x, int y, int b) {
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

        for (BuoyInfo infoPanel : infoPanels) {
            Button[] buttons = infoPanel.getButtons();
            if (buttons != null) {
                for (int i = 0; i < buttons.length; i++) {
                    Button button = buttons[i];
                    int[] target = {infoPanel.x + BUTTON_X + BUTTON_OFFSET * i, infoPanel.y + BUTTON_Y, BUTTON_SIZE, BUTTON_SIZE};
                    if (inRect(x, y, target)) {
                        if (button.isEnabled()) {
                            button.onClick();
                        }
                        return;
                    }
                }
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

    private abstract class BuoyInfo {
        private int x;
        private int y;
        private ILocalizedText label;

        protected BuoyInfo(ILocalizedText label, int x, int y) {
            this.x = x;
            this.y = y;
            this.label = label;
        }

        protected abstract EntityBuoy getBuoy();
        protected Button[] getButtons() {return null;}
    }

    private abstract class BuoyInfoProperty extends BuoyInfo {
        private Button[] buttons;
        protected BuoyInfoProperty(ILocalizedText label, int x, int y, boolean next) {
            super(label, x, y);
            buttons = new Button[] {
                new UseButton(next) {
                    @Override
                    protected void setBuoy(EntityBuoy buoy) {
                        BuoyInfoProperty.this.setBuoy(buoy);
                    }
                },
                new SelectButton() {
                    @Override
                    protected EntityBuoy getBuoy() {
                        return BuoyInfoProperty.this.getBuoy();
                    }
                },
                new RemoveButton(next) {
                    @Override
                    protected EntityBuoy getBuoy() {
                        return BuoyInfoProperty.this.getBuoy();
                    }

                    @Override
                    protected void setBuoy(EntityBuoy buoy) {
                        BuoyInfoProperty.this.setBuoy(buoy);
                    }
                }
            };
        }
        protected abstract void setBuoy(EntityBuoy buoy);


        @Override
        public Button[] getButtons() {
            return buttons;
        }
    }

    private abstract class Button {
        private ILocalizedText text;
        private int iconIndex;

        protected Button(ILocalizedText text, int iconIndex) {
            this.text = text;
            this.iconIndex = iconIndex;
        }

        protected abstract void onClick();
        protected abstract boolean isEnabled();
    }

    private abstract class UseButton extends Button {
        private boolean next;

        protected UseButton(boolean next) {
            super(new PlainText("Use selected buoy"), 0); //TODO localize
            this.next = next;
        }

        @Override
        protected void onClick() {
            setBuoy(selected);
            sendSetBuoyToServer(selected, next);
        }

        @Override
        protected boolean isEnabled() {
            return selected != null;
        }

        protected abstract void setBuoy(EntityBuoy buoy);
    }

    private abstract class SelectButton extends Button {
        protected SelectButton() {
            super(new PlainText("Select this buoy"), 1); //TODO localize
        }

        @Override
        protected void onClick() {
            selected = getBuoy();
        }

        @Override
        protected boolean isEnabled() {
            return getBuoy() != null;
        }
        protected abstract EntityBuoy getBuoy();
    }

    private abstract class RemoveButton extends Button {
        private boolean next;

        protected RemoveButton(boolean next) {
            super(new PlainText("Remove this buoy"), 2); //TODO localize
            this.next = next;
        }

        @Override
        protected void onClick() {
            setBuoy(null);
            sendSetBuoyToServer(null, next);
        }

        @Override
        protected boolean isEnabled() {
            return getBuoy() != null;
        }
        protected abstract EntityBuoy getBuoy();
        protected abstract void setBuoy(EntityBuoy buoy);
    }

    private void sendSetBuoyToServer(EntityBuoy buoy, boolean next) {
        DataWriter dw = PacketHandler.getDataWriter(PacketType.BUOY);
        int entityId = buoy != null && !buoy.isDead ? buoy.getEntityId() : -1;
        dw.writeInteger(entityId);
        dw.writeBoolean(next);
        PacketHandler.sendPacketToServer(dw);
    }
}
