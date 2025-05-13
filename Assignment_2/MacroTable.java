// package Assignment_2.Java Code ass2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MacroTable {

    // Macro Name Table (MNT)
    private static Map<String, MacroInfo> MNT = new HashMap<>();

    // Macro Definition Table (MDT)
    private static List<MacroDefinition> MDT = new ArrayList<>();

    // Intermediate Code
    private static List<String> intermediateCode = new ArrayList<>();

    // MacroInfo class to hold information about each macro
    private static class MacroInfo {
        String name;
        int numParams;
        int startLine;
        int endLine;

        public MacroInfo(String name, int numParams) {
            this.name = name;
            this.numParams = numParams;
        }
    }

    // MacroDefinition class to hold macro definition and line number
    private static class MacroDefinition {
        String definition;

        public MacroDefinition(String definition) {
            this.definition = definition;
        }
    }

    // First Pass: Process macro definitions and populate MNT and MDT
    private static void firstPass(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        boolean inMacro = false;
        String currentMacro = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("MACRO")) {
                inMacro = true;
                String[] parts = line.split("\\s+");
                currentMacro = parts[1];
                int numParams = parts.length - 2;
                MNT.put(currentMacro, new MacroInfo(currentMacro, numParams));
                MNT.get(currentMacro).startLine = MDT.size() + 1;
            } else if (inMacro && !line.startsWith("MEND")) {
                if (currentMacro != null) {
                    MDT.add(new MacroDefinition(line));
                }
            } else if (inMacro && line.startsWith("MEND")) {
                inMacro = false;
                if (currentMacro != null) {
                    MNT.get(currentMacro).endLine = MDT.size(); // Include MEND line
                    currentMacro = null;
                }
            } else {
                // Add non-macro lines to intermediate code
                intermediateCode.add(line);
            }
        }
        reader.close();

        // Print MNT
        System.out.println("Macro Name Table (MNT):");
        System.out.printf("%-10s | %-12s | %-10s | %-10s\n", "Macro", "Num Params", "Start Line", "End Line");
        for (Map.Entry<String, MacroInfo> entry : MNT.entrySet()) {
            MacroInfo info = entry.getValue();
            System.out.printf("%-10s | %-12d | %-10d | %-10d\n", entry.getKey(), info.numParams, info.startLine, info.endLine);
        }

        // Print MDT
        System.out.println("\nMacro Definition Table (MDT):");
        System.out.printf("%-5s | %-20s\n", "Index", "Macro Definition");
        for (int i = 0; i < MDT.size(); i++) {
            MacroDefinition macroDef = MDT.get(i);
            System.out.printf("%-5d | %-20s\n", i + 1, macroDef.definition);
        }

        // Print Intermediate Code
        System.out.println("\nIntermediate Code:");
        for (String codeLine : intermediateCode) {
            System.out.println(codeLine);
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\Lenovo\\Desktop\\TY 2024-25\\Sem 6\\LPCC\\Lab exam\\Assignment codes\\Assignment_2\\Java Code ass2\\input.txt"; // Specify the file name here
        try {
            firstPass(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
