package org.sola.opentenure.services.boundary.ws;

//import javax.ws.rs.ApplicationPath;
//import org.glassfish.jersey.media.multipart.MultiPartFeature;
//import org.glassfish.jersey.server.ResourceConfig;
//
//@ApplicationPath("ws")
//public class ApplicationConfig extends ResourceConfig {
//
//    public ApplicationConfig() {
//        super(ClaimResource.class, 
//                LoginResource.class, 
//                ReferenceDataResource.class, 
//                MultiPartFeature.class);
//    }
//}
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

@javax.ws.rs.ApplicationPath("ws")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        resources.add(MultiPartFeature.class);
        resources.add(org.sola.opentenure.services.boundary.ws.ProtocolVersionFilter.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.sola.opentenure.services.boundary.ws.ClaimResource.class);
        resources.add(org.sola.opentenure.services.boundary.ws.DynamicFormsResource.class);
        resources.add(org.sola.opentenure.services.boundary.ws.LoginResource.class);
        resources.add(org.sola.opentenure.services.boundary.ws.ReferenceDataResourceLocalized.class);
        resources.add(org.sola.opentenure.services.boundary.ws.ReferenceDataResourceUnlocalized.class);
    }
    
}