import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    static ArrayList <ArrayList<String>> args = new ArrayList <>();
    static ArrayList<String> cmd = new ArrayList <>();
    static ArrayList<String> is_redirect = new ArrayList <>();

    //Setting the commands and the number of their arguments in hash map
    public static HashMap setCommands() {
        HashMap <String, Integer> map = new HashMap<>();
        map.put("cd",-1);
        map.put("ls",-1);
        map.put("cp",2);
        map.put("cat",-1);
        map.put("more",1);
        map.put("mkdir",1);
        map.put("rmdir",1);
        map.put("mv",2);
        map.put("rm",1);
        map.put("args",1);
        map.put("date",0);
        map.put("help",0);
        map.put("pwd",0);
        map.put("clear",0);
        map.put("exit",0);

        return map;

    }

    //Setting the entered commands and their arguments into the array lists
    public static void setArgs(String input) {
        String [] tempArgs;
        String [] tempCmds;
        tempCmds=input.split(" \\| ");
        for(int z = 0; z < tempCmds.length; z++){
            if(tempCmds[z].contains(">") && !tempCmds[z].contains(">>")) is_redirect.add(">");
            else if(tempCmds[z].contains(">>")) is_redirect.add(">>");
            else is_redirect.add("0");

            tempArgs = tempCmds[z].split("\\s");
            cmd.add(tempArgs[0]);
            ArrayList <String> Shallow = new ArrayList <>();
            args.add(Shallow);
            for(int i = 1; i < tempArgs.length; i++){
                if(!tempArgs[i].equalsIgnoreCase(">")&&
                   !tempArgs[i].equalsIgnoreCase(">>"))
                    args.get(z).add(tempArgs[i]);
            }
        }

    }

    //Validating if the entered command is correct and its arguments
    public static boolean parse(String input) {
        String commandName;
        boolean cmd_flag = false, parameters_flag = false, bigFlag = false;
        int commandArguments = 0;
        HashMap <String, Integer> map = setCommands();
        setArgs(input);
        for(int j = 0; j < cmd.size(); j++){
            if(map.containsKey(cmd.get(j))){
                cmd_flag = true;
                commandArguments = map.get(cmd.get(j));
                if (is_redirect.get(j).equalsIgnoreCase(">")
                || is_redirect.get(j).equalsIgnoreCase(">>")) {
                    if (((commandArguments == -1) &&
                       (args.get(j).size() >= 2 ||
                        args.get(j).size() == 1))||
                       (commandArguments + 1 == args.get(j).size()))
                        parameters_flag = true;
                    else parameters_flag = false;
                }
                else {
                    if (((commandArguments == -1)&&
                       (args.get(j).size() >= 1 ||
                        args.get(j).isEmpty()))||
                       (commandArguments == args.get(j).size()))
                        parameters_flag = true;
                    else parameters_flag = false;
                }
            }
            else cmd_flag = false;
            if(cmd_flag && parameters_flag) bigFlag = true;
            else {
                is_redirect.remove(j);
                commandName = cmd.get(j);
                cmd.remove(j);
                args.remove(j);
                if (!cmd_flag) System.out.println("Error, couldn't recognized the command");
                else if(!parameters_flag)
                {
                    if(commandArguments == -1)
                        System.out.println("Error, "+ commandName +" requires 0 or 1 Arguments.");
                    else
                        System.out.println("Error, "+ commandName +" requires "+ commandArguments +" Arguments.");
                }
            }
        }

        if(bigFlag) return true;
        else return false;
    }

    //Returns the commands list
    public static ArrayList<String> getCmd(){
        return cmd;
    }

    //Returns array contains redirect operator
    public static ArrayList<String> getRedirect(){
        return is_redirect;
    }

    //Returns the Arguments list
    public static ArrayList <ArrayList<String>> getArguments() {
        return args;
    }
}