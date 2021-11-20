package org.smartframework.cloud.starter.log4j2.system;

/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.jar.JarFile;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Provides access to the application home directory. Attempts to pick a sensible home for
 * both Jar Files, Exploded Archives and directly running applications.
 *
 * @author Phillip Webb
 * @author Raja Kolli
 * @since 2.0.0
 */
public class ApplicationHome {

	private final File source;

	private final File dir;

	/**
	 * Create a new {@link ApplicationHome} instance for the specified source class.
	 * @param sourceClass the source class or {@code null}
	 */
	public ApplicationHome(Class<?> sourceClass) {
		Assert.isTrue(sourceClass!=null, "sourceClass can not be null!");
		
		this.source = findSource(sourceClass);
		this.dir = findHomeDir(this.source);
	}

	private File findSource(Class<?> sourceClass) {
		try {
			ProtectionDomain domain = (sourceClass != null)
					? sourceClass.getProtectionDomain() : null;
			CodeSource codeSource = (domain != null) ? domain.getCodeSource() : null;
			URL location = (codeSource != null) ? codeSource.getLocation() : null;
			File source = (location != null) ? findSource(location) : null;
			if (source != null && source.exists() && !isUnitTest()) {
				return source.getAbsoluteFile();
			}
			return null;
		}
		catch (Exception ex) {
			return null;
		}
	}

	private boolean isUnitTest() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (int i = stackTrace.length - 1; i >= 0; i--) {
				if (stackTrace[i].getClassName().startsWith("org.junit.")) {
					return true;
				}
			}
		}
		catch (Exception ex) {
		}
		return false;
	}

	private File findSource(URL location) throws IOException {
		URLConnection connection = location.openConnection();
		if (connection instanceof JarURLConnection) {
			return getRootJarFile(((JarURLConnection) connection).getJarFile());
		}
		return new File(location.getPath());
	}

	private File getRootJarFile(JarFile jarFile) {
		String name = jarFile.getName();
		int separator = name.indexOf("!/");
		if (separator > 0) {
			name = name.substring(0, separator);
		}
		return new File(name);
	}

	private File findHomeDir(File source) {
		File homeDir = source;
		homeDir = (homeDir != null) ? homeDir : findDefaultHomeDir();
		if (homeDir.isFile()) {
			homeDir = homeDir.getParentFile();
		}
		homeDir = homeDir.exists() ? homeDir : new File(".");
		return homeDir.getAbsoluteFile();
	}

	private File findDefaultHomeDir() {
		String userDir = System.getProperty("user.dir");
		return new File(StringUtils.hasLength(userDir) ? userDir : ".");
	}

	/**
	 * Returns the application home directory.
	 * @return the home directory (never {@code null})
	 */
	public File getDir() {
		return this.dir;
	}

}