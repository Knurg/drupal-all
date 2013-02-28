package racerclient;

import java.io.*;
import java.net.*;
import racerclient.util.*;

/**
 * <p>Description: This class reads the input-file, sends a http-request with
 * the content of that file, recieves the http-response and writes the content
 * of the response to the output-file</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Fachhochschule Wedel</p>
 * @author Christian Finckler(christian.finckler@gmx.de)
 * @version 1.0
 */

public class RacerClient {

  public RacerClient(String fileNameIn, String fileNameOut, String host, int port) throws IOException{
    //create httpmessage
    HTTPRequest request = new HTTPRequest();
    request.setMethod("POST");
    request.setRessource("/");
    request.setVersion("HTTP/1.1");
    request.setHeaderField("host","localhost:8080");//host + ":" + port);
    request.setHeaderField("content-type","application/x-www-form-urlencoded");
    //read file
    byte[] message = null;
    try{
      DataInputStream fistream = new DataInputStream(new BufferedInputStream(new FileInputStream(fileNameIn)));
      message = new byte[fistream.available()];
      fistream.read(message, 0, message.length);
      fistream.close();
    }catch(IOException ioe){
      System.out.println("could not read inputfile");
      throw ioe;
    }
    request.setMessageBody(message);
    //connect to server
    DataInputStream sistream = null;
    DataOutputStream sostream = null;
    Socket socket = null;
    try{
      socket = new Socket(host,port);
      sistream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      sostream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }catch(IOException ioe){
      System.out.println("could not connect to server");
      throw ioe;
    }
    //send request
    MessageSenderThread httpSenderThread = new MessageSenderThread();
    httpSenderThread.start();
    httpSenderThread.sendMessage(request, sostream); //sending in extra thread
    /*try{    //sending without extra thread
      request.writeToStream(sostream);
    }catch(IOException ioe){
      System.out.println(ioe.getMessage());
    }*/
    //receive response
    HTTPResponse response100 = new HTTPResponse();
    HTTPResponse response = new HTTPResponse();
    try{
      response100.readFromStream(sistream);
      if(response100.getStatusCode().equals("100")){
        response.readFromStream(sistream);
      }
      socket.close();
    }catch(IOException ioe){
      System.out.println("could not recieve response");
      throw ioe;
    }
    httpSenderThread.stopThread();
    //save response to file
    try{
      DataOutputStream fostream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileNameOut)));
      //request.writeToStream(fostream);
      response100.writeToStream(fostream);
      if(response100.getStatusCode().equals("100"))
        response.writeToStream(fostream);
      fostream.close();
    }catch(IOException ioe){
      System.out.println("could not save file");
      throw ioe;
    }
  }

    //Main method
  public static void main(String[] args) {
    if((args.length < 2) || (args.length > 4))
      System.out.println("usage: java racerclient.RacerClient fileNameIn fileNameOut [host] [port]");
    else
      try{
        if(args.length == 2)
          new RacerClient(args[0], args[1], "localhost", 8081);
        else if(args.length == 4)
          new RacerClient(args[0], args[1], args[2], Integer.parseInt(args[3]));
      }catch(IOException ioe){
        System.out.println(ioe.getMessage());
      }
  }

  /**
   *
   * <p>Description: This private class is a thread which sends the given
   * message asynchroniosly. Every RacerClient-object has its own sender-thread.</p>
   * <p>Copyright: Copyright (c) 2002</p>
   * <p>Company: Fachhochschule Wedel</p>
   * @author Christian Finckler(christian.finckler@gmx.de)
   * @version 1.0
   */
  private class MessageSenderThread extends Thread{
    private static final int MAX_TIME_FOR_WAITING = 1000;
    private DataOutputStream ostream;
    private SerializableMessage message;
    private boolean isRunning = true;

    /**
     * gives the given message free for sending. Note that this implementation
     * uses no puffer. Thats why it can happen, that if two messages sended
     * immediately after each other, only the last one is actually sended.
     * @param message the message which should be sended
     * @param ostream the output stream
     */
    public synchronized void sendMessage(SerializableMessage message, DataOutputStream ostream){
      this.message = message;
      this.ostream = ostream;
      notify();
    }

    /**
     * The run-method of this thread waits till a new message wants to be sended.
     */
    public void run(){
      while(isRunning){
        synchronized(this){
          while((message == null) && isRunning){
            try {
              wait(MAX_TIME_FOR_WAITING);
            }catch(InterruptedException ie){}
          }
          if(message != null){
            try{
              message.writeToStream(ostream);
              message = null; ostream = null;
            }catch(IOException ioe) {
              message = null; ostream = null;
            }
          }
        }
      }
    }

    void stopThread(){
      isRunning = false;
    }

  }


}
