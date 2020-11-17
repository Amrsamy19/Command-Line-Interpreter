import java.io.IOException;
import java.util.Scanner;

/**
 * Team Members:
 *  Amr Samy - ID: 20180187
 *  Amr Ayman - ID: 20180374
 *  Rawan Magdy - ID: 20180107
 *  Yousef Muhammed - ID: 20180352
 * 
 * Github repo's link -> https://github.com/Amrsamy19/Command-Line-Interpreter
 */

public class Main {
    static String currentDirectory = System.getProperty("user.dir");
    static String userInput;

    //Checks which command should be preformed
    public static void commandsExecutor() throws IOException {
        String commandsOutput = "";
        for(int i = 0; i < Parser.cmd.size(); i++) {
            switch (Parser.cmd.get(i)) {
                case "cd":{
                    if(Parser.args.get(i).isEmpty()){
                        commandsOutput = Terminal.pwd();
                    }
                    else
                        Terminal.cd(Parser.args.get(i).get(0));
                    break;
                }
                case "cp":
                    Terminal.cp(Parser.args.get(i).get(0), Parser.args.get(i).get(1));
                    break;
                case "cat":
                    commandsOutput += Terminal.cat(Parser.args.get(i)) + "\n";
                    break;
                case "mv":
                    Terminal.mv(Parser.args.get(i).get(0), Parser.args.get(i).get(1));
                    break;
                case "args":
                    commandsOutput = Terminal.args(Parser.args.get(i).get(0));
                    break;
                case "help":
                    commandsOutput = Terminal.help();
                    break;
                case "mkdir":
                    Terminal.mkdir(Parser.args.get(i).get(0));
                    break;
                case "rmdir":
                    commandsOutput = Terminal.rmdir(Parser.args.get(i).get(0));
                    break;
                case "rm":
                    Terminal.rm(Parser.args.get(i).get(0));
                    break;
                case "more":
                    commandsOutput = Terminal.more(Parser.args.get(i).get(0));
                    break;
                case "pwd":
                    commandsOutput = Terminal.pwd();
                    break;
                case "clear":
                    Terminal.clear();
                    break;
                case "date":
                    commandsOutput = Terminal.date();
                    break;
                case "ls": {
                    if (Parser.args.get(i).size() == 0)
                        commandsOutput = Terminal.ls();

                    else if (Parser.args.get(i).size() == 1){
                        if (Parser.args.get(i).get(0).contains(".txt"))
                            commandsOutput = Terminal.ls();
                        else
                            commandsOutput = Terminal.ls(Parser.args.get(i).get(0));
                    }
                    else if (Parser.args.get(i).size() == 2)
                        if(Parser.args.get(i).get(1).contains(".txt"))
                            commandsOutput = Terminal.ls(Parser.args.get(i).get(0));
                    break;
                }
            }
            //Checks if there is a single redirect
            if(Parser.is_redirect.get(i).equalsIgnoreCase(">")){
                if(Parser.args.get(i).isEmpty())
                    System.out.println("Error, you must write the file name");
                if(Parser.args.get(i).size() == 1)
                    Terminal.writeToFile(commandsOutput, Parser.args.get(i).get(0));
                if(Parser.args.get(i).size() == 2)
                    Terminal.writeToFile(commandsOutput, Parser.args.get(i).get(1));
                if(Parser.args.get(i).size() > 2)
                    Terminal.writeToFile(commandsOutput, Parser.args.get(i).get(Parser.args.get(i).size() - 1));
            }
            //Checks if there is a double redirect
            else if(Parser.is_redirect.get(i).equalsIgnoreCase(">>")){
                if(Parser.args.get(i).isEmpty())
                    System.out.println("Error, you must write the file name");
                if(Parser.args.get(i).size() == 1)
                    Terminal.appendToFile(commandsOutput, Parser.args.get(i).get(0));
                if(Parser.args.get(i).size() == 2)
                    Terminal.appendToFile(commandsOutput, Parser.args.get(i).get(1));
                if(Parser.args.get(i).size() > 2)
                    Terminal.appendToFile(commandsOutput, Parser.args.get(i).get(Parser.args.get(i).size() - 1));
            }
            else System.out.println(commandsOutput);
        }
    }

    public static void cliRunner() throws IOException {
        Scanner readingInput = new Scanner(System.in);
        while(true){
            System.out.print(currentDirectory + ">");
            userInput = readingInput.nextLine();
            if(userInput.equals("exit"))
                return;
            else if(Parser.parse(userInput)){
                commandsExecutor();
                Parser.cmd.clear();
                Parser.args.clear();
                Parser.is_redirect.clear();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        cliRunner();
    }
}