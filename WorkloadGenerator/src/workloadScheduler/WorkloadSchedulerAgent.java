package workloadScheduler;

import commonDtos.ExtendedTaskDto;
import commonDtos.TaskDiskDto;
import commonInterfaces.Vars;
import datacenterInterface.dtos.jaxbBindingClasses.*;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import misc.GlobalVars;

import vdrm.base.impl.BaseCommon;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Moldovanus
 * Date: Dec 18, 2010
 * Time: 9:02:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkloadSchedulerAgent extends Agent {
    protected ArrayList<ExtendedTaskDto> possibleTaskPool;
    private static int BEGINNING_ARRIVAL = 20;
    //interval at which new tasks are generated in seconds
    private static int WORKLOAD_GENERATION_INTERVAL_MAX = 20;
    private static int WORKLOAD_GENERATION_INTERVAL_MIN = 10;

    //the minimum and maximum value for the workload duration( task running time until the task terminates ) in seconds
    private static int TASK_DURATION_INTERVAL_MAX = 400;
    private static int TASK_DURATION_INTERVAL_MIN = 220;

    //number of tasks to be generated at one time        
    private static int NUMBER_OF_GENERATED_TASKS_MAX = 5;
    private static int NUMBER_OF_GENERATED_TASKS_MIN = 0;

    private Random random = new Random();

    private int generatedTaskIndex = 0;

    private Agent agent;

    private boolean pauseGenerating = false;

    private File generatedSchedule;
    private WorkloadSchedule workloadSchedule;
    private Date agentCreationDate;

    {
        possibleTaskPool = new ArrayList<ExtendedTaskDto>();
        TaskDiskDto baseDiskDto = new TaskDiskDto();
        baseDiskDto.setFormat("ext3");
        baseDiskDto.setPath(Vars.vmHardPath);
        baseDiskDto.setSize(1024);
        baseDiskDto.setType("disk");
        Collection<TaskDiskDto> diskDtoCollection = new ArrayList<TaskDiskDto>(1);
        diskDtoCollection.add(baseDiskDto);
        {
            ExtendedTaskDto taskType = new ExtendedTaskDto();
            taskType.setTaskName("MemIntensive");
            taskType.setServiceName("MemIntensive");
            taskType.setRequestedCores(5);
            taskType.setCpuWeight(0.2f);
            taskType.setMemWeight(0.8f);
            taskType.setRequestedCPUMax(500);
            taskType.setRequestedCPUMin(20);
            taskType.setRequestedMemoryMax(3000);
            taskType.setRequestedMemoryMin(2000);
            taskType.setDiskDtoCollection(diskDtoCollection);
            possibleTaskPool.add(taskType);
        }
        {
            ExtendedTaskDto taskType = new ExtendedTaskDto();
            taskType.setTaskName("CpuIntensive");
            taskType.setServiceName("CpuIntensive");
            taskType.setRequestedCores(8);
            taskType.setCpuWeight(0.8f);
            taskType.setMemWeight(0.2f);
            taskType.setRequestedCPUMax(2000);
            taskType.setRequestedCPUMin(1500);
            taskType.setRequestedMemoryMax(600);
            taskType.setRequestedMemoryMin(100);
            taskType.setDiskDtoCollection(diskDtoCollection);
            possibleTaskPool.add(taskType);
        }
        {
            ExtendedTaskDto taskType = new ExtendedTaskDto();
            taskType.setTaskName("IOIntensive");
            taskType.setServiceName("IOIntensive");
            taskType.setRequestedCores(5);
            taskType.setCpuWeight(0.5f);
            taskType.setMemWeight(0.5f);
            taskType.setRequestedCPUMax(700);
            taskType.setRequestedCPUMin(100);
            taskType.setRequestedMemoryMax(600);
            taskType.setRequestedMemoryMin(100);
            taskType.setDiskDtoCollection(diskDtoCollection);
            possibleTaskPool.add(taskType);
        }
        {
            ExtendedTaskDto taskType = new ExtendedTaskDto();
            taskType.setTaskName("MatrixMultiplication");
            taskType.setServiceName("MatrixMultiplication");
            taskType.setRequestedCores(6);
            taskType.setCpuWeight(0.6f);
            taskType.setMemWeight(0.4f);
            taskType.setRequestedCPUMax(700);
            taskType.setRequestedCPUMin(100);
            taskType.setRequestedMemoryMax(1000);
            taskType.setRequestedMemoryMin(505);
            taskType.setDiskDtoCollection(diskDtoCollection);
            possibleTaskPool.add(taskType);
        }
        {
            ExtendedTaskDto taskType = new ExtendedTaskDto();
            taskType.setTaskName("Sort");
            taskType.setServiceName("Sort");
            taskType.setRequestedCores(4);
            taskType.setCpuWeight(0.7f);
            taskType.setMemWeight(0.3f);
            taskType.setRequestedCPUMax(1500);
            taskType.setRequestedCPUMin(700);
            taskType.setRequestedMemoryMax(500);
            taskType.setRequestedMemoryMin(200);
            taskType.setDiskDtoCollection(diskDtoCollection);
            possibleTaskPool.add(taskType);
        }
        {
            ExtendedTaskDto taskType = new ExtendedTaskDto();
            taskType.setTaskName("Search");
            taskType.setServiceName("Search");
            taskType.setRequestedCores(4);
            taskType.setCpuWeight(0.6f);
            taskType.setMemWeight(0.4f);
            taskType.setRequestedCPUMax(1700);
            taskType.setRequestedCPUMin(1000);
            taskType.setRequestedMemoryMax(800);
            taskType.setRequestedMemoryMin(400);
            taskType.setDiskDtoCollection(diskDtoCollection);
            possibleTaskPool.add(taskType);
        }

    }

    @Override
    protected void setup() {
    	//modified this line - the path
        //generatedSchedule = new File("generated_schedule_" + new Date().toString().replace(' ', '_') + ".xml");
    	generatedSchedule = new File("generated_schedule_.xml");
    	if(!generatedSchedule.isFile()){
    		try {
				generatedSchedule.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
        workloadSchedule = new WorkloadSchedule();
        workloadSchedule.setApplications(new JaxbPair[0]);
        addBehaviour(new ReceiveMessagesWorkloadSchedulerBehavior(this));

        Object[] receivedData = getArguments();

        if (receivedData != null && receivedData.length > 0) {
            Serializable[] args = (Serializable[]) receivedData[0];
            for (Object arg : args) {
                String argInfo[] = ((String) arg).split("=");
                if (argInfo[0].equals("generationTimeMin")) {
                    WORKLOAD_GENERATION_INTERVAL_MIN = Integer.parseInt(argInfo[1]);
                } else if (argInfo[0].equals("generationTimeMax")) {
                    WORKLOAD_GENERATION_INTERVAL_MAX = Integer.parseInt(argInfo[1]);
                } else if (argInfo[0].equals("serviceLifeMax")) {
                    TASK_DURATION_INTERVAL_MAX = Integer.parseInt(argInfo[1]);
                } else if (argInfo[0].equals("serviceLifeMin")) {
                    TASK_DURATION_INTERVAL_MIN = Integer.parseInt(argInfo[1]);
                } else if (argInfo[0].equals("generatedServicesNoMax")) {
                    NUMBER_OF_GENERATED_TASKS_MAX = Integer.parseInt(argInfo[1]);
                } else if (argInfo[0].equals("generatedServicesNoMin")) {
                    NUMBER_OF_GENERATED_TASKS_MIN = Integer.parseInt(argInfo[1]);
                }
            }
        }
        int auxMax = WORKLOAD_GENERATION_INTERVAL_MAX;
        int auxMin = WORKLOAD_GENERATION_INTERVAL_MIN;
        WORKLOAD_GENERATION_INTERVAL_MAX = BEGINNING_ARRIVAL;
        WORKLOAD_GENERATION_INTERVAL_MIN = BEGINNING_ARRIVAL;
        initializeGenerateTasksBehaviour();
        WORKLOAD_GENERATION_INTERVAL_MAX = auxMax;
        WORKLOAD_GENERATION_INTERVAL_MIN = auxMin;
        agentCreationDate = new Date();
        agent = this;
    }

    @Override
    protected void takeDown() {
        pauseGenerating = true;
    }

    private void initializeGenerateTasksBehaviour() {
        if (pauseGenerating) {
            return;
        }
        final Timer timer = new Timer(true);

        TimerTask task = new TimerTask() {
            int executionCount = 0;

            public void run() {
                int generatedTaskNo = random.nextInt(NUMBER_OF_GENERATED_TASKS_MAX - NUMBER_OF_GENERATED_TASKS_MIN) + NUMBER_OF_GENERATED_TASKS_MIN;
                List<ExtendedTaskDto> generatedTasks = new ArrayList<ExtendedTaskDto>(generatedTaskNo);
                int maximumTaskType = possibleTaskPool.size();
                for (int i = 0; i < generatedTaskNo; i++) {
                    int generatedTaskTypeListIndex = random.nextInt(maximumTaskType);
                    int timeToLive = random.nextInt(TASK_DURATION_INTERVAL_MAX - TASK_DURATION_INTERVAL_MIN) + TASK_DURATION_INTERVAL_MIN;

                    ExtendedTaskDto generated = null;
                    try {
                        generated = (ExtendedTaskDto) (possibleTaskPool.get(generatedTaskTypeListIndex)).clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    int generatedTaskCount = generatedTaskIndex++;
                    generated.setTaskName("/Task_" + generatedTaskCount);
                    String ip = "192.168.1." + (generatedTaskCount + 100);//TODO mai generic ipu
//                    generated.setTaskName(generated.getTaskName() + "_Generated_Index_" + generatedTaskCount);
                    generated.setIp(ip);
                    generated.setVncPort(generatedTaskCount);
                    generated.setLife(timeToLive);

                    generatedTasks.add(generated);
                    addActivityAndPersistSchedule(generated);
                }

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                try {
                    msg.setContentObject(new Object[]{"Tasks added", generatedTasks});
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
//                msg.addReceiver(new AID(GlobalVars.RLAGENT_NAME + "@" + agent.getContainerController().getPlatformName()));
//                agent.send(msg);
                initializeGenerateTasksBehaviour();
            }
        };

        int generationTimerDelay = random.nextInt(WORKLOAD_GENERATION_INTERVAL_MAX - WORKLOAD_GENERATION_INTERVAL_MIN + 1) + WORKLOAD_GENERATION_INTERVAL_MIN;
        //System.out.println("Delaaaaaaaaaaay" + generationTimerDelay);
        timer.schedule(task, generationTimerDelay * 1000);

    }

    private void addActivityAndPersistSchedule(ExtendedTaskDto extendedTaskDto) {
        ApplicationDescription applicationDescription = new ApplicationDescription();
        applicationDescription.setName(extendedTaskDto.getTaskName() + "Application");
        ActivityDescription activityDescription = new ActivityDescription();
        activityDescription.setName(extendedTaskDto.getTaskName() + "Activity");
        ActivityService activityService = new ActivityService();

        activityService.setServiceName(extendedTaskDto.getServiceName());
        activityService.setCpuWeight(extendedTaskDto.getCpuWeight());
        activityService.setIp(extendedTaskDto.getIp());
        activityService.setMemWeight(extendedTaskDto.getMemWeight());
        activityService.setRequestedCoresCount(extendedTaskDto.getRequestedCores());
        activityService.setRequestedCPUMax(extendedTaskDto.getRequestedCPUMax());
        activityService.setRequestedCPUMin(extendedTaskDto.getRequestedCPUMin());
        activityService.setRequestedMemoryMax(extendedTaskDto.getRequestedMemoryMax());
        activityService.setRequestedMemoryMin(extendedTaskDto.getRequestedMemoryMin());
        activityService.setRequestedStorageMax(extendedTaskDto.getRequestedStorageMax());
        activityService.setRequestedStorageMin(extendedTaskDto.getRequestedStorageMin());
        activityService.setStorageWeight(extendedTaskDto.getCpuWeight());
        activityService.setVncPort(extendedTaskDto.getVncPort());
        activityDescription.setDuration(extendedTaskDto.getLife());
        activityDescription.setServices(new ActivityService[]{activityService});

        ArrayList<TaskDiskDto> taskDiskDtos = (ArrayList<TaskDiskDto>) extendedTaskDto.getDiskDtoCollection();

        int disksCount = taskDiskDtos.size();
        VirtualDiskDescription[] virtualDisks = new VirtualDiskDescription[disksCount];

        for (int i = 0; i < disksCount; i++) {
            TaskDiskDto taskDiskDto = taskDiskDtos.get(i);
            VirtualDiskDescription virtualDiskDescription = new VirtualDiskDescription();
            virtualDiskDescription.setFormat(taskDiskDto.getFormat());
            virtualDiskDescription.setSize(taskDiskDto.getSize());
            virtualDiskDescription.setTarget(taskDiskDto.getFormat());
            virtualDiskDescription.setType(taskDiskDto.getType());
            virtualDiskDescription.setSource(taskDiskDto.getPath());
            virtualDisks[i] = virtualDiskDescription;
        }

        activityService.setDisks(virtualDisks);
        applicationDescription.setActivities(new ActivityDescription[]{activityDescription});

        JaxbPair[] oldPairs = workloadSchedule.getApplications();
        int oldPairsSize = oldPairs.length;
        JaxbPair[] newPairs = new JaxbPair[oldPairsSize + 1];
        System.arraycopy(oldPairs, 0, newPairs, 0, oldPairsSize);
        long delay = new java.util.Date().getTime() - agentCreationDate.getTime();
        JaxbPair newApplication = new JaxbPair(delay / 1000, applicationDescription); 
        newPairs[oldPairsSize] = newApplication;

        workloadSchedule.setApplications(newPairs);

        try {
            JAXBContext context = JAXBContext.newInstance(WorkloadSchedule.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            if(generatedSchedule.isFile()){
            	if(generatedSchedule.canWrite())
            		marshaller.marshal(workloadSchedule, generatedSchedule);
            }
            // fire an event
            BaseCommon.Instance().getTaskGeneratedEvent().setChanged();
            BaseCommon.Instance().getTaskGeneratedEvent().notifyObservers(newApplication);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPauseGenerating(boolean pauseGenerating) {
        this.pauseGenerating = pauseGenerating;
        if (!pauseGenerating) {
            initializeGenerateTasksBehaviour();
        }
    }
}
