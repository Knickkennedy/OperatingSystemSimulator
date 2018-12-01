import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Gui extends JFrame implements ActionListener {
    private JPanel panel;
    private JScrollPane textPanel;
    private JScrollPane processesPanel;

    private JTextArea textArea;
    private JTextField firstTextField;

    private JTextArea processesTextArea;

    private JButton firstButton;
    private JButton secondButton;
    private JButton thirdButton;
    private JButton fourthButton;
    private JButton fifthButton;

    private CoreProcessingUnit firstCore;
    private CoreProcessingUnit secondCore;
    private MemoryManagementUnit memoryManagementUnit;
    private ArrayList<Process> processes;

    private boolean done;

    private int currentCpuTime;

    public Gui(String firstButtonText, String secondButtonText, String thirdButtonText, String fourthButtonText, String fifthButtonText, CoreProcessingUnit firstCore, CoreProcessingUnit secondCore, MemoryManagementUnit memoryManagementUnit, ArrayList<Process> processes){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dimension.width - 1000) / 2;
        int y = (dimension.height - 800) / 2;
        this.setLocation(x, y);
        this.setTitle("Operating System Simulator");
        this.panel = new JPanel();
        this.textArea = new JTextArea(20, 1);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        processesTextArea = new JTextArea(20, 1);
        processesTextArea.setEditable(false);
        processesTextArea.setLineWrap(true);
        processesTextArea.setWrapStyleWord(true);
        this.textPanel = new JScrollPane(textArea);
        processesPanel = new JScrollPane(processesTextArea);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 800);
        firstButton = new JButton(firstButtonText);
        firstButton.addActionListener(this);
        firstButton.setActionCommand("first");
        secondButton = new JButton(secondButtonText);
        secondButton.addActionListener(this);
        secondButton.setActionCommand("second");
        thirdButton = new JButton(thirdButtonText);
        thirdButton.addActionListener(this);
        thirdButton.setActionCommand("third");
        fourthButton = new JButton(fourthButtonText);
        fourthButton.addActionListener(this);
        fourthButton.setActionCommand("fourth");
        fifthButton = new JButton(fifthButtonText);
        fifthButton.addActionListener(this);
        fifthButton.setActionCommand("fifth");
        firstTextField = new JTextField("");
        firstTextField.setColumns(3);
        panel.add(firstTextField);
        panel.add(firstButton);
        panel.add(secondButton);
        panel.add(thirdButton);
        panel.add(fourthButton);
        panel.add(fifthButton);
        this.getContentPane().add(BorderLayout.SOUTH, panel);
        this.getContentPane().add(BorderLayout.NORTH, textPanel);
        this.getContentPane().add(BorderLayout.CENTER, processesPanel);

        this.firstCore = firstCore;
        this.secondCore = secondCore;

        this.memoryManagementUnit = memoryManagementUnit;
        this.processes = processes;

        this.done = false;

        this.currentCpuTime = 0;
    }

    public void makeGuiVisible(){
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        boolean firstDone;
        boolean secondDone;

        switch (action){
            case "first":

                String textField = firstTextField.getText();
                int numberOfCycles;
                if(!textField.isEmpty())
                    numberOfCycles = Integer.parseInt(textField);
                else{
                    textArea.append("You failed to enter an amount of cycles. Running the default 25 cycles.\n");
                    numberOfCycles = 25;
                }

                textArea.append(String.format("Running %d cycles.\n", numberOfCycles));
                for(int i = 0; i < numberOfCycles; i++){
                    if (!firstCore.checkIfDone()) {
                        firstCore.run();
                    }

                    if (!secondCore.checkIfDone()) {
                        secondCore.run();
                    }

                    firstDone = firstCore.checkIfDone();
                    secondDone = secondCore.checkIfDone();
                    currentCpuTime++;

                    if (firstDone && secondDone) {
                        done = true;
                    }
                }
                break;
            case "second":
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
                                process.fork(processes);
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
                                    process1.fork(processes);
                                break;
                        }
                    }

                    secondCore.addProcess(process1);
                }
                textArea.append("Successfully added a process to each core.\n");
                currentCpuTime++;
                break;
            case "third":
                textArea.append(String.format("Current Clock Time: %d\n", currentCpuTime));
                break;
            case "fourth":

                textArea.append("Running one Operating System Cycle.\n");

                if (!firstCore.checkIfDone()) {
                    firstCore.run();
                }

                if (!secondCore.checkIfDone()) {
                    secondCore.run();
                }

                firstDone = firstCore.checkIfDone();
                secondDone = secondCore.checkIfDone();
                currentCpuTime++;

                if (firstDone && secondDone) {
                    textArea.append(String.format("Round Robin with Priorities Scheduling Algorithm Took : %dms of simulated CPU time.\n", currentCpuTime));
                    done = true;
                }
                break;
            case "fifth":

                while(!done) {
                    if (!firstCore.checkIfDone()) {
                        firstCore.run();
                    }

                    if (!secondCore.checkIfDone()) {
                        secondCore.run();
                    }

                    firstDone = firstCore.checkIfDone();
                    secondDone = secondCore.checkIfDone();
                    currentCpuTime++;

                    if (firstDone && secondDone) {
                        textArea.append(String.format("Round Robin with Priorities Scheduling Algorithm Took : %dms of simulated CPU time.", currentCpuTime));
                        done = true;
                    }
                }
                break;
        }

        populateProcesses();
    }

    public void populateProcesses(){

        processesTextArea.setText("");

        for(Process process : processes){
            processesTextArea.append(String.format("%s %s\n", process, process.getProcessControlBlock()));
        }
    }
}
