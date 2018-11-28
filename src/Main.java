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

public class Main {

    public static void main(String[] args) {
    	MemoryManagementUnit memoryManagementUnit = new MemoryManagementUnit();
        CoreProcessingUnit firstCore = new CoreProcessingUnit(memoryManagementUnit, 25);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of initial processes you require.");
        int numberOfProcesses = scanner.nextInt();

        // Generate Processes

        for(int i = 1; i <= numberOfProcesses; i++) {
            try {
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

                    int typeOfInstruction = random.nextInt(3);
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
                            }
                        }
                    }

                    firstCore.addProcess(process);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Set start time to 0
        int currentCPUtime = 0;

        // Start CPU Loop
        while(true){
            boolean done = firstCore.run();
            currentCPUtime++;

            if(done) {
	            System.out.printf("Round Robin with Priorities Scheduling Algorithm Took : %dms of simulated CPU time.", currentCPUtime);
	            break;
            }
        }

    }
}
