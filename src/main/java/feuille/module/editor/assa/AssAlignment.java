package feuille.module.editor.assa;

public class AssAlignment {
    private int number;

    public AssAlignment(int number) {
        this.number = number;
    }

    public AssAlignment() {
        number = 2;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static AssAlignment fromSSA(int ssa){
        AssAlignment ass;
        switch(ssa){
            case 1 -> { ass = new AssAlignment(1); }
            case 3 -> { ass = new AssAlignment(3); }
            case 5 -> { ass = new AssAlignment(7); }
            case 6 -> { ass = new AssAlignment(8); }
            case 7 -> { ass = new AssAlignment(9); }
            case 9 -> { ass = new AssAlignment(4); }
            case 10 -> { ass = new AssAlignment(5); }
            case 11 -> { ass = new AssAlignment(6); }
            default -> { ass = new AssAlignment(2); }
        }
        return ass;
    }

    public static int toSSA(AssAlignment ass){
        int ssa;
        switch(ass.getNumber()){
            case 1 -> { ssa = 1; }
            case 3 -> { ssa = 3; }
            case 4 -> { ssa = 9; }
            case 5 -> { ssa = 10; }
            case 6 -> { ssa = 11; }
            case 7 -> { ssa = 5; }
            case 8 -> { ssa = 6; }
            case 9 -> { ssa = 7; }
            default -> { ssa = 2; }
        }
        return ssa;
    }
}
