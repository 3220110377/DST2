package Adventure;

// lockedroom继承room，在room的基础上添加了available，用于判断是否能够通过，available 默认为false
public class lockedroom extends room{
    private boolean available = false;

    public lockedroom(String name, String introduction)
    {
        super(name, introduction);
    }

    public void open()
    {
        this.available = true;
    }

    public boolean check()
    {
        return available;
    }
}
