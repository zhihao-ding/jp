package cn.aliate.schema;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"id", "interceptor", "message"})
@XmlRootElement(name="service")
public class Service {

    protected String id;
    protected List<String> interceptor;
    protected List<Service.Message> message;

    public String getId() { return id; }
    
    public void setId(String value) { this.id = value; }
    
    public List<String> getInterceptor() {
        if (interceptor == null) {
            interceptor = new ArrayList<String>();
        }
        return interceptor;
    }
    
    public List<Service.Message> getMessage() {
        if (message == null) {
            message = new ArrayList<Service.Message>();
        }
        return message;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="", propOrder={"name", "serviceId", "interceptor", "role"})
    public static class Message {
        
        @XmlElement(required=true)
        protected String name;
        protected String serviceId;
        protected List<String> interceptor;
        protected List<String> role;

        public String getName() { return name; }
        public void setName(String value) { name = value; }
        public String getServiceId() { return serviceId; }
        public void setServiceId(String value) { serviceId = value; }

        public List<String> getInterceptor() {
            if (interceptor == null) {
                interceptor = new ArrayList<String>();
            }
            return interceptor;
        }

        public List<String> getRole() {
            if (role == null) {
                role = new ArrayList<String>();
            }
            return role;
        }
    }
}
