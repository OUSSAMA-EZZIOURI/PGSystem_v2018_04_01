/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author OUSSAMA EZZIOURI
 */
public interface LoggerInterface {
    
    public void info(String msg);
    public void fine(String msg);
    public void finer(String msg);
    public void finest(String msg);
    public void warning(String msg);
    public void severe(String msg);
}
