package racerclient.util;

import java.io.*;
import java.util.*;
/**
 * <p>Description: This class represents a http response. A http response is s
 * http message plus http-version, the status-code and the reason-phrase</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Fachhochschule Wedel</p>
 * @author Christian Finckler(christian.finckler@gmx.de)
 * @version 1.0
 */

public class HTTPResponse extends HTTPMessage{

  private String version;
  private String statusCode;
  private String reasonPhrase;

  public HTTPResponse() {
    super();
    version = null;
    statusCode = null;
    reasonPhrase = null;
  }

  public String getVersion(){
    return version;
  }

  public void setVersion(String version){
    this.version = version;
  }

  public String getStatusCode(){
    return statusCode;
  }

  public void setStatusCode(String statusCode){
    this.statusCode = statusCode;
  }

  public String getReasonPhrase(){
    return reasonPhrase;
  }

  public void setReasonPhrase(String reasonPhrase){
    this.reasonPhrase = reasonPhrase;
  }

  /**
   * This method reads the http response from the given stream
   * @param istream istream the input stream
   * @throws IOException is thrown if an error while reading occurs or if the
   * input stream does not deliver the appropriate message syntax
   */
  public void readFromStream(DataInputStream istream) throws IOException{
    String line = readLine(istream);
    StringTokenizer sTokenizer = new StringTokenizer(line," ",false);
    if(sTokenizer.countTokens() < 3)
      throw new IOException("Request-Line fehlerhaft");
    version = sTokenizer.nextToken();
    statusCode = sTokenizer.nextToken();
    reasonPhrase = sTokenizer.nextToken();
    super.readFromStream(istream);
  }

  /**
   * This method writes the http response to the given stream
   * @param ostream the output stream
   * @throws IOException is thrown if an writing error occurs
   */
  public void writeToStream(DataOutputStream ostream) throws IOException{
    ostream.write((version + " " + statusCode + " " + reasonPhrase + "\r\n").getBytes());
    super.writeToStream(ostream);
  }

  protected void readBody(DataInputStream istream) throws IOException{
    if(!((statusCode.startsWith("1")) || (statusCode.equals("204")) || (statusCode.equals("304"))))
      super.readBody(istream);
  }

  public String printHeader(){
    return version + " " + statusCode + " " + reasonPhrase;
  }

  public String toString(){
    return printHeader() + "\r\n" + super.toString();
  }

  /**
   * This static method creates and returns a new HTTPResponse-object, which
   * represents a error500-response
   * @param errorMessage the individual error-message
   * @return the error500-response
   */
  public static HTTPResponse createError500Response(String errorMessage){
    HTTPResponse error500 = new HTTPResponse();
    error500.setVersion("HTTP/1.1");
    error500.setStatusCode("500");
    error500.setReasonPhrase("Internal Server Error");
    error500.setHeaderField("content-type","text/plain");
    if(errorMessage != null)
      error500.setMessageBody(errorMessage.getBytes());
    else
      error500.setMessageBody("Error 500: Internal Server Error".getBytes());
    return error500;
  }

  /**
   * This static method creates and returns a new HTTPResponse-object, which
   * represents a error400-response
   * @param errorMessage the individual error-message
   * @return the error400-response
   */
  public static HTTPResponse createError400Response(String errorMessage){
    HTTPResponse error400 = new HTTPResponse();
    error400.setVersion("HTTP/1.1");
    error400.setStatusCode("400");
    error400.setReasonPhrase("Bad Request");
    error400.setHeaderField("content-type","text/plain");
    if(errorMessage != null)
      error400.setMessageBody(errorMessage.getBytes());
    else
      error400.setMessageBody("Error 400: Bad Request".getBytes());
    return error400;
  }
}