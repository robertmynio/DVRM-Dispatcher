package datacenterInterface.dtos.jaxbBindingClasses;


import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 19, 2010
 * Time: 4:14:41 PM
 * To change this template use File | Settings | File Templates.
 */


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serverDiskDescription", propOrder = {
        "type",
        "maximumStorage",
        "optimumStorage",
        "format",
        "target"
})

@XmlRootElement(name = "serverDiskDescription")
public class ServerDiskDescription extends BasicJAXBEntity {

    @XmlElement(required = false)
    private String type;

    @XmlElement(required = true)
    private int maximumStorage;

    @XmlElement(required = true)
    private int optimumStorage;

    @XmlElement(required = false)
    private String format;

    @XmlElement(required = true)
    private String target;


    public String getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaximumStorage() {
        return maximumStorage;
    }

    public void setMaximumStorage(int maximumStorage) {
        this.maximumStorage = maximumStorage;
    }

    public int getOptimumStorage() {
        return optimumStorage;
    }

    public void setOptimumStorage(int optimumStorage) {
        this.optimumStorage = optimumStorage;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
