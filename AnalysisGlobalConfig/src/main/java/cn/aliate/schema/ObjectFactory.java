package cn.aliate.schema;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {}

    public GlobalConfig createGlobalConfig() {
        return new GlobalConfig();
    }

    public GlobalConfig.Config createGlobalConfigConfig() {
        return new GlobalConfig.Config();
    }
}
