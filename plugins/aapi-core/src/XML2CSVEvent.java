import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class XML2CSVEvent {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(args[0]);
		NodeList events = doc.getElementsByTagName("event");
		
		for (int i = 0; i < events.getLength(); i++) {
			Element event = (Element) events.item(i);
			String ename = event.getAttribute("name");
			
			NodeList items = event.getChildNodes();
			for (int j = 0; j < items.getLength(); j++) {
        if (!(items.item(j) instanceof Element)) continue;
        Element item = (Element) items.item(j);
				
				String word;
				String pos;
				if (item.getNodeName().equals("word")) {
					word = item.getTextContent();
					pos = item.getAttribute("pos");
				} else {	// phrase
					word = "";
					pos = "";
					NodeList words = item.getChildNodes();
					for (int k = 0; k < words.getLength(); k++) {
            if (!(words.item(k) instanceof Element)) continue;
						word += words.item(k).getTextContent() + " ";
						String p = ((Element) words.item(k)).getAttribute("pos");
						if (p == null) p = "";
						pos += p + " ";
					}
					word = word.substring(0, word.length() - 1);
					pos = pos.substring(0, pos.length() - 1);
				}
				
				System.out.println(ename + "\t" + pos + "\t" + word);
				
			}
		}
		
	}

}
