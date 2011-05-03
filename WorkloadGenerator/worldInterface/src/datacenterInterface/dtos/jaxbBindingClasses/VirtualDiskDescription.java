package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 19, 2010
 * Time: 5:04:23 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualDiskDescription", propOrder = {
        "type",
        "size",
        "format",
        "target",
        "source"
})

@XmlRootElement(name = "VirtualDiskDescription")
public class VirtualDiskDescription extends BasicJAXBEntity {

    @XmlElement(required = true)
    private String type;

    @XmlElement(required = true)
    private int size;

    @XmlElement(required = false)
    private String format;

    @XmlElement(required = true)
    private String target;

    @XmlElement(required = true)
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
