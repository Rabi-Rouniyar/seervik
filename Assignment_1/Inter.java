import java.util.*;

class opTable {
    String type;
    String code;

    public opTable(String type, String code) {
        this.type = type;
        this.code = code;
    }
}

class Symbol {
    String name;
    int address;

    public Symbol(String name, int address) {
        this.name = name;
        this.address = address;
    }
}

class Literal {
    String value;
    int address;

    public Literal(String value, int address) {
        this.value = value;
        this.address = address;
    }
}

public class Inter {
    public static void main(String[] args) {
        String[] assemblyCode = {
            "START 200",
            "MOVER AREG,=‘5’",
            "MOVEM AREG,X",
            "L1 MOVER BREG,=‘2’",
            "ORIGIN L1+3",
            "LTORG",
            "NEXT ADD AREG,=‘1’",
            "SUB BREG,=‘2’",
            "BC LT,BACK",
            "LTORG",
            "BACK EQU L1",
            "ORIGIN NEXT+9",
            "MULT CREG, X",
            "STOP",
            "X DS 1",
            "END"
        };

        int lc = 0;
        int size = 1;
        // int iST = 0;
        int iPT = 0;
        int[] PT = new int[100];
        // PT[iPT] = 0;
        // int iLT = 0;
        // int LTindex = -1;
        int LiteralCount = 0;
        
        List<Integer> LC = new ArrayList<>(); 
        
        Map<String, opTable> opcode = new HashMap<>();
        initializeOpcodeTable(opcode);

        Map<String,Symbol> symbolTable = new HashMap<>();
        List<Literal> literalTable = new ArrayList<>();

        for (String line : assemblyCode) {
            String[] parts = line.split("\\s+");
            String S1 = parts[0];
        
            if (S1.equals("END"))
            {
                if(LiteralCount!=0)
                {
                    for(int i=LiteralCount;i>0;i--)
                    {
                        literalTable.get(literalTable.size()-i).address = lc;
                        // System.out.println(literalTable.get(literalTable.size()-i).value+"-"+lc);
                        LC.add(lc);
                        lc++;
                    }
                }
                PT[iPT] = literalTable.size()-LiteralCount;
                iPT++;
                break;
            }

            String S2 = parts.length > 1 ? parts[1] : "";
            String S3 = parts.length > 2 ? parts[2] : "";

            if (S1.equals("START")) {
                lc = (S2!="") ? Integer.parseInt(S2) : 1;
                LC.add(lc);
                // System.out.println(S1+"-"+lc);
                continue;
            }

            if (S1.equals("ORIGIN")) {
                String[] originParts = S2.split("\\+");
                if(symbolTable.containsKey(originParts[0]))
                {
                    if(originParts.length==2)
                        lc = symbolTable.get(originParts[0]).address + Integer.parseInt(originParts[1]);
                    else
                        lc = symbolTable.get(originParts[0]).address;
                }
                // System.out.println(originParts[0]+" Origin-"+lc);
                LC.add(lc);
                continue;
            }
            
            if(S2.equals("EQU"))
            {
                symbolTable.put(S1,new Symbol(S1, symbolTable.get(S3).address));
                // System.out.println(S1+"-"+lc+"/"+symbolTable.get(S3).address);
                LC.add(lc);
                lc++;
                continue;
            }

            if (parts.length>=3 && !opcode.containsKey(S1)) {
                symbolTable.put(S1,new Symbol(S1, lc));
                // System.out.println(S1+"-"+lc);
                LC.add(lc);
            }
            
            if (S1.equals("LTORG")) {
                for(int i=LiteralCount;i>0;i--)
                {
                    literalTable.get(literalTable.size()-i).address = lc;
                    // System.out.println(literalTable.get(literalTable.size()-i).value+"-"+lc);
                    LC.add(lc);
                    lc++;
                }
                // iPT++;
                PT[iPT] = literalTable.size()-LiteralCount;
                iPT++;
                LiteralCount = 0;
                continue;
            }
            
            String last = parts[parts.length-1];
            String[] tokens= last.split(",");
            String lasttoken = tokens[tokens.length-1];
            
            if (lasttoken.contains("=")) {
                literalTable.add(new Literal(lasttoken, lc));
                LC.add(lc);
                // System.out.println(lasttoken+"-"+lc);
                LiteralCount++;
            }
            else if(!opcode.containsKey(lasttoken))
            {
                symbolTable.put(lasttoken,new Symbol(lasttoken, lc));
                // System.out.println(lasttoken+"-"+lc);
                LC.add(lc);
            }
            
            lc += size;
        }

        System.out.println("LC Count: ");
        for(int i=0;i<LC.size();i++)
        {
            System.out.println(LC.get(i));
        }
        System.out.println(" ");
        
        System.out.println("\nSymbol Table:");
        System.out.println("Symbol\tAddress");
        for (Map.Entry<String,Symbol> entry : symbolTable.entrySet()) 
        {
            Symbol symbol = entry.getValue();
            System.out.println(symbol.name + "\t" + symbol.address);
        }

        System.out.println("\nLiteral Table:");
        System.out.println("Literal\tAddress");
        for (Literal literal : literalTable) {
            System.out.println(literal.value + "\t" + literal.address);
        }

        System.out.println("\nPool Table:");
        for (int i = 0; i <= iPT; i++) {
            System.out.println("Pool " + i + ": " + PT[i]);
        }
         
        int constantCount = 0;
        int literalCount = 0;
        int symbolCount = 0;
        for (String line : assemblyCode) {
            String[] parts = line.split("\\s+");
            StringBuilder intermediateCode = new StringBuilder();
            
            for (int i = 0; i < parts.length; i++) {
                String current = parts[i];
                if (opcode.containsKey(parts[i])) {
                    String lineCurrentCode = "(" + opcode.get(parts[i]).type + "," + opcode.get(parts[i]).code + ")";
                    intermediateCode.append(lineCurrentCode).append(" ");
                } else if (parts[i].matches("\\d+")) {
                    intermediateCode.append("(C,").append(parts[i]).append(") ");
                } else if (parts[i].contains(",")) {
                    String[] tokens = parts[i].split(",");
                    if (tokens.length == 2) {
                        if ("AREG".equals(tokens[0]) || "BREG".equals(tokens[0]) || "CREG".equals(tokens[0]) || "DREG".equals(tokens[0])) {
                            intermediateCode.append("(R) ");
                        }
                        if (tokens[1].contains("=")) {
                            literalCount++;
                            String lineCurrentCode = "(L,0" + literalCount + ")";
                            intermediateCode.append(lineCurrentCode).append(" ");
                        } else {
                            symbolCount++;
                            String lineCurrentCode = "(S,0" + symbolCount + ")";
                            intermediateCode.append(lineCurrentCode).append(" ");
                        }
                    }
                }
            }
            System.out.println(intermediateCode);
        }
            
    }


