package racerclient.util;

import java.io.*;
/**
 * <p>Description: This interface must be implemented by all messages, which
 * should be serialized and transmitted by the RacerClient</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Fachhochschule Wedel</p>
 * @author Christian Finckler(christian.finckler@gmx.de)
 * @version 1.0
 */

public interface SerializableMessage {

  /**
   * This method reads the appropriate message from the given stream
   * @param istream the input stream
   * @throws IOException is thrown if an error while reading occurs or if the
   * input stream does not deliver the appropriate message syntax
   */
  public void readFromStream(DataInputStream istream) throws IOException;

  /**
   * This method writes the message to the given output stream
   * @param ostream the output stream
   * @throws IOException is thrown if an writing error occurs
   */
  public void writeToStream(DataOutputStream ostream) throws IOException;

}