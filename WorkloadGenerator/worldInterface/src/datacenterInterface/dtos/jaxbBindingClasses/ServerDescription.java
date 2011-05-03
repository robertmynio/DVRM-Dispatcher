package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 19, 2010
 * Time: 4:08:02 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serverDescription", propOrder = {
        "coreCount",
        "maximumCpu",
        "optimumCpu",
        "maximumMem",
        "optimumMem",
        "ip",
        "hostname",
        "mac",
        "im",
        "vmm",
        "tm",
        "serverDisks",
        "serverPowerUsageDescription"
})

@XmlRootElement(name = "serverDescription")
public class ServerDescription extends BasicJAXBEntity {

    @XmlElement(required = true)
    private int maximumCpu;

    @XmlElement(required = true)
    private int optimumCpu;

    @XmlElement(required = true)
    private int maximumMem;

    @XmlElement(required = true)
    private int optimumMem;

    @XmlElement(required = true, name = "serverDiskDescription")
    private ServerDiskDescription[] serverDisks;

    @XmlElement(required = true, name = "powerUsageDescription")
    private ServerPowerUsageDescription serverPowerUsageDescription;

    @XmlElement(required = true)
    private String ip;

    @XmlElement(required = false)
    private String hostname;

    @XmlElement(required = false)
    private String mac;

    @XmlElement(required = true)
    private String im;

    @XmlElement(required = true)
    private String vmm;

    @XmlElement(required = true)
    private String tm;

    @XmlElement(required = true)
    private int coreCount;

    public ServerPowerUsageDescription getServerPowerUsageDescription() {
        return serverPowerUsageDescription;
    }

    public void setServerPowerUsageDescription(ServerPowerUsageDescription serverPowerUsageDescription) {
        this.serverPowerUsageDescription = serverPowerUsageDescription;
    }

    public int getCoreCount() {
        return coreCount;
    }

    public void setCoreCount(int coreCount) {
        this.coreCount = coreCount;
    }

    public int getMaximumCpu() {
        return maximumCpu;
    }

    public void setMaximumCpu(int maximumCpu) {
        this.maximumCpu = maximumCpu;
    }

    public int getOptimumCpu() {
        return optimumCpu;
    }

    public void setOptimumCpu(int optimumCpu) {
        this.optimumCpu = optimumCpu;
    }

    public int getMaximumMem() {
        return maximumMem;
    }

    public void setMaximumMem(int maximumMem) {
        this.maximumMem = maximumMem;
    }

    public int getOptimumMem() {
        return optimumMem;
    }

    public void setOptimumMem(int optimumMem) {
        this.optimumMem = optimumMem;
    }

    public ServerDiskDescription[] getServerDisks() {
        return serverDisks;
    }

    public void setServerDisks(ServerDiskDescription[] serverDisks) {
        this.serverDisks = serverDisks;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getVmm() {
        return vmm;
    }

    public void setVmm(String vmm) {
        this.vmm = vmm;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }
}
