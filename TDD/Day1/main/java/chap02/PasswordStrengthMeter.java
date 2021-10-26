package chap02;

public class PasswordStrengthMeter {

    public PasswordStrength meter(String s){
        if(!isPasswordValid(s)){
            return PasswordStrength.INVALID;
        }
        int metCount = getMetCriteriaCount(s);

        if(metCount <= 1) return PasswordStrength.WEAK;
        if(metCount == 2) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;

    }

    private int getMetCriteriaCount(String s) {
        int metCount = 0;
        if(isLengthOverEight(s))metCount++;
        if(isPasswordContainsNumber(s))metCount++;
        if(isPasswordContainsUpperCase(s))metCount++;
        return metCount;
    }

    private boolean isLengthOverEight(String s){
        return s.length() >= 8;
    }

    private boolean isPasswordContainsNumber(String s){
        for(int i = 0 ; i < s.length(); i++){
            if(Character.isDigit(s.charAt(i))){
                return true;
            }
        }
        return false;
    }
    private boolean isPasswordContainsUpperCase(String s){
        for(int i = 0 ; i < s.length(); i++){
            if(s.charAt(i) >= 65 && s.charAt(i) <= 90 ){
                return true;
            }
        }
        return false;
    }

    private boolean isPasswordValid(String s){
        if(s == null || s.isEmpty()){
            return false;
        }
        return true;
    }
}
