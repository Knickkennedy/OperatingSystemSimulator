import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class OperatingSystem {

    public static int nextPID = 0;

    public static void main(String[] args) {
    	MemoryManagementUnit memoryManagementUnit = new MemoryManagementUnit();
        CoreProcessingUnit firstCore = new CoreProcessingUnit(memoryManagementUnit, 25);
        CoreProcessingUnit secondCore = new CoreProcessingUnit(memoryManagementUnit, 35);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of initial processes you require.");
        int numberOfProcesses = scanner.nextInt();
        // Generate Processes

        for(int i = 1; i <= numberOfProcesses; i++) {
            try {
                nextPID++;
                String fileName = "Processes/process" + i + ".xml";
                PrintWriter printWriter = new PrintWriter(fileName);
                printWriter.printf("<process id=\"%d\">\n", i);

                Random random = new Random();

                int priority = random.nextInt(999) + 1;
                int exponent = random.nextInt(10) + 1;
                int size = (int)Math.pow(2, exponent);

                printWriter.printf("\t<size>%d</size>\n", size);
                printWriter.printf("\t<priority>%d</priority>\n", priority);

                int numberOfInstructions = random.nextInt(15) + 5;
                int criticalSection = random.nextInt(numberOfInstructions);

                for(int j = 0; j < numberOfInstructions; j++) {

                    int typeOfInstruction = random.nextInt(5);
                    int lengthOfInstruction = random.nextInt(25) + 25;

                    switch (typeOfInstruction) {
                        case 0:
                            if(j == criticalSection) {
                                printWriter.printf("\t<criticalSection><calculate>%d</calculate></criticalSection>\n", lengthOfInstruction);
                            }
                            else{
                                printWriter.printf("\t<calculate>%d</calculate>\n", lengthOfInstruction);
                            }
                            break;
                        case 1:
                            if(j == criticalSection){
                                printWriter.printf("\t<criticalSection><io>%d</io></criticalSection>\n", lengthOfInstruction);
                            }
                            else {
                                printWriter.printf("\t<io>%d</io>\n", lengthOfInstruction);
                            }
                            break;
                        case 2:
                            if(j == criticalSection){
                                printWriter.println("\t<criticalSection><yield>0</yield></criticalSection>");
                            }
                            else {
                                printWriter.println("\t<yield>0</yield>");
                            }
                            break;
                        case 3:
                            if(j == criticalSection){
                                printWriter.println("\t<criticalSection><fork>0</fork></criticalSection>");
                            }
                            else{
                                printWriter.println("\t<fork>0</fork>");
                            }
                            break;
                        case 4:
                            int roll = random.nextInt(100);
                            if(roll < 10) {
                                if (j == criticalSection) {
                                    printWriter.println("\t<criticalSection><requestResources>3</requestResources></criticalSection>");
                                } else {
                                    printWriter.println("\t<requestResources>3</requestResources>");
                                }
                            }
                            break;
                    }
                }
                printWriter.printf("</process>");
                printWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            File[] files = new File("Processes/").listFiles();
            int fileNumber = 1;
            if (files != null) {
                for (File file : files) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = dbFactory.newDocumentBuilder();
                    Document document = builder.parse(file);

                    document.getDocumentElement().normalize();
                    NodeList nodeList = document.getElementsByTagName("process");

                    Node fullProcess = nodeList.item(0);

                    Element ID = (Element)fullProcess;

                    Process process = new Process(Integer.parseInt(ID.getAttribute("id")));

                    NodeList children = fullProcess.getChildNodes();


                    for (int i = 0; i < children.getLength(); i++) {
                        Node node = children.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            String tagName = node.getNodeName();

                            switch (tagName) {
                                case "priority":
                                    process.setPriority(Integer.parseInt(node.getTextContent()));
                                    break;
                                case "size":
                                    process.setSize(Integer.parseInt(node.getTextContent()));
                                case "criticalSection":
                                    switch (node.getChildNodes().item(0).getNodeName()){
                                        case "calculate":
                                            process.addInstruction(new Instruction(InstructionType.CALCULATE, Integer.parseInt(node.getTextContent()), true));
                                            break;
                                        case "io":
                                            process.addInstruction(new Instruction(InstructionType.IO, Integer.parseInt(node.getTextContent()), true));
                                            break;
                                        case "yield":
                                            process.addInstruction(new Instruction(InstructionType.YIELD, true));
                                            break;
                                        case "fork":
                                            process.fork();
                                            break;
                                        case "requestResources":
                                            process.addInstruction(new Instruction(InstructionType.REQUEST, true));
                                            break;
                                    }

                                    break;
                                case "calculate":
                                    process.addInstruction(new Instruction(InstructionType.CALCULATE, Integer.parseInt(node.getTextContent())));
                                    break;
                                case "io":
                                    process.addInstruction(new Instruction(InstructionType.IO, Integer.parseInt(node.getTextContent())));
                                    break;
                                case "yield":
                                    process.addInstruction(new Instruction(InstructionType.YIELD));
                                    break;
                                case "fork":
                                    process.fork();
                                    break;

                                case "requestResources":
                                    process.addInstruction(new Instruction(InstructionType.REQUEST));
                                    break;
                            }
                        }
                    }

                    if(fileNumber < numberOfProcesses / 2)
                        firstCore.addProcess(process);
                    else
                        secondCore.addProcess(process);
                    fileNumber++;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Set start time to 0
        int currentCpuTime = 0;

        int counter = 0;
        boolean done = false;
        // Start CPU Loop
        while(!done){

            System.out.print("0: Select X number of cycles to run\n" +
                    "1: Add new process to both cores\n" +
                    "2: Print current CPU Clock\n" +
                    "3: Run one cycle\n" +
                    "4: Run the rest of the cycles\n");

            Scanner scan = new Scanner(System.in);
            int check = scan.nextInt();

            switch (check) {
                case 0:{
                    System.out.println("Enter the number of cycles to run.");
                    int numberOfCycles = scan.nextInt();
                    System.out.printf("Running %d cycles.\n", numberOfCycles);
                    for(int i = 0; i < numberOfCycles; i++){
                        if (!firstCore.checkIfDone()) {
                            firstCore.run();
                        }

                        if (!secondCore.checkIfDone()) {
                            secondCore.run();
                        }

                        boolean firstDone = firstCore.checkIfDone();
                        boolean secondDone = secondCore.checkIfDone();
                        currentCpuTime++;

                        if (firstDone && secondDone) {
                            System.out.printf("Round Robin with Priorities Scheduling Algorithm Took : %dms of simulated CPU time.", currentCpuTime);
                            done = true;
                        }
                    }
                    break;
                }
                case 1:{
                    Random random = new Random();

                    Process process = new Process(OperatingSystem.nextPID++);

                    int priority = random.nextInt(999) + 1;
                    int exponent = random.nextInt(10) + 1;
                    int size = (int)Math.pow(2, exponent);

                    int numberOfInstructions = random.nextInt(15) + 5;
                    int criticalSection = random.nextInt(numberOfInstructions);

                    process.setPriority(priority);
                    process.setSize(size);

                    for(int j = 0; j < numberOfInstructions; j++) {

                        int typeOfInstruction = random.nextInt(4);
                        int lengthOfInstruction = random.nextInt(25) + 25;
                        switch (typeOfInstruction){
                            case 0:
                                if(j == criticalSection){
                                    process.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction, true));
                                }
                                else{
                                    process.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction));
                                }
                                break;
                            case 1:
                                if(j == criticalSection){
                                    process.addInstruction(new Instruction(InstructionType.IO, lengthOfInstruction, true));
                                }
                                else{
                                    process.addInstruction(new Instruction(InstructionType.IO, lengthOfInstruction));
                                }
                                break;
                            case 2:
                                if(j == criticalSection){
                                    process.addInstruction(new Instruction(InstructionType.YIELD, lengthOfInstruction, true));
                                }
                                else{
                                    process.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction));
                                }
                                break;
                            case 3:
                                int roll = random.nextInt(100);
                                if(roll < 3) // our forks have a 3% chance of forking
                                    process.fork();
                                break;
                        }

                        firstCore.addProcess(process);

                        Process process1 = new Process(OperatingSystem.nextPID++);

                        int priority1 = random.nextInt(999) + 1;
                        int exponent1 = random.nextInt(10) + 1;
                        int size1 = (int)Math.pow(2, exponent1);

                        int numberOfInstructions1 = random.nextInt(15) + 5;
                        int criticalSection1 = random.nextInt(numberOfInstructions1);

                        process1.setPriority(priority1);
                        process1.setSize(size1);

                        for(int k = 0; k < numberOfInstructions; k++) {

                            int typeOfInstruction1 = random.nextInt(4);
                            int lengthOfInstruction1 = random.nextInt(25) + 25;
                            switch (typeOfInstruction1){
                                case 0:
                                    if(k == criticalSection1){
                                        process1.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction1, true));
                                    }
                                    else{
                                        process1.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction1));
                                    }
                                    break;
                                case 1:
                                    if(k == criticalSection1){
                                        process.addInstruction(new Instruction(InstructionType.IO, lengthOfInstruction1, true));
                                    }
                                    else{
                                        process1.addInstruction(new Instruction(InstructionType.IO, lengthOfInstruction1));
                                    }
                                    break;
                                case 2:
                                    if(k == criticalSection1){
                                        process1.addInstruction(new Instruction(InstructionType.YIELD, lengthOfInstruction1, true));
                                    }
                                    else{
                                        process1.addInstruction(new Instruction(InstructionType.CALCULATE, lengthOfInstruction1));
                                    }
                                    break;
                                case 3:
                                    int roll = random.nextInt(100);
                                    if(roll < 3) // our forks have a 3% chance of forking
                                        process1.fork();
                                    break;
                            }
                        }

                        secondCore.addProcess(process1);
                    }
                    break;
                }
                case 2:{
                    System.out.printf("Current CPU Time: %d\n", currentCpuTime);
                    break;
                }
                case 3: {

                    System.out.println("Running one Operating System Cycle.");

                    if (!firstCore.checkIfDone()) {
                        firstCore.run();
                    }

                    if (!secondCore.checkIfDone()) {
                        secondCore.run();
                    }

                    boolean firstDone = firstCore.checkIfDone();
                    boolean secondDone = secondCore.checkIfDone();
                    currentCpuTime++;

                    if (firstDone && secondDone) {
                        System.out.printf("Round Robin with Priorities Scheduling Algorithm Took : %dms of simulated CPU time.", currentCpuTime);
                        done = true;
                    }
                    break;
                }
                case 4:{
                    while(!done) {
                        if (!firstCore.checkIfDone()) {
                            firstCore.run();
                        }

                        if (!secondCore.checkIfDone()) {
                            secondCore.run();
                        }

                        boolean firstDone = firstCore.checkIfDone();
                        boolean secondDone = secondCore.checkIfDone();
                        currentCpuTime++;

                        if (firstDone && secondDone) {
                            System.out.printf("Round Robin with Priorities Scheduling Algorithm Took : %dms of simulated CPU time.", currentCpuTime);
                            done = true;
                        }
                    }
                    break;
                }
            }
        }

    }
}
