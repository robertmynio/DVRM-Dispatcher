package commonDtos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Sep 8, 2010
 * Time: 11:00:47 AM
 * Adds IP, MAC fields to the standard ServerDto
 */
public class ExtendedServerDto implements Serializable {

    private String ipAddress;
    private String macAddress;
    private String serverName;
    private boolean isOn;
    private int maximumCPU;
    private int totalCPU;
    private int optimumCPU;
    private int usedCPU;

    private int maximumMemory;
    private int optimumMemory;
    private int totalMemory;
    private int usedMemory;

    private String tmName;
    private String vmmName;
    private String imName;

    private List<StorageDto> storageDtos;

    private int coreNo;

    private int id;

    private int powerConsumptionWhileIdle;
    private int powerConsumptionWhileLoadedTo40;
    private int powerConsumptionWhileLoadedTo100;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPowerConsumptionWhileIdle() {
        return powerConsumptionWhileIdle;
    }

    public void setPowerConsumptionWhileIdle(int powerConsumptionWhileIdle) {
        this.powerConsumptionWhileIdle = powerConsumptionWhileIdle;
    }

    public int getPowerConsumptionWhileLoadedTo40() {
        return powerConsumptionWhileLoadedTo40;
    }

    public void setPowerConsumptionWhileLoadedTo40(int powerConsumptionWhileLoadedTo40) {
        this.powerConsumptionWhileLoadedTo40 = powerConsumptionWhileLoadedTo40;
    }

    public int getPowerConsumptionWhileLoadedTo100() {
        return powerConsumptionWhileLoadedTo100;
    }

    public void setPowerConsumptionWhileLoadedTo100(int powerConsumptionWhileLoadedTo100) {
        this.powerConsumptionWhileLoadedTo100 = powerConsumptionWhileLoadedTo100;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public String getVmmName() {
        return vmmName;
    }

    public void setVmmName(String vmmName) {
        this.vmmName = vmmName;
    }

    public String getImName() {
        return imName;
    }

    public void setImName(String imName) {
        this.imName = imName;
    }

    public int getCoreNo() {
        return coreNo;
    }

    public int getTotalCPU() {
        return totalCPU;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setTotalCPU(int totalCPU) {
        this.totalCPU = totalCPU;
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }


    public void setCoreNo(int coreNo) {
        this.coreNo = coreNo;
    }

    public int getUsedCPU() {
        return usedCPU;
    }

    public void setUsedCPU(int usedCPU) {
        this.usedCPU = usedCPU;
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(int usedMemory) {
        this.usedMemory = usedMemory;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getMaximumCPU() {
        return maximumCPU;
    }

    public void setMaximumCPU(int maximumCPU) {
        this.maximumCPU = maximumCPU;
    }

    public int getOptimalCPU() {
        return optimumCPU;
    }

    public void setOptimumCPU(int optimumCPU) {
        this.optimumCPU = optimumCPU;
    }

    public int getMaximumMemory() {
        return maximumMemory;
    }

    public void setMaximumMemory(int maximumMemory) {
        this.maximumMemory = maximumMemory;
    }

    public int getOptimalMemory() {
        return optimumMemory;
    }

    public void setOptimumMemory(int optimumMemory) {
        this.optimumMemory = optimumMemory;
    }

    public List<StorageDto> getStorageDtos() {
        return storageDtos;
    }

    public void setStorageDtos(List<StorageDto> storageDtos) {
        this.storageDtos = storageDtos;
    }

    public double distanceTo(ExtendedServerDto server) {
        double minDistance = 100000;

        double memory1[] = {this.getUsedMemory(), (this.getTotalMemory() + this.getOptimalMemory()) / 2.0, (this.getTotalMemory() + this.getOptimalMemory()) / 2.0, this.getUsedMemory()};
        double cpu1[] = {this.getUsedCPU(), this.getUsedCPU(), (this.getTotalCPU() + this.getOptimalCPU()) / 2.0, (this.getTotalCPU() + this.getOptimalCPU()) / 2.0};

        double memory2[] = {server.getUsedMemory(), (server.getTotalMemory() + server.getOptimalMemory()) / 2.0, (server.getTotalMemory() + server.getOptimalMemory()) / 2.0, server.getUsedMemory()};
        double cpu2[] = {server.getUsedCPU(), server.getUsedCPU(), (server.getTotalCPU() + server.getOptimalCPU()) / 2.0, (server.getTotalCPU() + server.getOptimalCPU()) / 2.0};
        double totalDistance = 0;
        for (int i = 0; i < 4; i++) {
            //for (int j = 0; j < 4; j++) {
            double dist = Math.sqrt((Math.pow(memory1[i] - memory2[i], 2) + Math.pow((cpu1[i] - cpu2[i]), 2)));

            totalDistance += dist;
            // }
        }
        if (this.getCoreNo() != server.getCoreNo()) {
            totalDistance *= (Math.abs(coreNo - server.getCoreNo()) + 1);
        }
        return totalDistance;

    }

    @Override
    public boolean equals(Object s) {
        if (!(s instanceof ExtendedServerDto)) return false;
        ExtendedServerDto server = (ExtendedServerDto) s;
        if (!server.getIpAddress().equalsIgnoreCase(ipAddress)) return false;
        if (!server.getMacAddress().equalsIgnoreCase(macAddress)) return false;
        if (server.getMaximumCPU() != maximumCPU) return false;
        if (server.getOptimalCPU() != optimumCPU) return false;
        if (server.getMaximumMemory() != maximumMemory) return false;
        if (server.getOptimalMemory() != optimumMemory) return false;
        if (server.getUsedCPU() != usedCPU) return false;
        if (server.getUsedMemory() != usedMemory) return false;

        return true;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
