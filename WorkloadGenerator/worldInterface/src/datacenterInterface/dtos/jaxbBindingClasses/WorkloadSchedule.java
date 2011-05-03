package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 24, 2010
 * Time: 3:45:24 PM
 * To change this template use File | Settings | File Templates.
 */


@XmlAccessorType(XmlAccessType.FIELD)

@XmlRootElement(name = "WorkloadSchedule")

public class WorkloadSchedule extends BasicJAXBEntity {
    @XmlElement(required = true, name = "ApplicationList")
    private JaxbPair[] applications;

    public JaxbPair[] getApplications() {
        return applications;
    }

    public void setApplications(JaxbPair[] applications) {
        this.applications = applications;
    }
}
