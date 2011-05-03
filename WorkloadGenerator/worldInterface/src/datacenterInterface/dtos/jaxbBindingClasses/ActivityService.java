package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 19, 2010
 * Time: 5:05:40 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivityService", propOrder = {
        "serviceName",
        "cpuWeight",
        "memWeight",
        "storageWeight",
        "requestedCoresCount",
        "requestedCPUMax",
        "requestedCPUMin",
        "requestedMemoryMax",
        "requestedMemoryMin",
        "requestedStorageMax",
        "requestedStorageMin",
        "rootDisk",
        "disks",
        "ip",
        "vncPort"
})

@XmlRootElement(name = "ActivityService")
public class ActivityService extends BasicJAXBEntity {

    @XmlElement(required = true)
    private String serviceName;

    @XmlElement(required = true)
    private float cpuWeight;

    @XmlElement(required = true)
    private float memWeight;

    @XmlElement(required = true)
    private float storageWeight;

    @XmlElement(required = true)
    private int requestedCoresCount;

    @XmlElement(required = true)
    private int requestedCPUMax;

    @XmlElement(required = true)
    private int requestedCPUMin;

    @XmlElement(required = true)
    private int requestedMemoryMax;

    @XmlElement(required = true)
    private int requestedMemoryMin;

    @XmlElement(required = true)
    private int requestedStorageMax;

    @XmlElement(required = true)
    private int requestedStorageMin;

    @XmlElement(required = false)
    private String rootDisk;

    @XmlElement(required = true, name = "StorageDevice")
    private VirtualDiskDescription[] disks;

    @XmlElement(required = true)
    private String ip;

    private int vncPort;

    public int getRequestedStorageMax() {
        return requestedStorageMax;
    }

    public void setRequestedStorageMax(int requestedStorageMax) {
        this.requestedStorageMax = requestedStorageMax;
    }

    public int getRequestedStorageMin() {
        return requestedStorageMin;
    }

    public void setRequestedStorageMin(int requestedStorageMin) {
        this.requestedStorageMin = requestedStorageMin;
    }

    public float getCpuWeight() {
        return cpuWeight;
    }

    public void setCpuWeight(float cpuWeight) {
        this.cpuWeight = cpuWeight;
    }

    public float getMemWeight() {
        return memWeight;
    }

    public void setMemWeight(float memWeight) {
        this.memWeight = memWeight;
    }

    public float getStorageWeight() {
        return storageWeight;
    }

    public void setStorageWeight(float storageWeight) {
        this.storageWeight = storageWeight;
    }

    public int getRequestedCoresCount() {
        return requestedCoresCount;
    }

    public void setRequestedCoresCount(int requestedCoresCount) {
        this.requestedCoresCount = requestedCoresCount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getRequestedCPUMax() {
        return requestedCPUMax;
    }

    public void setRequestedCPUMax(int requestedCPUMax) {
        this.requestedCPUMax = requestedCPUMax;
    }

    public int getRequestedCPUMin() {
        return requestedCPUMin;
    }

    public void setRequestedCPUMin(int requestedCPUMin) {
        this.requestedCPUMin = requestedCPUMin;
    }

    public int getRequestedMemoryMax() {
        return requestedMemoryMax;
    }

    public void setRequestedMemoryMax(int requestedMemoryMax) {
        this.requestedMemoryMax = requestedMemoryMax;
    }

    public int getRequestedMemoryMin() {
        return requestedMemoryMin;
    }

    public void setRequestedMemoryMin(int requestedMemoryMin) {
        this.requestedMemoryMin = requestedMemoryMin;
    }

    public String getRootDisk() {
        return rootDisk;
    }

    public void setRootDisk(String rootDisk) {
        this.rootDisk = rootDisk;
    }

    public VirtualDiskDescription[] getDisks() {
        return disks;
    }

    public void setDisks(VirtualDiskDescription[] disks) {
        this.disks = disks;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getVncPort() {
        return vncPort;
    }

    public void setVncPort(int vncPort) {
        this.vncPort = vncPort;
    }
}
