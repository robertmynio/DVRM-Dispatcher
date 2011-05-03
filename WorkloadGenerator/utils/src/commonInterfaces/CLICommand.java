package commonInterfaces;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 22, 2010
 * Time: 10:50:40 AM
 * To change this template use File | Settings | File Templates.
 */
public enum CLICommand implements Serializable {
    EXIT("exit"),
    START_ON_TICK("startOnTick"),
    RESET("reset"),
    CLEAR("clear"),
    ADD_HOST("addHost", true),
    ADD_HOSTS("addHosts", true),
    ADD_APPLICATION("addApplication", true),
    ADD_SCHEDULE("addSchedule", true),
    LIST_APPLICATIONS("listApplications"),
    LIST_HOSTS("listHosts"),
    REMOVE_APPLICATION("removeApplication"),
    REMOVE_ACTIVITY("removeActivity"),
    REMOVE_SERVICE("removeService"),
    REMOVE_HOST("removeHost"),
    REMOVE_ALL("removeAll"),
    RESCHEDULE("reschedule"),
    STOP_WORKLOAD_GENERATOR("stopWG"),
    START_WORKLOAD_GENERATOR("startWG");


    private String command;
    private static HashMap<String, CLICommand> hashMap;
    private static HashMap<CLICommand, String> parameters;
    private boolean usesFile;

    static {
        hashMap = new HashMap<String, CLICommand>();
        parameters = new HashMap<CLICommand, String>();

        hashMap.put("exit", EXIT);
//        hashMap.put("reset", RESET);
//        hashMap.put("clear", CLEAR);
//        hashMap.put("startOnTick", START_ON_TICK);

//        hashMap.put("addHost", ADD_HOST);
//        parameters.put(ADD_HOST, "description_file");

        hashMap.put("addHosts", ADD_HOSTS);
        parameters.put(ADD_HOSTS, "description_file");

//        hashMap.put("addApplication", ADD_APPLICATION);
//        parameters.put(ADD_APPLICATION, "description_file");

        hashMap.put("addScheduledTasks", ADD_SCHEDULE);
        parameters.put(ADD_SCHEDULE, "description_file");

//        hashMap.put("removeApplication", REMOVE_APPLICATION);
//        parameters.put(ADD_SCHEDULE, "name");

//        hashMap.put("removeActivity", REMOVE_ACTIVITY);
//        parameters.put(REMOVE_ACTIVITY, "name");

//        hashMap.put("removeService", REMOVE_SERVICE);
//        parameters.put(REMOVE_SERVICE, "name");

//        hashMap.put("listApplications", LIST_APPLICATIONS);
//        hashMap.put("listHosts", LIST_HOSTS);

        hashMap.put("removeHost", REMOVE_HOST);
        parameters.put(REMOVE_HOST, "hostname");

//        hashMap.put("removeAll", REMOVE_ALL);

//        hashMap.put("reschedule", RESCHEDULE);
//        parameters.put(RESCHEDULE, "name");

        hashMap.put("startRandomWorkloadGenerator", START_WORKLOAD_GENERATOR);

        parameters.put(START_WORKLOAD_GENERATOR, "[generationTimeMin=<intValue>] [generationIntervalMax=<intValue>]" +
                " [serviceLifeMin=<intValue>] [serviceLifeMax=<intValue>]" +
                " [generatedServicesNoMin=<intValue>][generatedServicesNoMax=<intValue>]");

        hashMap.put("stopRandomWorkloadGenerator", STOP_WORKLOAD_GENERATOR);
    }

    CLICommand(String command) {
        this.command = command;
    }

    CLICommand(String command, boolean usesFile) {
        this.usesFile = usesFile;
        this.command = command;
    }

    public boolean usesFile() {
        return usesFile;
    }

    public static List<String> getValues() {

        Iterator<String> iterator = hashMap.keySet().iterator();
        List<String> names = new ArrayList<String>();
        while (iterator.hasNext()) {
            String value = iterator.next();
            CLICommand command = hashMap.get(value);
//            if (parameters.containsKey(command)) {
//                names.add(value + " " + parameters.get(command) + "\n");
//            } else {
            names.add(value);// + "\n");
//            }
        }
        Collections.sort(names);
        return names;
    }


    public static CLICommand getCommandForString(String string) {
        return hashMap.get(string);
    }

    public static String getParameter(String commandName) {
        CLICommand command = hashMap.get(commandName);
        return parameters.get(command);
    }
}
