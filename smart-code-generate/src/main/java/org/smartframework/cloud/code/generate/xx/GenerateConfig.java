package org.smartframework.cloud.code.generate.xx;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GenerateConfig {

	private String author;
	private int type;
	private String mainClassPackage;
	private ProjectConfig project;
	
}