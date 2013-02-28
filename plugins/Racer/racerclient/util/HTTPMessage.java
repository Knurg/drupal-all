package racerclient.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.*;

/**
 * <p>Description: This class represents a http message.
 * a http message consists of a header and a body. methods for manupulating both are given.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Fachhochschule Wedel</p>
 * @author Christian Finckler(christian.finckler@gmx.de)
 * @version 1.0
 */

public abstract class HTTPMessage implements SerializableMessage {

  protected HashMap headerFields;

  protected byte[] messageBody;

  protected HTTPMessage(){
    headerFields = new HashMap();
    messageBody = null;
  }

  public String getHeaderField(String fieldName){
    return (String)headerFields.get(fieldName.toLowerCase());
  }

  public void setHeaderField(String fieldName, String fieldValue){
    headerFields.put(fieldName.toLowerCase(), fieldValue);
  }

  public byte[] getMessageBody(){
    return messageBody;
  }

  public void setMessageBody(byte[] messageBody){
    this.messageBody = messageBody;
    setHeaderField("content-length", String.valueOf(messageBody.length));
  }

  /**
   * This method reads the header-fields and the body from the given stream
   * @param istream the input stream
   * @throws IOException is thrown if an error while reading occurs or if the
   * input stream does not deliver the appropriate message syntax
   */
  public void readFromStream(DataInputStream istream) throws IOException{
    headerFields = new HashMap();
    messageBody = null;
    String line;
    while(!(line = readLine(istream)).equals("")){
      String fieldName = line.substring(0,line.indexOf(":"));
      String fieldValue = line.substring(line.indexOf(" ")+1,line.length());
      setHeaderField(fieldName, fieldValue);
      //System.out.println(fieldName + ": " +fieldValue);
    }
    readBody(istream);
  }

  /**
   * This method writes the header-fields and the body to the given stream
   * @param ostream the output stream
   * @throws IOException is thrown if an writing error occurs
   */
  public void writeToStream(DataOutputStream ostream) throws IOException{
    Iterator fieldIterator = headerFields.entrySet().iterator();
    while (fieldIterator.hasNext()){
      Map.Entry entry = (Map.Entry)fieldIterator.next();
      ostream.write((entry.getKey() + ": " + entry.getValue() + "\r\n").getBytes());
    }
    ostream.write("\r\n".getBytes());
    if(messageBody != null)
      ostream.write(messageBody);
    ostream.flush();
  }

  /**
   * This method tries to read the body from the given http message.
   * At first it tries to read the body as chunked-encoded. Second the
   * content-length is used to read the body. At last it is assumed, that the
   * body end with a line-end.
   * @param istream the input stream
   * @throws IOException is thrown if an error while reading occurs or if the
   * input stream does not deliver the appropriate message syntax
   */
  protected void readBody(DataInputStream istream) throws IOException{
    if((getHeaderField("transfer-encoding") != null) && (!getHeaderField("transfer-encoding").equals("identity"))){
      messageBody = new byte[0];
      int chunkSize;
      String line = readLine(istream);
      if(line.indexOf(";") == -1){
        chunkSize = Integer.parseInt(line,16);
      }else
        chunkSize = Integer.parseInt(line.substring(0, line.indexOf(";")),16);
      while(chunkSize > 0){
        byte[] tempArray = new byte[messageBody.length + chunkSize];
        istream.read(tempArray,messageBody.length,chunkSize);

        System.arraycopy(messageBody, 0, tempArray, 0, messageBody.length);
        messageBody = tempArray;
        line = readLine(istream); //read CRLF
        line = readLine(istream);
        if(line.equals(""))
          chunkSize = 0;
        else if(line.indexOf(";") == -1)
          chunkSize = Integer.parseInt(line,16);
        else
          chunkSize = Integer.parseInt(line.substring(0, line.indexOf(";")),16);
      }
      while(!(line = readLine(istream)).equals("")){
        String fieldName = line.substring(0,line.indexOf(":"));
        String fieldValue = line.substring(line.indexOf(" ")+1,line.length());
        setHeaderField(fieldName, fieldValue);
      }
      setHeaderField("content-length", String.valueOf(messageBody.length));
      headerFields.remove("transfer-encoding");
    }else if(getHeaderField("content-length") != null){
      int contentLength = Integer.parseInt((String)getHeaderField("content-length"));
      messageBody = new byte[contentLength];
      istream.read(messageBody,0,contentLength);
    }else{
      messageBody = readLine(istream).getBytes();
    }
  }

  protected String readLine(DataInputStream istream) throws IOException{
    StringBuffer sb = new StringBuffer("");
    char ch = 0;
    do{
      ch = (char)istream.readByte();
      if((ch != '\r') && (ch != '\n'))
        sb.append(ch);
      if(ch == '\r'){
        istream.mark(1);
        if('\n' != (char)istream.readByte())
          istream.reset();
      }
    }while((ch != '\r') && (ch != '\n'));
    return sb.toString();
  }

  public String toString(){
    String output = "";
    Iterator fieldIterator = headerFields.entrySet().iterator();
    while (fieldIterator.hasNext()){
      Map.Entry entry = (Map.Entry)fieldIterator.next();
      output = output + entry.getKey() + ": " + entry.getValue() + "\r\n";
    }
    if (messageBody != null)
      output = output + new String(messageBody);
    return output;
  }
}