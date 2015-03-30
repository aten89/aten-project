/**
 * 
 */
package org.eapp.oa.lucene;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

/**
 * HTML处理工具
 * @author zsy
 * @version Jun 11, 2009
 */
public class HtmlParser {
	public static final String DEFAULT_ENCODING = "utf-8";

	/**
	 * 抽取纯文本信息
	 * @param inputHtml
	 * @return
	 */
	public static String extractText(String inputHtml) {
		if (inputHtml == null) {
			return null;
		}
		Parser nodesParser = Parser.createParser(inputHtml, DEFAULT_ENCODING);
        NodeList nodeList = null;
        try {
            nodeList = nodesParser.parse(new NodeClassFilter(TextNode.class));
        } catch (ParserException e){
            e.printStackTrace();
        }
        
        if (nodeList == null) {
        	return inputHtml;
        }

        StringBuffer result = new StringBuffer();
        for (Node nextNode : nodeList.toNodeArray()){
            if (nextNode instanceof TextNode) {
            	result.append(((TextNode) nextNode).toHtml(true));
            }
        }
		return result.toString();
	}
	
	/**
	 * 抽取纯文本信息除回车及空格
	 * @param inputHtml
	 * @return
	 */
	public static String extractPlainText(String inputHtml) {
		String text = extractText(inputHtml);
		if (text != null) {
			return text.replaceAll("[\n \t]", "");
		}
		return null;
	}
	
	/**
	 * 抽取body中的纯文本信息
	 * @param inputHtml
	 * @return
	 */
	public static String extractBodyText(String inputHtml) {
		if (inputHtml == null) {
			return null;
		}
		Parser parser = Parser.createParser(inputHtml, DEFAULT_ENCODING);
		String body = null;
        try {
        	parser.setEncoding(DEFAULT_ENCODING);
			HtmlPage htmlpage = new HtmlPage(parser);
			parser.visitAllNodesWith(htmlpage);
			body = htmlpage.getBody().toHtml();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		if (body == null) {
			return inputHtml;
		}
        return extractText(body);
	}
	
}
