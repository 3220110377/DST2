package Adventure;

import java.util.Random;
import java.util.Scanner;

public class game {
    
    public room now;
    public boolean hasKey = false;
    public boolean hasSword = false;
    public boolean conti = true;
    public boolean scroll = false;
    public boolean pass = false;

    // part 1 的初始化 （包括实例化，增加房间的连接，添加房间线索，确定初始为止，开局欢迎语）
    public void initialize()
    {
        room entrance = new room("entrance","This is a mysterious castle\nYou stood in the doorway and seemed to hear the dragon's low roar");
        room lobby1 = new room("First floor lobby","The hall is gorgeous\nYou look around and find two rooms and a spiral staircase leading to the second floor");
        room lobby2 = new room("Second floor lobby","Here again you hear the low roar of the dragon\nBut this time it was closer\ncloser...");
        room library = new room("library","There are many books in the library\nPerhaps you can find some information about the castle");
        room bedroom = new room("bedroom","You can tell by the decoration that this is a princess's bedroom\nBut where is the princess?\nMaybe we can find some clues in this room");
        room empty = new room("empty room", "This looks like an empty room\nBut always let you have a kind of unspeakable feeling\nIs there something hidden here?");
        lockedroom locked = new lockedroom("locked room", "You found the legendary sword\nIt has a powerful magic and seems to be able to overcome anything");
        bossroom boss = new bossroom("boss", null, true, true);

        entrance.setexit("north", lobby1);
        lobby1.setexit("south", entrance);
        lobby1.setexit("west", library);
        lobby1.setexit("east", empty);
        lobby1.setexit("up", lobby2);
        library.setexit("east", lobby1);
        empty.setexit("west", lobby1);
        lobby2.setexit("down", lobby1);
        lobby2.setexit("east", bedroom);
        lobby2.setexit("south", boss);
        lobby2.setexit("west", locked);
        bedroom.setexit("west", lobby2);
        locked.setexit("east", lobby2);

        empty.clue("You triggered the trigger, and a secret tunnel appeared\nIt led to a secret chamber");
        library.clue("From the book, you know that the castle has a magical sword hidden in it\nIt can defeat all the evil creatures in the world");
        bedroom.clue("You found the princess's diary\nYou know from your diary that many years ago, an evil dragon captured the princess and imprisoned her in this castle\nYou want to save the princess, but how do you deal with the dragon?");

        now = entrance;

        System.out.println();
        System.out.println("Welcome to adventure games");
        System.out.println("Explore the castle to the fullest");
        System.out.println("If you need help, type 'help'");
    }

    // 用于给玩家输出当前位置，有哪些方向可以选择
    public void printnow()
    {
        now.printnow();
    }

    // search用于在场景中搜索线索，如果没有线索会返回提示，搜索过程中可以会触发机关、找到关键道具、触发背景故事介绍等
    public void search()
    {
        // empty room （当在empty room里面search时会触发机关，出现一个新房间secret_chamber）
        if (now.toString() == "empty room") {
            room secret_chamber = new room("secret chamber","You found a secret chamber\nYou found a key on the table in the chamber of Secrets\nIt seems to be used to open inaccessible places");
            now.setexit("north", secret_chamber);
            secret_chamber.setexit("south", now);

            // 输出部分（线索，可选择方向）
            System.out.println(now.clue);
            System.out.print("The directions you can choose are: ");
            System.out.print(now.getexit());
            System.out.println("\n");

        // 没有线索则给出提示
        }else if (now.clue == null) {
            System.out.println("No useful clues were found");
        }else{
            // 这是part2中的scroll，用于打开大门
            if (now.clue == "Congratulations! You found the scroll!") {
               scroll = true; 
            }
            System.out.println(now.clue);
        }
    }

    // 当玩家输入不符合我们的预期时触发，返回可用的指令、当前位置、可选择的方向
    public void help()
    {
        now.help();
    }

