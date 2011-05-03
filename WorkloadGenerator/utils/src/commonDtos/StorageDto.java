package commonDtos;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 8, 2010
 * Time: 10:52:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class StorageDto implements Serializable {
    public String name;
    public int usedStorage;
    public int maximumStorage;
    public int optimumStorage;


    public StorageDto(String name, int size, int usedStorage) {
        this.name = name;
        this.usedStorage = usedStorage;
    }

    public StorageDto() {
    }

    public int getOptimumStorage() {
        return optimumStorage;
    }

    public void setOptimumStorage(int optimumStorage) {
        this.optimumStorage = optimumStorage;
    }

    public int getMaximumStorage() {
        return maximumStorage;
    }

    public void setMaximumStorage(int maximumStorage) {
        this.maximumStorage = maximumStorage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getUsedStorage() {
        return usedStorage;
    }

    public void setUsedStorage(int usedStorage) {
        this.usedStorage = usedStorage;
    }


}
