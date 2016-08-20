package cn.aliate.schema;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
    public ObjectFactory() {}
    public Service.Message createServiceMessage() { return new Service.Message(); }
    public Service createService() { return new Service(); }
}
