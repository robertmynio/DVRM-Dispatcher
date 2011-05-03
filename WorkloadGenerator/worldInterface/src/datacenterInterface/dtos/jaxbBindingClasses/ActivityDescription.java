package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 19, 2010
 * Time: 5:45:30 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivityDescription", propOrder = {
        "duration",
        "name",
        "services"
})

@XmlRootElement(name = "ActivityDescription")
public class ActivityDescription extends BasicJAXBEntity {

    @XmlElement(required = false)
    private int duration;   //in minutes

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true, name = "ActivityServiceDescription")
    private ActivityService[] services;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityService[] getServices() {
        return services;
    }

    public void setServices(ActivityService[] services) {
        this.services = services;
    }
}
