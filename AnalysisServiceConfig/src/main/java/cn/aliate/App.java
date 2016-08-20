package cn.aliate;

import cn.aliate.schema.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

class ServiceConfig {
    private String filename;
    private Service service;

    public ServiceConfig(String filename, Service service) {
        this.filename = filename;
        this.service = service;
    }

    public String getFilename() {
        return filename; 
    }
    
    public void setFilename(String filename) {
        this.filename = filename; 
    }
    
    public Service getService() {
        return service; 
    }
    
    public void setService(Service service) { 
        this.service = service; 
    }
}

public class App 
{
    private static final String CONFIG_FOLDER 
        = "/Users/zhihao/github/zstack/conf/serviceConfig";

    private static Map<String, Set<ServiceConfig>> services = new HashMap<String, Set<ServiceConfig>>();


    private static List<String> scanFolder(String folderName) {
        try {
            File folder = new File(folderName);
            String[] files = folder.list();
            List<String> ret = new ArrayList<String>(files.length);
            for (String f: files) {
                ret.add(String.format("%s/%s", folder.getAbsolutePath(), f));
            }
            return ret;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void populateService(String filename, Service schema) {
        Set<ServiceConfig> scs = services.get(schema.getId());
        if (scs == null) {
            scs = new HashSet<ServiceConfig>();
            services.put(schema.getId(), scs);
        }
        ServiceConfig sc = new ServiceConfig(filename, schema);
        scs.add(sc);
    }

    private static void showAllServices() {
        System.out.println("All services: ");
        for (String serviceId: services.keySet()) {
            System.out.println(serviceId);
        }
    }

    private static void showServiceDetails(String serviceId) {
        System.out.println("Show info of service: " + serviceId);
        Set<ServiceConfig> scs = services.get(serviceId);
        if (scs == null ) {
            System.out.println("No info about service: " + serviceId);
        }
        for (ServiceConfig sc: scs) {
            System.out.println();
            System.out.println("config file: " + sc.getFilename());
            System.out.println("intercepter: " + sc.getService().getInterceptor());
            for (Service.Message msg: sc.getService().getMessage()) {
                System.out.println(msg.getName());
            }
        }
    }

    private static void showDescription(Service schema, String cfgPath) {
        System.out.println("\tid: " + schema.getId());
        for (String interceptor: schema.getInterceptor()) {
            System.out.println("\tinterceptor: " + interceptor);
        }
        for (Service.Message msg: schema.getMessage()) {
            System.out.println("\t" + msg.getName() + ": " + msg.getServiceId());
        }
    }

    public static void main( String[] args ) {
        try {
            JAXBContext context = JAXBContext.newInstance("cn.aliate.schema");
            List<String> paths = scanFolder(CONFIG_FOLDER);
            for (String p: paths) {
                if (!p.endsWith(".xml")) {
                    System.out.println(String.format("ignore %s which is not ending with .xml", p));
                    continue;
                }

                //System.out.println("process file: " + p);
                File cfg = new File(p);
                Unmarshaller ums = context.createUnmarshaller();
                Service schema = (Service)ums.unmarshal(cfg);
                //showDescription(schema, cfg.getAbsolutePath());
                populateService(p, schema);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        if (args.length == 0) {
            showAllServices();
        } else {
            showServiceDetails(args[0]);
        }
    }
}
