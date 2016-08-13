package cn.aliate.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"config"})
@XmlRootElement(name="globalConfig")
public class GlobalConfig {

    protected List<GlobalConfig.Config> config;

    public List<GlobalConfig.Config> getConfig() {
        if (config == null) {
            config = new ArrayList<GlobalConfig.Config>();
        }
        return config;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name="", propOrder={
        "name",
        "category",
        "description",
        "type",
        "validatorRegularExpression",
        "defaultValue",
        "value",
        "hidden"
    })
    public static class Config {
        
        @XmlElement(required=true)
        protected String name;
        @XmlElement(required=true)
        protected String category;
        @XmlElement(required=true)
        protected String description;
        @XmlElement(required=true)
        protected String type;
        @XmlElement(required=true)
        protected String validatorRegularExpression;
        @XmlElement(required=true)
        protected String defaultValue;
        @XmlElement(required=true)
        protected String value;
        protected boolean hidden;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getValidatorRegularExpression() { return validatorRegularExpression; }
        public void setValidatorRegularExpression(String value) { validatorRegularExpression = value; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public boolean isHidden() { return hidden; }
        public void setHidden(boolean value) { this.hidden = value; }
    }
}
