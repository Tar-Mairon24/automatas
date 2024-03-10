package practica3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpresionRegular {
    public ExpresionRegular(){

    }

    public boolean identificadoresJava(String cadena){
        String er = "^\\$?(_[a-zA-Z]|[a-zA-Z])(_|[a-zA-Z]|\\d)*$";
        Pattern pattern = Pattern.compile(er);
        Matcher matcher = pattern.matcher(cadena);
        return matcher.matches();
    }
}
