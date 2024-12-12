package server.websocket;

public class NumsToAlphabet {
    public String turnAlphtoInt(int alph){
        if(alph == 1 ){
            return "A";
        }
        else if(alph == 2 ){
            return "B";
        }
        else if(alph == 3 ){
            return "C";
        }
        else if(alph == 4 ){
            return "D";
        }
        else if(alph == 5 ){
            return "E";
        }
        else if(alph == 6 ){
            return "F";
        }
        else if(alph == 7 ){
            return "G";
        }
        else if(alph == 8 ){
            return "H";
        }
        return "NOT AN INTEGER";
    }
    public String getIntfromAlph(int abcdefghijklmnop){
        return turnAlphtoInt(abcdefghijklmnop);
    }
}
