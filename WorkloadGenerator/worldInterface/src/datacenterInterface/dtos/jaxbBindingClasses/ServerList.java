package datacenterInterface.dtos.jaxbBindingClasses;

import javax.xml.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 29, 2010
 * Time: 2:59:58 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Hosts", propOrder = {
        "servers"
})

@XmlRootElement(name = "Hosts")
public class ServerList {
    @XmlElement(required = true, name = "Host")
    private ServerDescription[] servers;

    public ServerDescription[] getServers() {
        return servers;
    }

    public void setServers(ServerDescription[] servers) {
        this.servers = servers;
    }
}
