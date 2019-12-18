package pl.polsl.egradebook.model.security;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.polsl.egradebook.model.exceptions.InputException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 Stores properties for each Role.
 Properties are read from xml file.
 Example format for role is shown in comment at the beginning of the file.
 */
@Component
public class RoleProperties {

	private final String PROP_FILE="src"+File.separator+"main"+File.separator+"resources"+File.separator+"roleConfig.xml";
	private final String ROLE_TAG_NAME="role";
	private final String URL_ACCESS_TAG_NAME = "access";
	private final String ROLE_ATTRIBUTE_NAME = "name";

	/**
	 Collection holds list of allowed URL's for given role name.
	 */
	private Map<String, List<String>> roleAuthenticatorsMap;

	RoleProperties() throws InputException{
		this.roleAuthenticatorsMap =new HashMap<>();
	}

	/**
	 Function is responsible for reading XML file and filling roleAuthenticatorMap object.
	 @throws InputException When some error occurred during xml reading.
	 */
	@PostConstruct
	public void loadRoleConfig() throws InputException{
		File file = new File(PROP_FILE);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//Create DOM for file
			Document document = documentBuilder.parse(file);
			NodeList roleNodes = document.getElementsByTagName(ROLE_TAG_NAME);

			fillRoleAuthenticatorsMap(roleNodes);

		}
		catch (ParserConfigurationException e) {
			throw new InputException("Unable to create document builder \n" + e.getStackTrace());
		}
		catch (SAXException e) {
			throw new InputException("Unable to parse XML file \n" + e.getStackTrace());
		}
		catch (IOException e) {
			throw new InputException("Unable to read document. \n" + e.getStackTrace());
		}
	}

	/**
	 Function fills roleAuthenticatorMap object.
	 Based on NodeList from XML tree.
	 */
	private void fillRoleAuthenticatorsMap(NodeList roleNodes){
		for(int i=0 ; i<roleNodes.getLength();i++) {
			//Get first in line role tag
			Node currentRoleNode = roleNodes.item(i);
			//Get its child's (access tags for this role)
			NodeList roleAuthentications = currentRoleNode.getChildNodes();
			List<String> roleAllowedUrls = new ArrayList<>();

			for (int j = 0; j < roleAuthentications.getLength(); j++) {
				Node currentRoleAccessNode = roleAuthentications.item(j);
				//Bypass if tag name not valid
				if (currentRoleAccessNode.getNodeType() == Node.ELEMENT_NODE && currentRoleAccessNode.getNodeName().equals(URL_ACCESS_TAG_NAME)) {
					roleAllowedUrls.add(currentRoleAccessNode.getTextContent());
				}
			}
			try {
				NamedNodeMap nodeAttributesMap = currentRoleNode.getAttributes();
				String roleName = nodeAttributesMap.getNamedItem(ROLE_ATTRIBUTE_NAME).getNodeValue();
				this.roleAuthenticatorsMap.put(roleName, roleAllowedUrls);
			}
			catch(NullPointerException e){
				System.out.println("WARNING! [RoleProperties.java|fillRoleAuthenticatorsMap] mistake in XML file. Role bypassed.");
			}
		}
	}

	Set<String> getRoleNames(){
		return roleAuthenticatorsMap.keySet();
	}

	List<String> getPropertiesByRoleName(String roleName){
		return this.roleAuthenticatorsMap.get(roleName);
	}


}
