package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 22, 2010
 * Time: 10:24:24 AM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationDescription", propOrder = {
        "name",
        "activities"
})

@XmlRootElement(name = "ApplicationDescription")
public class ApplicationDescription extends BasicJAXBEntity {

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true, name = "ActivityDescription")
    private ActivityDescription[] activities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityDescription[] getActivities() {
        return activities;
    }

    public void setActivities(ActivityDescription[] activities) {
        this.activities = activities;
    }
}
