package org.eclipse.epsilon.emc.ptcim.benchmarking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.ptcim.models.PtcimModel;

public class EvlStandalone {
	
	public static void main(String[] args) throws Exception {
		
		
		String modelId = args[0];
		String resourceStr = args[1];
		String outputFile = args[2];
		String outputErrorFile = args[3];
		String pathTxt = args[4];
		String numOfElements = args[5];
		String numOfIterations = args[6];
		boolean propertiesAttributesCacheEnabled = Boolean.parseBoolean(args[7]);
		boolean propertiesValuesCacheEnabled = Boolean.parseBoolean(args[8]);
				
		/*
		String outputFile = "C:\\Users\\astal\\Documents\\test.txt";
		String resourceStr = "\\\\Enabler\\KB_WORK\\Examples\\Traffic Lights\\0";
		String modelId = "43442142-fbf2-11d2-a53d-00104bb05af8";
		boolean propertiesAttributesCacheEnabled = true;
		bolean propertiesValuesCacheEnabled = true;
		*/
		
		EvlStandalone evlStndln = new EvlStandalone();
		StringProperties properties = new StringProperties();		
		
		String[] info = evlStndln.modelReferenceToFields(resourceStr);
		String server = info[3];
		String repository = info[4];
		String reference = info[5];
		String version = info[6];
		//System.out.println("Server: " + server + " Repository: " + repository + " Name: " + reference + " Version: " + version);
		
		properties.put("modelRef", reference);
		properties.put("server", server);
		properties.put("repository", repository);
		properties.put("version", version);
		properties.put("fromSelection", false);
		properties.put("propertiesAttributesCacheEnabled", propertiesAttributesCacheEnabled);
		properties.put("propertiesValuesCacheEnabled", propertiesValuesCacheEnabled);
		properties.put("elementId", "");
		properties.put("readOnLoad", true);
		
		IRelativePathResolver resolver = null;
		
		File evlFile = new File(pathTxt + "\\evlConstraints.evl");
		
		EvlModule m = new EvlModule();
		PtcimModel p = new PtcimModel();
		
		//p.setServer(server);
		//p.setServer(server);
		//p.setRepository(repository);
		//p.setVersion(version);
		//p.setPropertiesAttributesCacheEnabled(propertiesAttributesCacheEnabled);
		//p.setPropertiesValuesCacheEnabled(propertiesValuesCacheEnabled);
		//p.setFromSelection(false);
		//p.setModelId(modelId);
		//p.load(); 
		p.load(properties,resolver);
		
		m.getContext().getModelRepository().addModel(p);
		
		long startTimeParse = System.currentTimeMillis();
		m.parse(evlFile);
		long stopTimeParse = System.currentTimeMillis();
		long timeElapsedParse = stopTimeParse - startTimeParse;
		System.out.println("Constraints are now being checked. This window will close automatically when finished.");
		long startTime = System.currentTimeMillis();
		m.execute();
		long stopTime = System.currentTimeMillis();
		long timeElapsed = stopTime - startTime;
		ArrayList<UnsatisfiedConstraint> errors = (ArrayList<UnsatisfiedConstraint>) m.getContext().getUnsatisfiedConstraints();
		String approach = "";
		if (propertiesAttributesCacheEnabled && propertiesValuesCacheEnabled) {
			approach = "EpsilonCacheAll";
		} else if (propertiesAttributesCacheEnabled && !propertiesValuesCacheEnabled) {
			approach = "EpsilonCacheAttributesOnly";
		} else if (!propertiesAttributesCacheEnabled && propertiesValuesCacheEnabled) {
			approach = "EpsilonCacheValuesOnly";
		} else {
			approach = "EpsilonNoCache";
		}
		//System.out.println("Time passed: " + (timeElapsed));
		try {
		    FileWriter fw = new FileWriter(outputFile,true);
		    FileWriter fwError = new FileWriter(outputErrorFile,true);

		    fw.write(modelId + "," + reference + "," + numOfElements + "," + numOfIterations + "," + timeElapsedParse + "," + timeElapsed + "," + m.getContext().getUnsatisfiedConstraints().size() + ","  + approach + "\n");
		    if (approach.equals("EpsilonCacheAll")) {
		    	for (UnsatisfiedConstraint uc : errors) {
		    		fwError.write("[Epsilon]," + uc.getMessage() + "\n");
		    	}
		    }
		    fw.close();
		    fwError.close();
		}
		catch(IOException ioe)
		{
		    System.err.println("IOException: " + ioe.getMessage());
		}
		
		
	}
	
	private String[] modelReferenceToFields(String ref) {
		return ref.split("\\\\");
	}

}
