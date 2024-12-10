package ui;

public class AlphabetToNums {
    public int turnAlphtoInt(String alph){
        if(alph.equalsIgnoreCase("A")){
            return 1;
        }
        else if(alph.equalsIgnoreCase("B")){
            return 2;
        }
        else if(alph.equalsIgnoreCase("C")){
            return 3;
        }
        else if(alph.equalsIgnoreCase("D")){
            return 4;
        }
        else if(alph.equalsIgnoreCase("E")){
            return 5;
        }
        else if(alph.equalsIgnoreCase("F")){
            return 6;
        }
        else if(alph.equalsIgnoreCase("G")){
            return 7;
        }
        else if(alph.equalsIgnoreCase("H")){
            return 8;
        }
        return 0;
    }
    public int getIntfromAlph(String abcdefghijklmnop){
        return turnAlphtoInt(abcdefghijklmnop);
    }
}
