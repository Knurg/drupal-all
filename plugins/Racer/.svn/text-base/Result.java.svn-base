import java.io.*;

class Result {
     public static void main(String[] args) {
        try{
             FileInputStream fstream = new FileInputStream("Output.xml");
             DataInputStream in = new DataInputStream(fstream);
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             String strLine;
             
             //System.out.println("<br>");
             //System.out.println("<br>");
             System.out.println("XML Output:");
             System.out.println("<br>");
             System.out.println("<br>");
                                                                      
             while ((strLine = br.readLine()) != null)   {
                   System.out.println(strLine);
                   System.out.println("\n");
                   System.out.println("<br>");
             }
  
             in.close();
        }catch (Exception e){
             System.err.println("Error: " + e.getMessage());
        }                                   
     }
}

