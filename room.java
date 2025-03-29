package Adventure;

import java.util.HashMap;

// 父类room，拥有name，introduction，clue，并且采用容器HashMap来储存相通的房间
public class room {
    private String name;
    private String introduction;
    protected String clue;
    private HashMap<String, room> exits = new HashMap<String, room>();

    public room(String name, String introduction)
    {
        this.name = name;
        this.introduction = introduction;
    }

    public void clue(String clue)
    {
        this.clue = clue;
    }

    // 设定可以走的方向和相通的房间
    public void setexit(String direction, room room)
    {
        exits.put(direction, room);
    }
    
    // 从HashMap中获取可用走的方向
    public String getexit()
    {
        StringBuffer exit = new StringBuffer();
        for( String direction : exits.keySet()){
            exit.append(direction+" ");
        }
        return exit.toString();
    }

    // 如果在getexit中输入了方向，则返回对应的房间
    public room getexit(String direction){
        return exits.get(direction);
    }

    // 用于给玩家输出当前位置，有哪些方向可以选择
    public void printnow()
    {
        System.out.println("You're in "+ this.name + " now");
        if (this.introduction!= null) {
            System.out.println(this.introduction);
        }
        System.out.print("The directions you can choose are: ");
        System.out.print(this.getexit());
        System.out.println("\n");
    }

    // 当玩家输入不符合我们的预期时触发，返回可用的指令、当前位置、可选择的方向
    public void help()
    {
        System.out.println("Are you lost? Some of the things you can do are:\ngo（Choose a direction to go） search（Explore in the current area）\nhelp（Ask for help and I'll show you where you are and where you can go） \nbye（Leave the game, but there will be no save）");
        System.out.println("for example:\tgo north");
        System.out.println("You're in "+ this.name + "now");
        System.out.print("The directions you can choose are:");
        System.out.print(this.getexit());
        System.out.println("\n");
    }

    @Override
    public String toString()
    {
        return name;
    }
}