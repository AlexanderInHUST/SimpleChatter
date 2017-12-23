package file;

/**
 * Created by tangyifeng on 2017/12/20.
 * Email: yifengtang_hust@outlook.com
 */
public class PackageBrokenException extends Exception {

    private String failPlace;

    public PackageBrokenException(String failPlace) {
        super();
        this.failPlace = failPlace;
    }

    public String getFailPlace() {
        return failPlace;
    }
}
