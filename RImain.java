import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class RImain {
	
	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	
	public static boolean existe(ArrayList<String> list,String nome){
		for(int i=0;i<list.size();i++){
			if(nome.equals(list.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		ArrayList<Produto> ListaProdutos = new ArrayList<Produto>();
		ArrayList<String> vocab = new ArrayList<String>();
		
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("src/textDescDafitiPosthaus.xml");
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		if (dom != null) {
			Element docEle = dom.getDocumentElement();
	
			NodeList nl = docEle.getElementsByTagName("produto");
			
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength(); i++) {
					Element el = (Element)nl.item(i);	
					Produto aux = new Produto(
						getTextValue(el, "id"), 
						getTextValue(el, "categoria"),
						getTextValue(el,"titulo"),
						getTextValue(el,"descricao"),
						getTextValue(el,"img"),
						getTextValue(el,"preco")
					);
					
					ListaProdutos.add(aux);
					
				}
			}
		}

		File vocabulario = new File("vocabulario.txt");
		try {
			vocabulario.createNewFile();
			FileWriter fw = new FileWriter(vocabulario.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i = 0;i<ListaProdutos.size();i++){
				ListaProdutos.get(i).descricao=ListaProdutos.get(i).descricao.replace(",","");
				ListaProdutos.get(i).descricao=ListaProdutos.get(i).descricao.replace(":"," ");
				ListaProdutos.get(i).descricao=ListaProdutos.get(i).descricao.replace("-"," ");
				ListaProdutos.get(i).descricao=ListaProdutos.get(i).descricao.replace(".","");
				ListaProdutos.get(i).descricao=ListaProdutos.get(i).descricao.toLowerCase();
				String[] aux2 = ListaProdutos.get(i).descricao.split(" ");
				for(int j = 0;j<aux2.length;j++){
					if(!existe(vocab,aux2[j])){
						vocab.add(aux2[j]);
					}
				}

			}
			
			for(int x = 0;x<vocab.size();x++){
				if(vocab.get(x).contains("-")){
					String aux2=vocab.get(x);
					String[] aux=vocab.get(x).split("-");
					for(int i=0;i<aux.length;i++){
						if(!existe(vocab,aux[i])){
							vocab.add(aux[i]);
						}
					}
				vocab.remove(aux2);
				}
			}
			for(int x = 0;x<vocab.size();x++){
				if(vocab.get(x).contains("/")){
					String aux2=vocab.get(x);
					String[] aux=vocab.get(x).split("/");
					for(int i=0;i<aux.length;i++){
						if(!existe(vocab,aux[i])){
							vocab.add(aux[i]);
						}
					}
				vocab.remove(aux2);
				}
			}
			Hashtable<String,Integer> indice = new Hashtable<String,Integer>();
			for(int i=0;i<vocab.size();i++){
				indice.put(vocab.get(i), i);
				
			}
			 ArrayList<ArrayList<Integer>> ocorre = new ArrayList<ArrayList<Integer>>();
			 for(int i = 0;i<vocab.size();i++){
				 ArrayList<Integer> item = new ArrayList<Integer>();
				 ocorre.add(i,item);
			 }
			 
			for(int i = 0;i<ListaProdutos.size();i++){
				String[] aux3 = ListaProdutos.get(i).descricao.split(" ");
				for(int j=0;j<aux3.length;j++){
					Integer n = indice.get(aux3[j]);
					if(n != null){
						ocorre.get(j).add(i);
					}
				}
			}
			
			
			
			
			for(int j = 0; j<vocab.size();j++){
				bw.write(vocab.get(j));
				bw.newLine();
			}
			System.out.println(vocab.size());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

}
