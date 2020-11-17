import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Terminal {
    public static String help() {
        return "cd: Changes current directory to another one."+
        "\nls: List all the directory contents."+
        "\ncp: Copies file from current directory to another."+
        "\ncat: Concatenates files and display the content of the file."+
        "\nmkdir: Creates a new directory."+
        "\nrmdir: Removes given directory."+
        "\nmv: Moves current file from the current directory to another one."+
        "\nrm: Removes given file."+
        "\nargs: List all the given command's arguments."+
        "\ndate: Shows current date/time."+
        "\nhelp: List all user commands."+
        "\nclear: Clear the console."+
        "\npwd: Display current user directory."+
        "\nmore: Display the content of a given file with the ability of scrolling down."+
        "\nexit: Close the console.";
    }

    public static String args(String input) {
        try {//TODO Remove try-catch and make a default case for invalid input\
            String commandOutput = "";
            switch (input) {
                case "cd":
                    commandOutput = "Arguments: DestinationPath";
                    break;
                case "ls":
                    commandOutput = "Arguments: DestinationPath || Arguments: None";
                    break;
                case "cp":
                case "mv":
                    commandOutput = "Arguments: SourcePath DestinationPath";
                    break;
                case "cat":
                    commandOutput = "Arguments: firstFileName secondFileName || Arguments: fileName";
                    break;
                case "more":
                case "rm":
                    commandOutput = "Arguments: fileName";
                    break;
                case "mkdir":
                case "rmdir":
                    commandOutput = "Arguments: directoryName";
                    break;
                case "args":
                    commandOutput = "Arguments: commandName";
                    break;
                case "pwd":
                case "date":
                case "clear":
                case "help":
                case "exit":
                    commandOutput = "Arguments: None";
                    break;
            }
            return commandOutput;
        } catch (Exception e) {
            return "Invalid input";
        }
    }

    public static String pwd(){
        return Main.currentDirectory;
    }

    public static String cat(ArrayList<String> Paths) {
        File filePath = null;
        String fileLines = "";
        try {
            File files;
            Scanner myReader = null;
            for (int i = 0; i < Paths.size(); i++) {
                filePath = new File(Main.currentDirectory, Paths.get(i));
                if (filePath.isAbsolute()) {
                    System.out.println("File Name: " + filePath.getName());
                    files = new File(Main.currentDirectory, Paths.get(i));
                    myReader = new Scanner(files);
                    while (myReader.hasNextLine()) fileLines += myReader.nextLine() + "\n";
                } else {
                    System.out.println("File Name: " + Paths.get(i));
                    files = new File(Main.currentDirectory, Paths.get(i));
                    myReader = new Scanner(files);
                    while (myReader.hasNextLine()) fileLines += myReader.nextLine() + "\n";
                }
            }
            myReader.close();
            return fileLines;
        } catch (Exception e) {
            return "File " + filePath.getName() + " Not Found!";
        }
    }

    public static void mv(String sourcePath, String destinationPath) throws IOException {
        File source = new File(Main.currentDirectory + "/" +sourcePath);
        File destination = new File(destinationPath);

        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel destinationChannel = null;
        if(destination.isDirectory())
            destinationChannel = new FileOutputStream(destination + "\\" + source.getName()).getChannel();
        else destinationChannel = new FileOutputStream(destination).getChannel();
        try {
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            source.delete();
            destinationChannel.close();
        }
    }

    public static String more(String input) {
        Scanner myFileReader = null;
        Scanner myReader = new Scanner(System.in);
        File filePath;
        String choice;
        String fileLines = "";
        int linesCounter = 0;
        try {
            filePath = new File(Main.currentDirectory, input);
            myFileReader = new Scanner(filePath);
            while (myFileReader.hasNextLine()){
                String tempLine = myFileReader.nextLine();
                fileLines += tempLine + "\n";
                System.out.println(tempLine);
                linesCounter++;

                if(linesCounter == 5){
                    linesCounter = 0;
                    System.out.println("\nWould you like to continue? [e To exit || c to Continue]");
                    choice = myReader.nextLine();

                    if(choice.equalsIgnoreCase("c")) continue;

                    else if (choice.equalsIgnoreCase("e")){
                        myFileReader.close();
                        myReader.close();
                        break;
                    }
                }
            }
            return fileLines;
            } catch (FileNotFoundException ex) {
                myReader.close();
                return "File Not Found";
            } catch (NoSuchElementException e) {
                myReader.close();
                return "No Lines Found";
            }
    }

    private static long fileCounter(File currentDirectory) {
        File[] files = currentDirectory.listFiles();
        long length = 0;
        int count = files.length;
        for(int i = 0; i < count; i++){
            length += files[i].isFile() == true ? files[i].length() : fileCounter(files[i]);
        }
        return length;
    }

    public static String rmdir(String Path) {
        String commandOutput = "";
        File dirPath = new File(Main.currentDirectory + "/" + Path);
        if(dirPath.isDirectory() && dirPath.exists()){
            if(fileCounter(dirPath) == 0) {
                dirPath.delete();
                commandOutput = "Directory " + dirPath.getName()+ " is deleted Successfully";
            }
            else commandOutput = "Directory " + dirPath.getName()+ " is not empty";
        }
        else commandOutput = "Directory " + dirPath.getName()+ " is not Found";
        return commandOutput;
    }

    public static void clear(){
        for (int i = 0; i < 150; i++) System.out.println();
    }

    public static void cp(String sourcePath, String destinationPath) {
        File source = new File(Main.currentDirectory + "/"+ sourcePath);
        File destination = new File(destinationPath);
        try {
            if(destination.isDirectory()){
                Scanner sc = new Scanner(source);
                FileWriter copiedWriter= new FileWriter(destination + "\\" + source.getName());
                while (sc.hasNextLine()) {
                    String data= sc.nextLine();
                    copiedWriter.write(data);
                    copiedWriter.write("\n");
                }
                copiedWriter.close();
            }
            else{
                Scanner sc = new Scanner(source);
                FileWriter copiedWriter= new FileWriter(destination);
                while (sc.hasNextLine()) {
                    String data= sc.nextLine();
                    copiedWriter.write(data);
                    copiedWriter.write("\n");
                }
                copiedWriter.close();
            }
        } catch (IOException e) {
            System.out.println("File " + source.getName() + " is not found");
        }
    }

    public static void rm(String Path) {
        File file= new File(Main.currentDirectory + "/" + Path);

        if(!file.exists()) {
            System.out.println("No such file found");
        }
        else {
            if(file.isFile()) {
                file.delete();
                System.out.println("File '"+ file.getName() +"' deleted successfully");
            }
            else if(file.isDirectory()) {
                file.delete();
                System.out.println("File '"+ file.getName() +"' deleted successfully");
            }
            else System.out.println("The following '"+ file.getName() +"' is not a file");
        }
    }

    public static void mkdir(String Path) {
        File file= new File(Main.currentDirectory, Path);
        boolean bool = file.mkdir();
        if(bool){
            System.out.println("Directory '"+ file.getName() +"' created successfully");
        }else{
            System.out.println("Either it is already created or The path is invalid");
        }
    }

    public static String date() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void cd(String input) {
        try{
            Path currentDirectory = Paths.get(Main.currentDirectory);
            Path destinationPath = Paths.get(input);
            String finalPath = currentDirectory.resolve(destinationPath).toFile().getCanonicalPath();

            if (Files.exists(Paths.get(finalPath)))
            {
                Main.currentDirectory = finalPath;
            }
            else System.out.println("Cannot find the specified file");
        } catch (IOException e){
            System.out.println("Cannot find the path");
        }
    }

    public static void writeToFile(String commandsOutput, String fileName) throws IOException {
        File filePath= new File(Main.currentDirectory, fileName);
        if(filePath.exists()){
            FileWriter fileWriter= new FileWriter(filePath);
            fileWriter.write("\n" + commandsOutput);
            fileWriter.close();
        }
        else{
            FileWriter fileWriter= new FileWriter(filePath);
            fileWriter.write("\n" + commandsOutput);
            fileWriter.close();
        }
    }

    public static void appendToFile(String commandsOutput, String fileName) throws IOException {
        File filePath= new File(Main.currentDirectory, fileName);
        if(filePath.exists()){
            FileWriter fileWriter= new FileWriter(filePath, true);
            fileWriter.append("\n" + commandsOutput);
            fileWriter.close();
        }
        else{
            FileWriter fileWriter= new FileWriter(filePath, true);
            fileWriter.append("\n" + commandsOutput);
            fileWriter.close();
        }
    }

    public static String ls() throws IOException {
        String fileLines = "";
        try{
            File folder = new File(Main.currentDirectory);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile())
                    fileLines += "File: " + listOfFiles[i].getName() + "\n";
                else if (listOfFiles[i].isDirectory())
                    fileLines += "Directory: " + listOfFiles[i].getName() + "\n";
            }
        } catch (NullPointerException e){
            System.out.println("Path not found");
        }
        return fileLines;
    }

    public static String ls(String listPathFiles){
        String fileLines = "";
        try{
            File folder = new File(listPathFiles);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile())
                    fileLines += "File: " + listOfFiles[i].getName() + "\n";
                else if (listOfFiles[i].isDirectory())
                    fileLines += "Directory: " + listOfFiles[i].getName() + "\n";
            }
        } catch (NullPointerException e){
            System.out.println("Path not found");
        }
        return fileLines;
    }
}
