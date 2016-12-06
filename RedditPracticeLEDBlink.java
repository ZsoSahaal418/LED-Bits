package redditpracticeledblink;
import java.util.Arrays;

public class RedditPracticeLEDBlink {
    static int mark = 0;
    static final int ASCII = 97;
    static int[] reg = new int[2];

    public static void main(String[] args){
        String[] instSet = {"  ld a,14",
                "  out (0),a",
                "  ld a,12",
                "  out (0),a",
                "  ld a,8",
                "  out (0),a",
                "  out (0),a",
                "  ld a,12",
                "  out (0),a",
                "  ld a,14",
                "  out (0),a"};
        String[] instSet2 = {"  ld b,3",
                "",
                "triple:",
                "  ld a,126",
                "  out (0),a",
                "  ld a,60",
                "  out (0),a",
                "  ld a,24",
                "  out (0),a",
                "  djnz triple"};
        String[] instSet3 = {"  ld a,1",
                "  ld b,9",
                "",
                "loop:",
                "  out (0),a",
                "  rlca",
                "  djnz loop"};
        String[] instSet4 = {"  ld a,2",
                "  ld b,9",
                "",
                "loop:",
                "  out (0),a",
                "  rrca",
                "  djnz loop"};
        System.out.println("--------- Sample Set 1 ---------");
        executeI(instSet, instSet);
        System.out.println("--------- Sample Set 2 ---------");
        executeI(instSet2, instSet2);
        System.out.println("--------- Sample Set 3 ---------");
        executeI(instSet3, instSet3);
        System.out.println("--------- Sample Set 4 ---------");
        executeI(instSet4, instSet4);
    }

    static void executeI(String[] instSet, String[] origInstSet){
        int index = 0;
        for(String instr : instSet){
            switch(readInput(instr)){
                case OUT:
                    System.out.println(outputR(reg, instr.charAt(instr.length()-1)-ASCII));
                    break;
                case DECR:
                    if(reg[1] - 1 > 0){
                        reg[1]--;
                        executeI((String[]) 
                                Arrays.copyOfRange(origInstSet, mark + 1, origInstSet.length), origInstSet);
                    }
                    break;
                case LABEL:
                    mark = index;
                    break;
                case LOAD:
                    String[] r = instr.split(",");
                    r[0].charAt(r[0].length() - 1);
                    reg[r[0].charAt(r[0].length() - 1)-ASCII] = Integer.parseInt(r[1]);
                    break;
                case ROTATER:
                    reg['a'-ASCII] = rotateB(reg['a'-ASCII], false);
                    break;
                case ROTATEL:
                    reg['a'-ASCII] = rotateB(reg['a'-ASCII], true);
                    break;
                default:
                    break;
            }
            index++;
        }
    }

    static Inst readInput(String s){
        if(s.contains("ld")){
            return Inst.LOAD;
        } else if (s.contains("out")){
            return Inst.OUT;
        } else if (s.contains(":")){
            return Inst.LABEL;
        }  else if (s.contains("djnz")){
            return Inst.DECR;
        }  else if (s.contains("rlca")){
            return Inst.ROTATEL;
        }  else if (s.contains("rrca")){
            return Inst.ROTATER;
        }
        return Inst.UNKNOWN;
    }

    static int rotateB(int num, boolean left){
        String bits = convertToEightBitString(num);
        String newBits = null;
        if(left){
            newBits = bits.substring(1, bits.length()).concat(bits.substring(0,1));
        } else {
            newBits = bits.substring(bits.length()-1, bits.length()).concat(bits.substring(0, bits.length()-1));
        }
        return Integer.parseInt(newBits, 2);
    }

    static String convertToEightBitString(int num){
        String bitString = Integer.toBinaryString(num);
        String begin = "00000000";
        return begin.substring(0, begin.length() - bitString.length()) + bitString;
    }
    
    static String outputR(int[] regs, int regNum){
        String output = convertToEightBitString(regs[regNum]);
        output = output.replaceAll("0", ".").replaceAll("1", "*");
        return output;
    }
    
    static enum Inst{
        LOAD,
        OUT,
        LABEL,
        ROTATEL,
        ROTATER,
        DECR,
        UNKNOWN
    }
}
