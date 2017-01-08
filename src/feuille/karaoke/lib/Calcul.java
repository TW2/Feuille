/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package feuille.karaoke.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>This class is a module for calculation.<br />
 * Cette classe est un module pour le calcul.</p>
 * @author The Wingate 2940
 */
public class Calcul {

    private String expression = null;
    private String error = null;
    
    /** <p>Create a new Calcul.<br />Crée un nouveau Calcul.</p> */
    public Calcul(){
        //nothing : expression is null
    }

    /** <p>Create a new Calcul with an expression to solve.<br />
     * Crée un nouveau Calcul avec une expression à résoudre..</p> */
    public Calcul(String expression){
        this.expression = expression;
    }
    
    /** <p>Do all calculations.<br />Fait tous les calculs.</p> */
    public String doCalcul(String expression, boolean inFloat){
        
        expression = multiply(expression);
        expression = divide(expression);
        expression = subtract(expression);
        expression = add(expression);
        expression = removeSigns(expression);
        if(isNumber(expression)==false){
            expression = doCalcul(expression,true);
        }
        
        if(error==null){
            if(inFloat==true){
                return expression;
            }else{
                float i = Float.parseFloat(expression);
                return Math.round(i)+"";
            }            
        }else{
            return "ERROR = "+error;
        }        
    }

    /** <p>Do all calculations in float or not.<br />
     * Fait tous les calculs en virgule flottante ou non.</p> */
    public String doCalcul(boolean inFloat){
        return doCalcul(expression,inFloat);
    }
    
    /** <p>Do the calculation : multiply.<br />Fait le calcul : multiplier.</p> */
    private String multiply(String expression){
        // Check if we find the * symbol, if not just return 'expression'.
        if(expression.indexOf("*")!=-1 && error==null){
            Pattern p = Pattern.compile("(-?\\d+\\.?\\d*)\\*(-?\\d+\\.?\\d*)");
            Matcher m = p.matcher(expression);

            if(m.find()){
                Float val1, val2;

                try{
                    val1 = Float.parseFloat(m.group(1));
                    val2 = Float.parseFloat(m.group(2));
                    
                    Float val = val1 * val2;

                    expression = expression.substring(0,m.start()) +
                            Float.toString(val) +
                            expression.substring(m.end());
                }catch(Exception exc){
                    error = exc.getLocalizedMessage();
                }
                
            }
            
        }        
        return expression;
    }

    /** <p>Do the calculation : divide.<br />Fait le calcul : diviser.</p> */
    private String divide(String expression){
        // Check if we find the / symbol, if not just return 'expression'.
        if(expression.indexOf("/")!=-1 && error==null){
            Pattern p = Pattern.compile("(-?\\d+\\.?\\d*)/(-?\\d+\\.?\\d*)");
            Matcher m = p.matcher(expression);

            if(m.find()){
                Float val1, val2;
                
                try{
                    val1 = Float.parseFloat(m.group(1));
                    val2 = Float.parseFloat(m.group(2));
                    
                    Float val = val1 / val2;

                    expression = expression.substring(0,m.start()) +
                            Float.toString(val) +
                            expression.substring(m.end());
                }catch(Exception exc){
                    error = exc.getLocalizedMessage();
                }
                
            }
            
        }
        return expression;
    }

    /** <p>Do the calculation : subtract.<br />Fait le calcul : soustraire.</p> */
    private String subtract(String expression){
        // Check if we find the - symbol, if not just return 'expression'.
        if(expression.indexOf("-")!=-1 && error==null){
            Pattern p = Pattern.compile("(-?\\d+\\.?\\d*)-(-?\\d+\\.?\\d*)");
            Matcher m = p.matcher(expression);

            if(m.find()){
                Float val1, val2;
                
                try{
                    val1 = Float.parseFloat(m.group(1));
                    val2 = Float.parseFloat(m.group(2));
                    
                    Float val = val1 - val2;

                    expression = expression.substring(0,m.start()) +
                            Float.toString(val) +
                            expression.substring(m.end());
                }catch(Exception exc){
                    error = exc.getLocalizedMessage();
                }
                
            }
            
        }
        return expression;
    }
    
    /** <p>Do the calculation : add.<br />Fait le calcul : ajouter.</p> */
    private String add(String expression){
        // Check if we find the + symbol, if not just return 'expression'.
        if(expression.indexOf("+")!=-1 && error==null){
            Pattern p = Pattern.compile("(-?\\d+\\.?\\d*)\\+(-?\\d+\\.?\\d*)");
            Matcher m = p.matcher(expression);

            if(m.find()){
                Float val1, val2;

                try{
                    val1 = Float.parseFloat(m.group(1));
                    val2 = Float.parseFloat(m.group(2));
                    
                    Float val = val1 + val2;

                    expression = expression.substring(0,m.start()) +
                            Float.toString(val) +
                            expression.substring(m.end());
                }catch(Exception exc){
                    error = exc.getLocalizedMessage();
                }
                
            }
            
        }
        return expression;
    }

    /** <p>Remove signs.<br />Enlève les signes.</p> */
    private String removeSigns(String expression){
        if(error==null){
            Pattern p = Pattern.compile("\\({1}(-?\\d+\\.?\\d*)\\){1}");
            Matcher m = p.matcher(expression);

            if(m.find()){
                expression = expression.substring(0,m.start()) +
                        m.group(1) +
                        expression.substring(m.end());
            }
        }
        
        return expression;
    }

    /** <p>Check if the expression is a number.<br />
     * Vérifie que l'expression soit un nombre.</p> */
    private boolean isNumber(String expression){
        if(error==null){
            try{
                Float f = Float.parseFloat(expression);
                return true;
            }catch(Exception exc){
                return false;
            }
        }else{
            // Force return true because an error has occurred.
            // The result is a quick ending of Calcul operations.
            return true;
        }
    }

}
