package misc;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Aug 9, 2010
 * Time: 1:28:30 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GlobalVars {
    private GlobalVars() {
    }

    public static final String x3dScene = "x3d/datacenter.x3d";
    public static final String XML_ELEMENT = "int";
    public static final String CONTEXT_INSTANCE_MODIFIED = "Context instance modified";
    public static final String CMAGENT_NAME = "CMAAgent";
    public static final String RPAGENT_NAME = "RPAgent";
    public static final String EMAGENT_NAME = "EMAgent";
    public static final String GUIAGENT_NAME = "GUIAgent";
    public static final String RTEINFOAGENT_NAME = "RTEInfoAgent";
    public static final String ENERGYAGENT_NAME = "EnergyConsumptionActualizationAgent";
    public static final String RLAGENT_NAME = "RLAgent";
    public static final String X3DAGENT_NAME = "X3DAgent";
    public static final String TMAGENT_NAME = "TMAgent";
    public static final String WSCHEDULER_Agent = "WorkloadSchedulerAgent";
    public static final String CLIENT_AGENT = "ClientAgent";
    public static final String PHYSICAL_RESOURCE_VALUE_NAME = "valueOfService";
    public static final String PHYSICAL_RESOURCE_NAME_NAME = "nameOfService";
    public static final String AGENT_REQUEST = "request";
    public static final String WORLD_FILE = "ontology/worldFile.xml";
    public static final String WORLD_FILE_SCHEMA = "ontology/worldFile.xsd";
    public static final String ONTOLOGY_CLASS_NAME = "ontology-class-name";
    public static final String ONTOLOGY_INDIVIDUAL_NAME = "ontology-individual-name";
    public static final String X3D_SCENE_FILE = "x3d/datacenter.x3d";
    public static final String DATA_NAME = "data-name";
    public static final String DATA_VALUE = "data-value";
    public static final String CONTEXT_ELEMENT = "context-element";
    public static final String ONTOLOGY_FILE = "./ontology/context_KAON.rdf-xml.owl";
    public static final String POLICIES_FILE = "ontology/TestPolicies.xml";
    public static final String MEMORY_SELFHEALING_FILE = "memory/memorySelfHealing.dat";
    public static final String MEMORY_SELFOPTIMIZING_FILE = "memory/memorySelfOptimizing.dat";
    public static final String FUZZY_LOGIC_CONTROL_FILE = "fuzzy/negotiator.fcl";
    public static final String VIRTUAL_MACHINES_NETWORK_PATH = "\\\\WINDOWSSS2008\\VirtualMachines\\";
    public static final String GLOBAL_LOOP_CONTROLLER_IP = "192.168.1.10";
    public static final String BASE_VM_NAME_CPU = "CPUIntensive";//"Empty";
    public static final String BASE_VM_NAME_MEM = "MEMntensive";
    public static final String BASE_VM_NAME_HDD = "IOIntensive";//"Empty";
    public static final String BROADCAST_IP_ADDRESS = "192.168.1.255";
    public static final String WAKE_UP_PORT = "0";
    public static final String PHYSICAL_PATH = "\\\\" + GLOBAL_LOOP_CONTROLLER_IP + "\\VirtualMachines\\";
    public static final int MAX_NAME_LENGTH = 10;
    public static final int MAX_TASK_LIFE_IN_MINUTES = 5;
    public static final String TASK_TEMPLATE_NAME = "TaskType";

    //public static final String MEMORY_DATACENTER_FILE = "memory/memory.dat";
    public static final int INDIVIDUAL_DELETED = 0;
    public static final int INDIVIDUAL_CREATED = 1;
    public static final int INDIVIDUAL_MODIFIED = 2;

    private static String x3DPlatformName = "acasa-25f3f1aa5:1099/JADE";
    private static String x3DPlatformAddress = "http://acasa-25f3f1aa5:7778/acc";

    public static int RMI_PORT = 1234;

}
