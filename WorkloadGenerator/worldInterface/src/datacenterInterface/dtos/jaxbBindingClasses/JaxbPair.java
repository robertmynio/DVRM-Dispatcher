package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 24, 2010
 * Time: 4:17:05 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)

@XmlRootElement(name = "Pair")
public class JaxbPair extends BasicJAXBEntity {

    @XmlElement(required = true)
    private Long startDelay;

    @XmlElement(required = true)
    private ApplicationDescription application;

    public JaxbPair() {
    }


    public JaxbPair(Long startDelay, ApplicationDescription application) {
        this.startDelay = startDelay;
        this.application = application;
    }

    public Long getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(Long startDelay) {
        this.startDelay = startDelay;
    }

    public ApplicationDescription getApplication() {
        return application;
    }

    public void setApplication(ApplicationDescription application) {
        this.application = application;
    }
}