    // 移动相关的代码
    public void move(String direction)
    {
        room nextroom = now.getexit(direction);

        if (nextroom == null) {
            System.out.println("There are no doors!");
            printnow();
        }else if (nextroom instanceof lockedroom) {
            // 对于lockedroom，首先确认它是否被开启（available）
            // 如果已经被打开（available=true）则移动
            // 如果没有被打开（available=false）则判断是否拥有钥匙，如果有钥匙则打开门（available=true），如果没有钥匙则返回提示

            lockedroom lockedroom = (lockedroom) nextroom;
            boolean available = lockedroom.check();
            if (!available) {
                if (hasKey) {
                    lockedroom.open();
                    nextroom = lockedroom;
                    now = nextroom;
                    printnow();
                    hasSword = true;
                }else{
                    System.out.println("The room is locked. You may need a KEY to open it");
                }
            }else{
                nextroom = lockedroom;
                now = nextroom;
                printnow();
            }      
        }else if (nextroom instanceof sealedroom) {

            // 对于sealedroom，确认是否拥有scroll，如果有则询问是否打开，打开后游戏结束。如果没有scroll则返回提示。
            sealedroom sealedroom = (sealedroom) nextroom;
            if (scroll) {
                System.out.println("You now have a scroll, choose to open the door or not\nPlease enter 'yes' or 'no'");
                Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            if (line.equals("yes")) {
                sealedroom.open();
                nextroom = sealedroom;
                conti = false;
                System.out.println("Congratulations! You have successfully rescued the princess and become a hero!");
            }
            in.close();
            }else{
                System.out.println("It is sealed. You may need a scroll to open it");
            }
                
        }else if (nextroom instanceof bossroom) {

            // 对于bossroom，首先确认里面是否有boss，如果没有则返回提示，如果有则继续判断是否有几率能够存活
            // 如果不可能存活，则被杀死，游戏结束；如果有几率能够存活则判断是否拥有Sword
            // 如果拥有Sword，则杀死boss，找到公主，结束part1，开启part2；如果没有Sword，则被杀死，游戏结束。
            bossroom bossroom = (bossroom) nextroom;
            boolean boss = bossroom.check();
            if (boss) {
                if (bossroom.survive) {
                    if (hasSword) {
                        System.out.println("With the sword, you successfully defeated the dragon and saved the princess!\nNow you can escape the castle with the princess");
                        pass = true;
                    }else{
                        System.out.println("The dragon is too powerful. You tried your best and failed\nBut you believe that if you can get stronger, even a little, you can defeat the dragon");
                    }
                    conti = false;
                }else{
                    System.out.println("Unfortunately, you came across a room with a monster. You were killed and the princess was taken away.");
                    conti = false;
                }
            }else{
                System.out.println("You're lucky there aren't any monsters in this room");
                now = nextroom;
                printnow();
            }
        }else{

            // 对于secret chamber，进入会获得钥匙
            if (nextroom.toString().equals("secret chamber")) {
                hasKey = true;
            }

            // 对于普通房间则直接移动
            now = nextroom;
            printnow();
        }
    }

    // 这是游戏的主循环，叫做loop,用于识别玩家的输入，然后运行指令。并且通过conti来判断游戏是否还在进行
    public static void loop(game game, Scanner in)
    {
        while (game.conti) {
            String line = in.nextLine();
            String[] words = line.split(" ");
            if (words.length == 2) {
                if (words[0].equals("go")) {
                    game.move(words[1]);
                }else{
                    game.help();
                }
            }else if (words.length == 1) {
                if (words[0].equals("search")) {
                    game.search();
                    }else{
                        game.help();
                    }
                }else if (words[0].equals("bye")) {
                    break;
            }else{
                game.help();
            }
        }
    }   

    // part 2 的初始化 （包括实例化，增加房间的连接，添加房间线索，确定初始为止，part2的欢迎语）
    public void reinitialize()
    {
        sealedroom entrance = new sealedroom("entrance",null);
        room lobby1 = new room("First floor lobby",null);
        room lobby2 = new room("Second floor lobby",null);
        bossroom library = new bossroom("library", null, false);
        bossroom bedroom = new bossroom("bedroom", null, false);
        bossroom empty = new bossroom("empty room", null, false);
        bossroom locked = new bossroom("locked room", "This is where you got the sword", false);
        room boss = new room("boss", "This is where you defeat the dragon");
        bossroom secret_chamber = new bossroom("secret chamber","This is where you get the key", false);
        
        bossroom[] rooms = {library, bedroom, empty, locked, secret_chamber};
        Random random = new Random();

        int index1 = random.nextInt(5); 
        rooms[index1].put();
        
        int index2 = random.nextInt(5);
        rooms[index2].clue("Congratulations! You found the scroll!");

        entrance.setexit("north", lobby1);
        lobby1.setexit("south", entrance);
        lobby1.setexit("west", library);
        lobby1.setexit("east", empty);
        lobby1.setexit("up", lobby2);
        library.setexit("east", lobby1);
        empty.setexit("west", lobby1);
        lobby2.setexit("down", lobby1);
        lobby2.setexit("east", bedroom);
        lobby2.setexit("south", boss);
        lobby2.setexit("west", locked);
        bedroom.setexit("west", lobby2);
        locked.setexit("east", lobby2);
        empty.setexit("north", secret_chamber);
        secret_chamber.setexit("south", empty);
        boss.setexit("north", lobby2);

        now = boss;

        System.out.println("The dragon was killed by you, but it sealed the gate with its last strength");
        System.out.println("Fortunately, the princess tells you that there is a scroll in the castle that has the power to break the dragon's seal and escape\nThe scroll needs to be \t\t'searched'\t in the room to find it");
        System.out.println("Unfortunately, one of the dragon's men know you've killed the dragon, and they've come to the castle. They're hiding in a room in the castle");
        System.out.println("Since you were wounded in the battle with the dragon, you lost your fighting power. You just have to pray that luck will be on your side and you'll be lucky enough to avoid those rooms");

        printnow();
    }


    // 这是主体部分，在part1 结束后，根据pass判断是通过了part1还是被杀死的，从而判断是否开始part2
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        game part1 = new game();
        part1.initialize();
        loop(part1, in);
        if (part1.pass) {
            game part2 = new game();
            part2.reinitialize();
            loop(part2, in);
        }
        in.close();
        System.out.println("The game is over, look forward to our next meeting!");
    }
}

