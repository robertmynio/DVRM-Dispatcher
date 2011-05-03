package commonDtos;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: oneadmin
 * Date: Nov 15, 2010
 * Time: 11:48:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskDiskDto implements Serializable {
    private int size;
    private String format;
    private String type;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
