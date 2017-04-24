/**
 * 
 */
package socialcenter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ui.Preferences;
import ui.general.UIHelper;

/**
 * @author harirajan
 *
 */
public class SocialCenterView {

	private BorderPane myRoot;
	private Stage myStage;

	public SocialCenterView(Stage s) {
		myRoot = new BorderPane();
		myRoot.setLeft(createLeftPane());
		myRoot.setCenter(new Rectangle());
		myStage = s;
		myStage.setScene(new Scene(myRoot));
		myStage.setWidth(800);
		myStage.setHeight(800);
		myStage.show();
	}

	private Node createLeftPane() {
		VBox vb = new VBox();
		Label reviewsLabel = new Label("Reviews");
		reviewsLabel.setTextFill(Color.WHITE);
		reviewsLabel.setFont(Preferences.FONT_MEDIUM_BOLD);
		Label chatLabel = new Label("Chat");
		chatLabel.setTextFill(Color.WHITE);
		chatLabel.setFont(Preferences.FONT_MEDIUM_BOLD);
		StackPane reviews = UIHelper.buttonStack(e -> reviewsButtonHandler(), Optional.of(reviewsLabel),
				Optional.ofNullable(null), Pos.CENTER_LEFT, true);
		StackPane chat = UIHelper.buttonStack(e -> chatButtonHandler(), Optional.of(chatLabel),
				Optional.ofNullable(null), Pos.CENTER_LEFT, true);
		reviews.setPrefWidth(125);
		chat.setPrefWidth(125);
		vb.getChildren().addAll(reviews, chat);
		return vb;
	}

	/**
	 * @return
	 */
	private void chatButtonHandler() {
		// TODO Auto-generated method stub

		System.out.println("CHAT");
	}

	/**
	 * @return
	 */
	private void reviewsButtonHandler() {
		// TODO Auto-generated method stub
		VBox vb = new VBox();
		RatingEntry re = new RatingEntry();
		re.addSubmitEventHandler(e -> submit(re.getUser(), re.getRating(), re.getReview()));
		vb.getChildren().add(re);
		vb.getChildren().add(new RatingDisplay("reviews.xml"));
		ScrollPane sp = new ScrollPane(vb);
		myRoot.setCenter(sp);

		System.out.println("REVIEWS");
	}

	private void submit(String user, int rating, String review) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File("reviews.xml"));
			Element reviewElement = addElement(doc, "user_review", "", doc.getDocumentElement());
			addElement(doc, "username", user, reviewElement);
			addElement(doc, "rating", Integer.toString(rating), reviewElement);
			addElement(doc, "review", review, reviewElement);
			
			saveXML("reviews.xml", doc);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		reviewsButtonHandler();
		
	}

	private Element addElement(Document doc, String elementTitle, String elementData, Element root) {
		Element newElement = doc.createElement(elementTitle);
		newElement.appendChild(doc.createTextNode(elementData));
		root.appendChild(newElement);
		return newElement;
	}
	
	private void saveXML(String filePath, Document doc) throws TransformerException, IOException {
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer transformer = transfac.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = stringWriter.toString();
		FileWriter fileWriter = new FileWriter(filePath);
		fileWriter.write(xmlString);
		fileWriter.close();
	}

}
