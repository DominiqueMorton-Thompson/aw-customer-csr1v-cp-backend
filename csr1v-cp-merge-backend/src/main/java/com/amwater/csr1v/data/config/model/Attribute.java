package com.amwater.csr1v.data.config.model;

public class Attribute {
private String name;
private String type;
private String mappingattribute;
private String resolver;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getResolver() {
	return resolver;
}
public String getMappingattribute() {
	return mappingattribute;
}
public void setMappingattribute(String mappingattribute) {
	this.mappingattribute = mappingattribute;
}
public void setResolver(String resolver) {
	this.resolver = resolver;
}
}
