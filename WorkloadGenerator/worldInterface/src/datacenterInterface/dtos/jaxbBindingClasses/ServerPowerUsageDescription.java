package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 8, 2011
 * Time: 11:07:50 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "powerUsageDescription", propOrder = {
        "whileIdle",
        "whileLoadedTo40",
        "whileLoadedTo100"
})

@XmlRootElement(name = "powerUsageDescription")
public class ServerPowerUsageDescription {

    @XmlElement(required = true)
    private int whileIdle;
    @XmlElement(required = true)
    private int whileLoadedTo40;
    @XmlElement(required = true)
    private int whileLoadedTo100;

    public int getWhileIdle() {
        return whileIdle;
    }

    public void setWhileIdle(int whileIdle) {
        this.whileIdle = whileIdle;
    }

    public int getWhileLoadedTo40() {
        return whileLoadedTo40;
    }

    public void setWhileLoadedTo40(int whileLoadedTo40) {
        this.whileLoadedTo40 = whileLoadedTo40;
    }

    public int getWhileLoadedTo100() {
        return whileLoadedTo100;
    }

    public void setWhileLoadedTo100(int whileLoadedTo100) {
        this.whileLoadedTo100 = whileLoadedTo100;
    }
}
