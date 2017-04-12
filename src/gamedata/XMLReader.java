package gamedata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import factories.DataGenerator;
import factories.OptionGenerator;
import gamedata.compositiongen.Data;
import gamedata.compositiongen.HealthData;
import gamedata.reflections.Reflections;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import types.BasicActorType;

public class XMLReader {
	private GameData myGameData;
	private Map<Integer,List<Grid2D>> pathData;
	private File dataFile;
	private List<ActorData> myActors;
	private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();

	public XMLReader(File data) {
		dataFile = data;
		myGameData = new GameData();
		getData();
	}

	public XMLReader(String fileName) {
		dataFile = new File(fileName);
		myGameData = new GameData();
		getData();

	}

	private String getTextValue(Element e, String tagName) {
		NodeList nodeList = e.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		} else {
			// FIXME: empty string or null, is it an error to not find the text
			// value?
			return "";
		}
	}

	private void getData() {
		getPathData();
		try {
			getActorData();
		} catch (Exception e) {
			throw new XMLException(e);
		}
		getLevelData();
		
	}

	private void getPathData()  {
		Element root = getRootElement(dataFile);
		Element allPaths = (Element) root.getElementsByTagName("PathData").item(0);
		NodeList paths = allPaths.getElementsByTagName("Path");
		 pathData = new HashMap<Integer, List<Grid2D>>();
		for (int i = 0; i < paths.getLength(); i++) {
			String[] xCoors = getTextValue((Element) paths.item(i), "X").split(" ");
			String[] yCoors = getTextValue((Element) paths.item(i), "Y").split(" ");
			if (xCoors.length != yCoors.length) {
				// throw new XMLException();
			}
			List<Grid2D> path = new ArrayList<Grid2D>();
			for (int j = 0; j < xCoors.length; j++) {
				Coordinates coor = new Coordinates(Double.parseDouble(xCoors[j]), Double.parseDouble(yCoors[j]));
				path.add(coor);
			}
			pathData.put(i, path);
		}
		
		myGameData.setMyPaths(new PathData(pathData));

	}
	private void getActorData() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<Data>[] dataTypes=Reflections.getClasses("gamedata.composition");
		myActors=new ArrayList<ActorData>();
		Element root=getRootElement(dataFile);
		Element allActors=(Element)root.getElementsByTagName("ActorData").item(0);
		NodeList actors=allActors.getElementsByTagName("Actor");
		
		for(int i=0;i<actors.getLength();i++){
			Element actor=(Element) actors.item(i);
			String type=getTextValue(actor,"type");
			BasicActorType basicType=BasicActorType.valueOf(type);
			String imagePath=getTextValue(actor,"imagePath");
			String name=getTextValue(actor,"name");
			BasicData basicData=new BasicData(name,imagePath);
			HealthData healthData=null;
			List<Data>data=new ArrayList<Data>();
			for(Class c:dataTypes){
				System.out.println("dsad");
				String field=c.getName().split("\\.")[c.getName().split("\\.").length-1];
				Optional<String> fieldData=Optional.ofNullable(getTextValue(actor,field));
				if(fieldData.isPresent()&&fieldData.get().length()>0){
					String[]fields=fieldData.get().split(" ");
					
					Constructor<?> constr=c.getConstructors()[0];
					Class[] neededFields=constr.getParameterTypes();
				
					Object[] convertedFields=new Object[neededFields.length];
					for(int j=0;j<neededFields.length;j++){
						convertedFields[j]=StringToFieldFactory.getObject(fields[j], neededFields[j]);
						
					}
					
					Object o=DataGenerator.makeData(field.substring(0, field.length()-4), convertedFields);
					if (o instanceof HealthData){
						healthData=(HealthData)o;
					}
					else{
						data.add((Data)o);
					}
					
				}
			}
			Data[]dataArray=new Data[data.size()];
			for(int k=0;k<data.size();k++){
				dataArray[k]=data.get(k);
			}
		ActorData newActor=new ActorData(basicType,basicData,healthData,dataArray );
		myActors.add(newActor);
		myGameData.add(newActor);
		}
	}
	
	private void getLevelData(){
		Element root = getRootElement(dataFile);
		
		Element allWaves = (Element) root.getElementsByTagName("LevelData").item(0);
		double duration=Double.parseDouble(getTextValue(allWaves,"Duration"));
		double difficulty=Double.parseDouble(getTextValue(allWaves,"Difficulty"));
		Optional<Double> healthMultiplier=Optional.ofNullable(Double.parseDouble(getTextValue(allWaves,"HealthMultiplier")));
		Optional<Double> speedMultiplier=Optional.ofNullable(Double.parseDouble(getTextValue(allWaves,"SpeedMultiplier")));
		Optional<Double> attackMultiplier=Optional.ofNullable(Double.parseDouble(getTextValue(allWaves,"AttackMultiplier")));
		LevelData levelData=new LevelData(duration);
		levelData.setDifficulty(difficulty);
		levelData.setHealthMultiplier(healthMultiplier);
		levelData.setSpeedMultiplier(speedMultiplier);
		levelData.setAttackMultiplier(attackMultiplier);
		NodeList waves = allWaves.getElementsByTagName("Wave");
		for(int i=0;i<waves.getLength();i++){
			Element wave=(Element)waves.item(i);
			NodeList enemies=wave.getElementsByTagName("Enemy");
			WaveData waveData=new WaveData();
			for(int j=0;j<enemies.getLength();j++){
				Element enemy=(Element)enemies.item(j);
				Integer actorID=Integer.parseInt(getTextValue(enemy,"ActorID"));
				Integer number=Integer.parseInt(getTextValue(enemy,"Number"));
				String[]pathsUnprocessed=getTextValue(enemy,"Paths").split(" ");
				List<Integer>pathIDs=new ArrayList<Integer>();
				for(String s:pathsUnprocessed){
					pathIDs.add(Integer.parseInt(s));
				}
				EnemyInWaveData waveEnemy=new EnemyInWaveData(myActors.get(actorID),number,pathIDs);
			//waveData.addWaveEnemy(waveEnemy);	
			levelData.addWaveEnemy(myActors.get(actorID),number,pathIDs);
			}
		//levelData.addWave(waveData)
			//when multiple waves get implemented
		}
	myGameData.addLevel(levelData, 1);
	}
	
	
	private Element getRootElement(File file) {
		try {
			DOCUMENT_BUILDER.reset();
			Document xmlDocument = DOCUMENT_BUILDER.parse(file);
			return xmlDocument.getDocumentElement();
		} catch (SAXException | IOException e) {
			throw new XMLException(e);
		}
	}

	private String getAttribute(Element e, String attributeName) {
		return e.getAttribute(attributeName);
	}

	private static DocumentBuilder getDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		XMLReader x = new XMLReader("data/voogatest.xml");
		
	}
}