package Adventure;

// bossroom继承room，在room的基础上添加了boss和survive，用于储存这个房间是否有boss和玩家是否有机会从boss下存活
public class bossroom extends room{
    private boolean boss = false;
    public boolean survive;

    // 两种实例化，boss一开始都默认为false，实例化时可用选择是否更改boss，后续也可以通过put()函数来更改
    public bossroom(String name, String introduction, boolean survive)
    {
        super(name, introduction);
        this.survive = survive;
    }

    public bossroom(String name, String introduction, boolean boss, boolean survive)
    {
        super(name, introduction);
        this.boss = boss;
        this.survive = survive;
    }

    // 用于向bossroom中添加boss（boss = true）
    public void put()
    {
        this.boss = true;
    }

    // 由于返回房间中是否有boss
    public boolean check()
    {
        return this.boss;
    }

}
