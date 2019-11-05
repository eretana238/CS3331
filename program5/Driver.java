/**
 * Driver
 */
public class Driver {

    private String name;
    private DriverType type;
    // constructor
    Driver(String name, DriverType type){
        this.name = name;
        this.type = type;
    }

    public String getName(){
        return name;
    }
    public DriverType getType(){
        return type;
    }
}