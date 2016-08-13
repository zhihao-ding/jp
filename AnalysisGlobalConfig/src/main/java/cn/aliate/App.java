package cn.aliate;

import cn.aliate.schema.GlobalConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.lang.StringBuilder;

public class App 
{
    private JAXBContext context;
    private Map<String, GlobalConfig.Config> configsFromXml = new HashMap<String, GlobalConfig.Config>();

    private String getConfigFolderFromEnvironment() {
        String configFolder = System.getenv().get("GLOBAL_CONFIG_FOLDER");
        if (configFolder == null) {
            throw new RuntimeException("No variable GLOBAL_CONFIG_FOLDER in environment");
        }
        return configFolder;
    }

    private List<String> scanFolder(String folderName) {
        try {
            File folder = new File(folderName);
            String[] files = folder.list();
            List<String> ret = new ArrayList<String>();
            for (String f: files) {
                ret.add(String.format("%s/%s", folder.getAbsolutePath(), f));
            }
            return ret;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private String getIdentity(GlobalConfig.Config config) {
        return String.format("%s.%s", config.getCategory(), config.getName());
    }

    private void parseConfig(File file) throws JAXBException, FileNotFoundException {
        if (!file.getName().endsWith(".xml")) {
            System.out.println(String.format("file[%s] in global config folder is not end with .xml, skip it!",
                                             file.getAbsolutePath()));
            return;
        }

        Unmarshaller unmarshaller = context.createUnmarshaller();
        GlobalConfig gbc = (GlobalConfig)unmarshaller.unmarshal(file);
        for (GlobalConfig.Config c: gbc.getConfig()) {
            String category = c.getCategory();
            category = category == null ? "Others" : category;
            c.setCategory(category);
            if (c.getValue() == null) {
                c.setValue(c.getDefaultValue());
            }
            if (c.getDefaultValue() == null) {
                throw new IllegalArgumentException(String.format("GlobalConfig[category: %s, name: %s] must have a default value",
                                                                 c.getCategory(), c.getName()));
            }
            configsFromXml.put(getIdentity(c), c);
        }
    }

    public void dump(String category) {
        for (Map.Entry<String, GlobalConfig.Config> e: configsFromXml.entrySet()) {
            GlobalConfig.Config config = e.getValue();
            if (category != null && !category.equals(config.getCategory())) {
                continue;
            }
            System.out.println(String.format("%s: %s", getIdentity(config), config.getDefaultValue()));
        }
    }

    public void loadConfigFromXml() throws JAXBException, FileNotFoundException {
        context = JAXBContext.newInstance("cn.aliate.schema");
        List<String> filePaths = scanFolder(getConfigFolderFromEnvironment());
        for (String path: filePaths) {
            File f = new File(path);
            parseConfig(f);
        }
    }

    public static void main( String[] args )
    {
        try {
            App app = new App();
            app.loadConfigFromXml();
            String category = null;
            if (args.length > 0) {
                category = args[0];
            }
            app.dump(category);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
