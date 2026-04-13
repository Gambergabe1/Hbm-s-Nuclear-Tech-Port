package com.hbm.blockentity.rbmk;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * RBMK console block entity.
 * Main control interface for the entire RBMK reactor.
 */
public class RBMKConsoleBlockEntity extends AbstractRBMKBlockEntity {
    
    private String reactorName = "RBMK Reactor";
    private boolean reactorActive = false;
    private long ticksActive = 0;
    
    private List<RBMKColumnData> columns = new ArrayList<>();
    private Map<BlockPos, RBMKColumnData> columnMap = new HashMap<>();
    
    private int selectedColumn = 0;
    private boolean showGraph = true;
    private GraphType currentGraph = GraphType.HEAT;
    
    public RBMKConsoleBlockEntity(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }
    
    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        
        ticksActive++;
        
        // Scan for connected RBMK components every 5 seconds
        if (ticksActive % 100 == 0) {
            scanReactorComponents();
        }
    }
    
    /**
     * Scans the area to find all connected RBMK components.
     */
    private void scanReactorComponents() {
        columns.clear();
        columnMap.clear();
        
        int scanRadius = 16;
        for (int x = -scanRadius; x <= scanRadius; x++) {
            for (int z = -scanRadius; z <= scanRadius; z++) {
                BlockPos checkPos = new BlockPos(worldPosition.getX() + x, worldPosition.getY(), worldPosition.getZ() + z);
                
                if (level != null && level.getBlockEntity(checkPos) instanceof AbstractRBMKBlockEntity rbmk) {
                    RBMKColumnData data = new RBMKColumnData(checkPos, rbmk);
                    columns.add(data);
                    columnMap.put(checkPos, data);
                }
            }
        }
    }
    
    /**
     * Returns summary data for the GUI.
     */
    public ReactorSummary getReactorSummary() {
        double avgHeat = 0;
        double maxHeat = 0;
        double totalNeutronFlux = 0;
        int fuelRodCount = 0;
        int controlRodCount = 0;
        
        for (RBMKColumnData column : columns) {
            AbstractRBMKBlockEntity rbmk = column.blockEntity();
            avgHeat += rbmk.heat;
            maxHeat = Math.max(maxHeat, rbmk.heat);
            
            if (rbmk instanceof RBMKFuelRodBlockEntity fuelRod) {
                totalNeutronFlux += fuelRod.getNeutronFlux();
                fuelRodCount++;
            }
            
            if (rbmk instanceof RBMKControlRodBlockEntity) {
                controlRodCount++;
            }
        }
        
        if (!columns.isEmpty()) {
            avgHeat /= columns.size();
        }
        
        return new ReactorSummary(
            columns.size(),
            fuelRodCount,
            controlRodCount,
            avgHeat,
            maxHeat,
            totalNeutronFlux,
            reactorActive
        );
    }
    
    @Override
    public double getMaxHeat() {
        return 800.0;
    }
    
    @Override
    public double getPassiveCooling() {
        return 7.5;
    }
    
    @Override
    public RBMKColumnType getConsoleType() {
        return RBMKColumnType.CONSOLE;
    }
    
    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        reactorName = tag.getString("reactorName");
        reactorActive = tag.getBoolean("reactorActive");
        ticksActive = tag.getLong("ticksActive");
        selectedColumn = tag.getInt("selectedColumn");
        currentGraph = GraphType.values()[tag.getInt("currentGraph")];
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        tag.putString("reactorName", reactorName);
        tag.putBoolean("reactorActive", reactorActive);
        tag.putLong("ticksActive", ticksActive);
        tag.putInt("selectedColumn", selectedColumn);
        tag.putInt("currentGraph", currentGraph.ordinal());
    }
    
    // =====GETTERS/SETTERS=====
    
    public String getReactorName() {
        return reactorName;
    }
    
    public void setReactorName(String name) {
        this.reactorName = name;
    }
    
    public boolean isReactorActive() {
        return reactorActive;
    }
    
    public long getTicksActive() {
        return ticksActive;
    }
    
    public List<RBMKColumnData> getColumns() {
        return columns;
    }
    
    @Nullable
    public RBMKColumnData getColumnAt(BlockPos pos) {
        return columnMap.get(pos);
    }
    
    // =====DATA CLASSES=====
    
    public record RBMKColumnData(BlockPos position, AbstractRBMKBlockEntity blockEntity) {
        public double getHeat() {
            return blockEntity.heat;
        }
        
        public RBMKColumnType getType() {
            return blockEntity.getConsoleType();
        }
    }
    
    public record ReactorSummary(
        int totalColumns,
        int fuelRodCount,
        int controlRodCount,
        double averageHeat,
        double maxHeat,
        double totalNeutronFlux,
        boolean isActive
    ) {}
    
    public enum GraphType {
        HEAT,
        NEUTRON_FLUX,
        WATER_LEVEL,
        STEAM_PRODUCTION,
        FUEL_DEPLETION
    }
}
