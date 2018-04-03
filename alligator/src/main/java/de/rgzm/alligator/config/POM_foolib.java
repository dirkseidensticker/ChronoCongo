package de.rgzm.alligator.config;

import java.io.IOException;
import org.json.simple.JSONObject;

/**
 * Class for POM details
 * @author thiery
 */
public class POM_foolib {
    
    /**
     * get POM info as JSON
     * @return pom JSON
     * @throws IOException 
     */
    public static JSONObject getInfo() throws IOException {
        JSONObject outObj = new JSONObject();
        JSONObject maven = new JSONObject();
        maven.put("modelVersion", FoolibProperties.getPropertyParam("modelVersion"));
        maven.put("mavenCompilerSource", FoolibProperties.getPropertyParam("source"));
        maven.put("mavenCompilerTarget", FoolibProperties.getPropertyParam("target"));
        outObj.put("maven", maven);
        JSONObject project = new JSONObject();
        project.put("buildNumber", FoolibProperties.getPropertyParam("buildNumber"));
        project.put("buildNumberShort", FoolibProperties.getPropertyParam("buildNumber").substring(0, 7));
        project.put("buildRepository", FoolibProperties.getPropertyParam("url").replace(".git", "/tree/" + FoolibProperties.getPropertyParam("buildNumber")));
        project.put("artifactId", FoolibProperties.getPropertyParam("artifactId"));
        project.put("groupId", FoolibProperties.getPropertyParam("groupId"));
        project.put("version", FoolibProperties.getPropertyParam("version"));
        project.put("packaging", FoolibProperties.getPropertyParam("packaging"));
        project.put("name", FoolibProperties.getPropertyParam("name"));
        project.put("description", FoolibProperties.getPropertyParam("description"));
        project.put("url", FoolibProperties.getPropertyParam("url"));
        project.put("encoding", FoolibProperties.getPropertyParam("sourceEncoding"));
        outObj.put("project", project);
        return outObj;
    }

}
