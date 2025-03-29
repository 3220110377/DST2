package Adventure;

// sealedroom继承room，在room的基础上添加了available，用于判断是否能够通过，available 默认为false
public class sealedroom extends room{
    private boolean available = false;

    public sealedroom(String name, String introduction)
    {
        super(name, introduction);
    }

    // 打开sealedroom（available = true）
    public void open()
    {
        this.available = true;
    }

    public boolean check()
    {
        return available;
    }
}
