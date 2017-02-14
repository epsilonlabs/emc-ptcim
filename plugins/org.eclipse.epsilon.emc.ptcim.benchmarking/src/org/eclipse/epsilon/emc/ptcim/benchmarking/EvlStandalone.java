package org.eclipse.epsilon.emc.ptcim.benchmarking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.epsilon.emc.ptcim.PtcimCachedPropertyManager;
import org.eclipse.epsilon.emc.ptcim.PtcimModel;
import org.eclipse.epsilon.emc.ptcim.PtcimPropertyManager;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.evl.EvlModule;

import com.google.common.base.Stopwatch;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;

public class EvlStandalone {
	
	public static void main(String[] args) throws Exception {
		
		
		String modelId = args[0];
		String resourceStr = args[1];
		String outputFile = args[2];
		boolean propertiesAttributesCacheEnabled = Boolean.parseBoolean(args[3]);
		boolean propertiesValuesCacheEnabled = Boolean.parseBoolean(args[4]);
		
		
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
		
		File evlFile = new File("C:\\Git\\Emc-ptcim\\plugins\\org.eclipse.epsilon.emc.ptcim.benchmarking\\files\\evlConstraints.evl");
		
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
		
		m.parse(evlFile);
		System.out.println(m.getParseProblems());
		long startTime = System.currentTimeMillis();
		m.execute();
		long stopTime = System.currentTimeMillis();
		long timeElapsed = stopTime - startTime;
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
		System.out.println("Time passed: " + (timeElapsed));
		try {
		    FileWriter fw = new FileWriter(outputFile,true);
		    fw.write(modelId + "," + timeElapsed + "," + approach + "\n");
		    fw.close();
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
