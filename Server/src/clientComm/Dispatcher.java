package clientComm;
import data.*;
import gson.*;
import services.*;
import unitTests.*;

/**
* The Dispatcher implements DispatcherInterface.
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019
*/

import java.util.*;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import services.LoginServices;
import services.PlaylistServices;
import services.RegisterServices;
import services.SearchServices;
import services.SongServices;

import com.google.gson.*;




public class Dispatcher implements DispatcherInterface {
    HashMap<String, Object> ListOfObjects;


    public Dispatcher()
    {
    	/*
    	 * NOTE: there are still a lot more methods to put 
    	 * in "ListofObjects" however the only objectReference.json file
    	 * I see is the objectReference1.json in the ./Client directory
    	 * We do need to add more objects because we have more services, but 
    	 * to do that we need to make sure the both the client and server
    	 * have has to the same object reference or else when the proxy sends 
    	 * a request the dispatch will not know what its talking about
    	 */
        ListOfObjects = new HashMap<String, Object>();
        LoginServices loginServices = new LoginServices();
        RegisterServices registerServices = new RegisterServices();
        PlaylistServices playlistServices = new PlaylistServices();
        SongServices songServices = new SongServices();
        SearchServices searchServices = new SearchServices();
        
        ListOfObjects.put("LoginServices", loginServices);
        ListOfObjects.put("RegistrationServices",registerServices);
        ListOfObjects.put("PlaylistServices", playlistServices);
        ListOfObjects.put("SongServices", songServices);
        ListOfObjects.put("SearchServices", searchServices);
    }

    /*
    * dispatch: Executes the remote method in the corresponding Object
    * @param request: Request: it is a Json file
    {
        "remoteMethod":"getSongChunk",
        "objectName":"SongServices",
        "param":
          {
              "song":490183,
              "fragment":2
          }
    }
    */
    public String dispatch(String request)
    {
        /*This is going to be the jsonReturn object
        that stores the return of the function
        */
        JsonObject jsonReturn = new JsonObject();
        /*This parser is used to convert the string
        request into a json
        */
        JsonParser parser = new JsonParser();
        /*this is gonna convert a string Request
        to json
        */
       
        //JsonElement jelement = new JsonParser().parse(request.trim());
        //JsonObject  jsonRequest = jelement.getAsJsonObject();
        
        /*
         * This handles the error when the user tryies to register
         * then login. The problem is the addition rn\":\"String\"}}"
         * at the end of the request. This makes the request an invalid json 
         * string
         */
    	if(request.contains("\"return\":\"String\"}}rn\":\"String\"}}"))
    	{
    		
    		request= request.replace("\"return\":\"String\"}}rn\":\"String\"}}","\"return\":\"String\"}}");
    	}
    	
        JsonObject jsonRequest = parser.parse(request.trim()).getAsJsonObject();
      
        
        
        try {
            // Obtains the value pair object from the json object remoteMethod
         
        	
        	Object object = ListOfObjects.get(jsonRequest.getAsJsonObject("remoteMethod").get("object").getAsString());
        
        	Method[] methods = object.getClass().getMethods();
        	Method method = null;
           
            // Obtains the method
           
           /*
            * NOTE: This loop will read in all the methods contain within the class, however 
            * right now the Name: Value pair in our Client\objectReference1.json do not match 
            * with the method name in the actual class. For example: before I made any changes 
            * to the Client\objectReference1.json we had "name": "registration" but there isn't a
            * single method in the RegisterServices.java called registeration. What we have in RegisterServices.java
            * is a method called registerUser, if we want the dispatcher to run a service we must make the name in our
            * objectReference1.json and the method name in actaul class the same. 
            */
            for (int i=0; i<methods.length; i++)
            {
            	
                if (methods[i].getName().contains(jsonRequest.getAsJsonObject("remoteMethod").get("name").getAsString()))
                    method = methods[i];
            }
            if (method == null)
            {
                jsonReturn.addProperty("error", "Method does not exist");
                return jsonReturn.toString();
            }
           
            // Prepare the  parameters
            Class[] types =  method.getParameterTypes();
            Object[] parameter = new Object[types.length];
            String[] strParam = new String[types.length];
            JsonObject jsonParam = jsonRequest.getAsJsonObject("remoteMethod").get("param").getAsJsonObject();
          
            int j = 0;
            for (Map.Entry<String, JsonElement>  entry  :  jsonParam.entrySet())
            {
                strParam[j++] = entry.getValue().getAsString();
            }
            
            
            // Prepare parameters
            for (int i=0; i<types.length; i++)
            {
    
                switch (types[i].getCanonicalName())
                {
                    case "java.lang.Long":
                        parameter[i] =  Long.parseLong(strParam[i]);
                        break;
                    case "java.lang.Integer":
                        parameter[i] =  Integer.parseInt(strParam[i]);
                        break;
                    case "java.lang.String":
                        parameter[i] = new String(strParam[i]);
                        break;
                }
            }
           
           
            // Prepare the return
            Class returnType = method.getReturnType();
            String ret = "";
            
           
            switch (returnType.getCanonicalName())
                {
                    case "java.lang.Long":
                        ret = method.invoke(object, parameter).toString();
                        break;
                    case "java.lang.Integer":
                        ret = method.invoke(object, parameter).toString();
                        break;
                    case "java.lang.String":
                        ret = (String)method.invoke(object, parameter).toString();
                        break;
                }
            	JsonObject convertToJson = parser.parse(ret.trim()).getAsJsonObject();
            	jsonReturn.add("ret", convertToJson);
                System.out.println(jsonReturn);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
        	System.out.println(e);
           jsonReturn.addProperty("error", "Error on " + jsonRequest.getAsJsonObject("remoteMethod").get("object").getAsString() + "." + jsonRequest.getAsJsonObject("remoteMethod").get("name").getAsString());
        }
      
        return jsonReturn.toString();
        
       
    }

    /*
    * registerObject: It register the objects that handle the request
    * @param remoteMethod: It is the name of the method that
    *  objectName implements.
    * @objectName: It is the main class that contaions the remote methods
    * each object can contain several remote methods
    */
    public void registerObject(Object remoteMethod, String objectName)
    {
        ListOfObjects.put(objectName, remoteMethod);
    }

}
