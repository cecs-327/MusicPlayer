package services;
import java.util.Comparator;
import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SortBasedOnHotness implements Comparator<JsonObject> {

        @Override
        public int compare(JsonObject lhs, JsonObject rhs) {

        	float leftObject;
        	float rightObject;
        	Set<String>lKey = lhs.keySet();
        	String lSong = lKey.iterator().next();
        	Set<String>rKey = rhs.keySet();
        	String rSong = rKey.iterator().next();
        	try
    		{
        		leftObject = lhs.getAsJsonObject(lSong).get("hottness").getAsFloat();
            	rightObject = rhs.getAsJsonObject(rSong).get("hottness").getAsFloat();
            	


    		}catch(Exception e)
    		{

    			try
        		{

    				leftObject = lhs.getAsJsonObject("artist").get("hotttnesss").getAsFloat();
                	rightObject = rhs.getAsJsonObject("artist").get("hotttnesss").getAsFloat();

        		}catch(Exception c)
        		{
        			System.out.println("Enter second time");
        			System.out.println("null as value");
        			leftObject = 0;
        			rightObject = 0;
        		}
    		}


            try {
                if(leftObject < rightObject)
                {

                	return 	1;

                }
                else if(leftObject > rightObject)
                {

                	return -1;
                }
                else
                {

                	return 0;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
           return 0;

        }
    }