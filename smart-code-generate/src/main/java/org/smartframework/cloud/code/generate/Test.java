package org.smartframework.cloud.code.generate;

import java.io.FileReader;
import java.io.IOException;

import org.smartframework.cloud.code.generate.xx.YamlConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import com.alibaba.fastjson.JSON;

public class Test {

	public static void main(String[] args) throws IOException {
		String yamlUrl = "D:\\code\\github\\smart-cloud\\smart-code-generate\\src\\test\\resources\\config\\basic_user.yml";
		Representer representer = new Representer();
        // Set null for missing values in the yaml
        representer.getPropertyUtils().setSkipMissingProperties(true);
		Yaml yaml = new Yaml(representer);
		try (FileReader reader = new FileReader(yamlUrl)) {
			YamlConfig yamlConfig = yaml.loadAs(reader, YamlConfig.class);
			System.out.println(JSON.toJSONString(yamlConfig));
		} catch (IOException e) {
			// 抛异常，则取当前jar名
			e.printStackTrace();
		}
	}

}
