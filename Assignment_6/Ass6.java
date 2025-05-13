import java.util.*;
class Ass6 {
    
    // Class to represent a three-address code instruction
    static class Instruction {
        String op;
        String arg1;
        String arg2;
        String result;
        
        public Instruction(String op, String arg1, String arg2, String result) {
            this.op = op;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.result = result;
        }
        
        @Override
        public String toString() {
            if (op.equals("=")) {
                return result + " = " + arg1;
            }
            return result + " = " + arg1 + " " + op + " " + arg2;
        }
    }
    
    public static void main(String[] args) {
        // Hard-coded three-address code input
        List<Instruction> code = new ArrayList<>();
        code.add(new Instruction("+", "a", "5", "t1"));      // t1 = a + 5
        code.add(new Instruction("*", "t1", "2", "t2"));     // t2 = t1 * 2
        code.add(new Instruction("=", "3", "", "a"));        // a = 3
        code.add(new Instruction("+", "a", "5", "t3"));      // t3 = a + 5
        code.add(new Instruction("*", "t3", "2", "t4"));     // t4 = t3 * 2
        code.add(new Instruction("*", "t1", "1", "t5"));     // t5 = t1 * 1
        code.add(new Instruction("+", "t5", "0", "t6"));     // t6 = t5 + 0
        code.add(new Instruction("=", "t6", "", "b"));      // b = t6
        code.add(new Instruction("=", "10", "", "c"));       // c = 10
        code.add(new Instruction("*", "c", "2", "t7"));     // t7 = c * 2
        code.add(new Instruction("=", "t7", "", "d"));      // d = t7
        code.add(new Instruction("=", "5", "", "e"));        // e = 5
        code.add(new Instruction("=", "e", "", "f"));        // f = e
        
        System.out.println("Original Three Address Code:");
        printCode(code);
        
        // Apply optimizations
        List<Instruction> optimizedCode = optimize(code);
        
        System.out.println("\nOptimized Three Address Code:");
        printCode(optimizedCode);
    }
    
    // Print the three-address code
    public static void printCode(List<Instruction> code) {
        for (Instruction instr : code) {
            System.out.println(instr);
        }
    }
    
    // Apply all optimizations
    public static List<Instruction> optimize(List<Instruction> code) {
        // Create a copy of the original code
        List<Instruction> optimized = new ArrayList<>(code);
        
        // Apply optimizations in multiple passes
        boolean changed;
        do {
            changed = false;
            
            // Constant propagation
            Map<String, String> constValues = new HashMap<>();
            for (int i = 0; i < optimized.size(); i++) {
                Instruction instr = optimized.get(i);
                
                // Check for constant assignments
                if (instr.op.equals("=") && isConstant(instr.arg1)) {
                    constValues.put(instr.result, instr.arg1);
                    // Check if this is a redundant assignment (x = y where y is constant)
                    if (constValues.containsKey(instr.arg1)) {
                        constValues.put(instr.result, constValues.get(instr.arg1));
                        optimized.set(i, new Instruction("=", constValues.get(instr.arg1), "", instr.result));
                        changed = true;
                    }
                }
                
                // Replace variables with their constant values if available
                if (constValues.containsKey(instr.arg1)) {
                    instr.arg1 = constValues.get(instr.arg1);
                    changed = true;
                }
                if (constValues.containsKey(instr.arg2)) {
                    instr.arg2 = constValues.get(instr.arg2);
                    changed = true;
                }
            }
            
            // Constant folding
            for (int i = 0; i < optimized.size(); i++) {
                Instruction instr = optimized.get(i);
                if (isConstant(instr.arg1) && isConstant(instr.arg2) && !instr.op.equals("=")) {
                    int val1 = Integer.parseInt(instr.arg1);
                    int val2 = Integer.parseInt(instr.arg2);
                    int result = 0;
                    
                    switch (instr.op) {
                        case "+": result = val1 + val2; break;
                        case "-": result = val1 - val2; break;
                        case "*": result = val1 * val2; break;
                        case "/": result = val1 / val2; break;
                    }
                    
                    optimized.set(i, new Instruction("=", Integer.toString(result), "", instr.result));
                    changed = true;
                }
                
                // Remove operations with identity elements
                if (instr.op.equals("*") && instr.arg2.equals("1")) {
                    optimized.set(i, new Instruction("=", instr.arg1, "", instr.result));
                    changed = true;
                }
                if (instr.op.equals("+") && instr.arg2.equals("0")) {
                    optimized.set(i, new Instruction("=", instr.arg1, "", instr.result));
                    changed = true;
                }
            }
            
            // Common subexpression elimination
            Map<String, String> exprMap = new HashMap<>();
            for (int i = 0; i < optimized.size(); i++) {
                Instruction instr = optimized.get(i);
                if (!instr.op.equals("=")) {
                    String exprKey = instr.op + "," + instr.arg1 + "," + instr.arg2;
                    if (exprMap.containsKey(exprKey)) {
                        // Replace with existing temporary variable
                        optimized.set(i, new Instruction("=", exprMap.get(exprKey), "", instr.result));
                        changed = true;
                    } else {
                        exprMap.put(exprKey, instr.result);
                    }
                }
            }
            
            // Dead code elimination
            Set<String> usedVars = new HashSet<>();
            // First pass: find all variables that are used
            for (Instruction instr : optimized) {
                if (!instr.op.equals("=")) {
                    if (!isConstant(instr.arg1)) usedVars.add(instr.arg1);
                    if (!isConstant(instr.arg2)) usedVars.add(instr.arg2);
                }
            }
            
            // Second pass: remove assignments to variables that are never used
            Iterator<Instruction> it = optimized.iterator();
            while (it.hasNext()) {
                Instruction instr = it.next();
                if (instr.op.equals("=") && !usedVars.contains(instr.result)) {
                    // Check if this is a user variable (not a temporary)
                    if (!instr.result.startsWith("t")) {
                        continue; // keep user variable assignments
                    }
                    it.remove();
                    changed = true;
                }
            }
            
        } while (changed); // Repeat until no more changes
        
        return optimized;
    }
    
    // Check if a string represents a constant value
    private static boolean isConstant(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
