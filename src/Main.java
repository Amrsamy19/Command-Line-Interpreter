import java.io.IOException;
import java.util.Scanner;

public class Main {
    static String currentDirectory = System.getProperty("user.dir");
    static String userInput;

    public static void commandsExecutor() throws IOException {
        String commandsOutput = "";
        for(int i = 0; i < Parser.cmd.size(); i++) {
            switch (Parser.cmd.get(i)) {
                case "cd":{
                    if(Parser.args.get(i).isEmpty()){
                        commandsOutput = Terminal.pwd();
                        System.out.println(commandsOutput);
                    }
                    else
                        Terminal.cd(Parser.args.get(i).get(0));
                    break;
                }
                case "cp":
                    Terminal.cp(Parser.args.get(i).get(0), Parser.args.get(i).get(1));
                    break;
                case "cat":
                    commandsOutput = Terminal.cat(Parser.args.get(i));
                    System.out.println(commandsOutput);
                    break;
                case "mv":
                    Terminal.mv(Parser.args.get(i).get(0), Parser.args.get(i).get(1));
                    break;
                case "args":
                    commandsOutput = Terminal.args(Parser.args.get(i).get(0));
                    System.out.println(commandsOutput);
                    break;
                case "help":
                    commandsOutput = Terminal.help();
                    System.out.println(commandsOutput);
                    break;
                case "mkdir":
                    Terminal.mkdir(Parser.args.get(i).get(0));
                    break;
                case "rmdir":
                    commandsOutput = Terminal.rmdir(Parser.args.get(i).get(0));
                    System.out.println(commandsOutput);
                    break;
                case "rm":
                    Terminal.rm(Parser.args.get(i).get(0));
                    break;
                case "more":
                    commandsOutput = Terminal.more(Parser.args.get(i).get(0));
                    break;
                case "pwd":
                    commandsOutput = Terminal.pwd();
                    System.out.println(commandsOutput);
                    break;
                case "clear":
                    Terminal.clear();
                    break;
                case "date":
                    commandsOutput = Terminal.date();
                    System.out.println(commandsOutput);
                    break;
                case "ls": {
                    if (Parser.args.get(i).size() == 0){
                        commandsOutput = Terminal.ls();
                        System.out.println(commandsOutput);
                    }

                    else if (Parser.args.get(i).size() == 1){
                        if (Parser.args.get(i).get(0).contains(".txt")){
                            commandsOutput = Terminal.ls();
                            System.out.println(commandsOutput);
                        }
                        else{
                            commandsOutput = Terminal.ls(Parser.args.get(i).get(0));
                            System.out.println(commandsOutput);
                        }
                    }
                    else if (Parser.args.get(i).size() == 2)
                        if(Parser.args.get(i).get(1).contains(".txt")){
                            commandsOutput = Terminal.ls(Parser.args.get(i).get(0));
                            System.out.println(commandsOutput);
                        }
                    break;
                }
            }
            if(Parser.is_redirect.get(i).equalsIgnoreCase(">")){
                if(Parser.args.get(i).size() == 1)
                    Terminal.writeToFile(commandsOutput, Parser.args.get(i).get(0));
                if(Parser.args.get(i).size() == 2)
                    Terminal.writeToFile(commandsOutput, Parser.args.get(i).get(1));
            }
            if(Parser.is_redirect.get(i).equalsIgnoreCase(">>")){
                if(Parser.args.get(i).size() == 1)
                    Terminal.appendToFile(commandsOutput, Parser.args.get(i).get(0));
                if(Parser.args.get(i).size() == 2)
                    Terminal.appendToFile(commandsOutput, Parser.args.get(i).get(1));
            }
        }
    }

    public static void cliRunner() throws IOException {
        Scanner readingInput = new Scanner(System.in);
        boolean hasPipe = false;
        boolean hasRedirect = false;
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