    private static void initializeOpcodeTable(Map<String, opTable> opcode) {
        opTable obj = new opTable("IS","00");
        opcode.put("STOP",obj);
        obj = new opTable("IS","01");
        opcode.put("ADD",obj);
        obj = new opTable("IS","02");
        opcode.put("SUB",obj);
        obj = new opTable("IS","03");
        opcode.put("MULT",obj);
        obj = new opTable("IS","04");
        opcode.put("MOVER",obj);
        obj = new opTable("IS","05");
        opcode.put("MOVEM",obj);
        obj = new opTable("IS","06");
        opcode.put("COMB",obj);
        obj = new opTable("IS","07");
        opcode.put("BC",obj);
        obj = new opTable("IS","08");
        opcode.put("DIV",obj);
        obj = new opTable("IS","09");
        opcode.put("READ",obj);
        obj = new opTable("IS","10");
        opcode.put("PRINT",obj);
        obj = new opTable("AD","01");
        opcode.put("START",obj);
        obj = new opTable("AD","02");
        opcode.put("END",obj);
        obj = new opTable("AD","03");
        opcode.put("ORIGIN",obj);
        obj = new opTable("AD","04");
        opcode.put("EQU",obj);
        obj = new opTable("AD","05");
        opcode.put("LTORG",obj);
        obj = new opTable("DL","01");
        opcode.put("DS",obj);
        obj = new opTable("DL","01");
        opcode.put("DC",obj);
        obj = new opTable("NA","00");
        opcode.put("AREG",obj);
        obj = new opTable("NA","01");
        opcode.put("BREG",obj);
        obj = new opTable("NA","02");
        opcode.put("CREG",obj);
    }
}