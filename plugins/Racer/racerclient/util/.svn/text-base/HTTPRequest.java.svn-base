package racerclient.util;

import java.io.*;
import java.util.*;

/**
 * <p>Description: This class represent a http request. A http request is a
 * http message plus request-String. The request string consists of the desired
 * method-type, the desired ressource and the http-version.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Fachhochschule Wedel</p>
 * @author Christian Finckler(christian.finckler@gmx.de)
 * @version 1.0
 */

public class HTTPRequest extends HTTPMessage{

  private String method;
  private String ressource;
  private String version;

  public HTTPRequest(){
    super();
    method = null;
    ressource = null;
    version = null;
  }

  public String getMethod(){
    return method;
  }

  public void setMethod(String method){
    this.method = method;
  }

  public String getRessource(){
    return ressource;
  }

  public void setRessource(String ressource){
    this.ressource = ressource;
  }

  public String getVersion(){
    return version;
  }

  public void setVersion(String version){
    this.version = version;
  }

  /**
   * This method reads the http request from the given stream
   * @param istream istream the input stream
   * @throws IOException is thrown if an error while reading occurs or if the
   * input stream does not deliver the appropriate message syntax
   */
  public void readFromStream(DataInputStream istream) throws IOException{
    String line = readLine(istream);
    StringTokenizer sTokenizer = new StringTokenizer(line," ",false);
    if(sTokenizer.countTokens() < 3)
      throw new IOException("Request-Line fehlerhaft");
    method = sTokenizer.nextToken();
    ressource = sTokenizer.nextToken();
    version = sTokenizer.nextToken();
    super.readFromStream(istream);
  }

  /**
   * This method writes the http request to the given stream
   * @param ostream the output stream
   * @throws IOException is thrown if an writing error occurs
   */
  public void writeToStream(DataOutputStream ostream) throws IOException{
    ostream.write((method + " " + ressource + " " + version + "\r\n").getBytes());
    super.writeToStream(ostream);
  }


  public String printHeader(){
    return method + " " + ressource + " " + version;
  }

  public String toString(){
    return printHeader() + "\r\n" + super.toString();
  }


}