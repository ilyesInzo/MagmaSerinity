
import com.magma.bibliotheque.TraitementDate;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author symatique001
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Date date = new Date();
        
        Long d = TraitementDate.returnDateJour(date);
        
        
//        Long d = TraitementDate.returnDiffJour( dt,date) ;
    
        System.out.println(date.getTime()+"***");
        System.out.println(d + "---");
        
    }
    
}
