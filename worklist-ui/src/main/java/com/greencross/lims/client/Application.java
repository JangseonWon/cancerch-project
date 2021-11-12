package com.greencross.lims.client;

import com.google.gwt.core.client.EntryPoint;
import com.greencross.lims.client.worklist.WorklistDetailElement;
import com.greencross.lims.client.worklist.WorklistElement;
import elemental2.dom.DomGlobal;
import org.jboss.elemento.Elements;

public class Application implements EntryPoint {
   public void onModuleLoad(){
       String hash = DomGlobal.window.location.hash;
       if(hash == null || hash.trim().isEmpty()) Elements.body().add(WorklistElement.instance());
       else try{
           String param = hash.substring(1);
           if("link".equalsIgnoreCase(param)){
                String[] params = DomGlobal.window.location.search.substring(1).split("#");
               Elements.body().add(WorklistDetailElement.instance(params[0]));
           }
       } catch(Exception ignore){
           Elements.body().add(WorklistElement.instance());
       }
   }
}
