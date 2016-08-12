package cn.aliate;

import java.util.*;

public class ApiMessageDescriptor {
    private String name;
    private String serviceId;
    private String configPath;
    private List<String> roles;
    private List<String> interceptors;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public List<String> getInterceptors() { return interceptors; }
    public void setInterceptors(List<String> interceptors) { this.interceptors = interceptors; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public String getConfigPath() { return configPath; }
    public void setConfigPath(String configPath) { this.configPath = configPath; }
}
