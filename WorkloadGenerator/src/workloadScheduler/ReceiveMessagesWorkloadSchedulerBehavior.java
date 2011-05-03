package workloadScheduler;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by IntelliJ IDEA.
 * User: Moldovanus
 * Date: Dec 18, 2010
 * Time: 9:36:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveMessagesWorkloadSchedulerBehavior extends CyclicBehaviour {

    private WorkloadSchedulerAgent agent;

    public ReceiveMessagesWorkloadSchedulerBehavior(WorkloadSchedulerAgent agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage message = agent.receive();
        if (message == null) {
            return;
        }

        try {
            switch (message.getPerformative()) {
                case ACLMessage.INFORM:
                    String individualName = message.getContent();
                    if (individualName.equals("Pause generation")) {
                        agent.setPauseGenerating(true);
                    } else if (individualName.equals("Resume generation")) {
                        agent.setPauseGenerating(false);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
