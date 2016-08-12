package cn.aliate;

import cn.aliate.schema.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.lang.StringBuilder;

public class App 
{
    private Map<String, ApiMessageDescriptor> descriptors = new HashMap<String, ApiMessageDescriptor>();
    private Set<String> serviceIds = new HashSet<String>();

    private static String getConfigFolderFromEnvironment() {
        String configFolder = System.getenv().get("CONFIG_FOLDER");
        if (configFolder == null) {
            throw new RuntimeException("No variable CONFIG_FOLDER in environment!");
        }
        return configFolder;
    }

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

    public App() {
        String configFolder = getConfigFolderFromEnvironment();
        try {
            JAXBContext context = JAXBContext.newInstance("cn.aliate.schema");
            List<String> paths = scanFolder(configFolder);
            for (String p: paths) {
                if (!p.endsWith(".xml")) {
                    System.out.println(String.format("ignore %s which is not ending with .xml", p));
                    continue;
                }

                File cfg = new File(p);
                Unmarshaller ums = context.createUnmarshaller();
                Service schema = (Service)ums.unmarshal(cfg);
                populateService(p, schema);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private void populateDescriptorToStringBuilder(StringBuilder sb, ApiMessageDescriptor desc) {
        sb.append("\n-----------------------------------------------------");
        sb.append(String.format("\nname: %s", desc.getName()));
        sb.append(String.format("\nconfigured service id: %s", desc.getServiceId()));
        sb.append(String.format("\nconfig path: %s", desc.getConfigPath()));
        sb.append(String.format("\ninterceptors: %s", desc.getInterceptors()));
        sb.append("\n-----------------------------------------------------");
    }

    private void showApiMessageDescriptor(String message) {
        ApiMessageDescriptor desc = descriptors.get(message);
        if (desc == null) {
            System.out.println(String.format("No Descriptor about message: %s", message));
            return;
        }
        StringBuilder sb = new StringBuilder();
        populateDescriptorToStringBuilder(sb, desc);
        System.out.println(sb.toString());
    }

    public void dump() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ApiMessageDescriptor> e: descriptors.entrySet()) {
            ApiMessageDescriptor desc = e.getValue();
            populateDescriptorToStringBuilder(sb, desc);
        }
        System.out.println(String.format("ApiMessageDescriptor dump: %s", sb.toString()));
    }

    private void prepareInterceptors(ApiMessageDescriptor desc, Service.Message msg, Service schema) {
        List<String> interceptors = new ArrayList<String>();
        interceptors.addAll(msg.getInterceptor());
        interceptors.addAll(schema.getInterceptor());
        // GlobalApiMessageInterceptor FRON END SYSTEM
        desc.setInterceptors(interceptors); 
    }

    private void populateService(String configPath, Service schema) {
        for (Service.Message msg: schema.getMessage()) {
            ApiMessageDescriptor old = descriptors.get(msg.getName());
            if (old != null) {
                throw new RuntimeException(String.format(
                            "Duplicate message description. Message[%s] is described in %s and %s",
                            msg.getName(), old.getConfigPath(), configPath));
            }

            ApiMessageDescriptor desc = new ApiMessageDescriptor();
            desc.setName(msg.getName());
            String serviceId = msg.getServiceId() != null ? msg.getServiceId() : schema.getId();
            desc.setServiceId(serviceId);
            desc.setConfigPath(configPath);

            serviceIds.add(serviceId);

            prepareInterceptors(desc, msg, schema);
            descriptors.put(msg.getName(), desc);
        }
    }

    public void showAllServices() {
        System.out.println("All services: ");
        for (String serviceId: serviceIds) {
            System.out.println("\t" + serviceId);
        }
    }

    public void showServiceInfo(String serviceId) {
        System.out.println("Show info of service: " + serviceId);
        for (Map.Entry<String, ApiMessageDescriptor> e: descriptors.entrySet()) {
            ApiMessageDescriptor desc = e.getValue();
            if (serviceId.equals(desc.getServiceId())) {
                System.out.println(desc.getName());
            }
        }
    }

    public void showMessageInfo(String messageName) {
        System.out.println("Show info of message: " + messageName);
        List<String> queryMessages = new ArrayList<String>();
        for (String message: descriptors.keySet()) {
            if (message.contains(messageName)) {
                queryMessages.add(message);
            }
        }
        for (String message: queryMessages) {
            showApiMessageDescriptor(message);
        }
    }

    public static void main( String[] args ) {
        App app = new App();

        String serviceId = null;
        String messageName = null;
        boolean dump = false;
        boolean listService = false;
        for (int i = 0; i < args.length; ++i) {
            if ("-s".equals(args[i])) {
                serviceId = args[++i];
            } else if ("-m".equals(args[i])) {
                messageName = args[++i];
            } else if ("dump".equals(args[i])) {
                dump = true;
            } else if ("list-service".equals(args[i])) {
                listService = true;
            }
        }

        if (serviceId != null) {
            app.showServiceInfo(serviceId);
        } else if (messageName != null) {
            app.showMessageInfo(messageName);
        } else if (dump) {
            app.dump();
        } else if (listService) {
            app.showAllServices();
        } else {
            System.out.println("Usage: App \\");
            System.out.println("\t-s <serviceId>");
            System.out.println("\t-m <messageName> dump list-service");
            System.out.println("\tdump list-service");
            System.out.println("\tlist-service");
        }
    }
}
