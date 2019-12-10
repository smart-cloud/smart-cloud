package org.smartframework.cloud.starter.apidoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavadocReader {

	private static RootDoc root = null;

	public static class Doclet {
		public static boolean start(RootDoc root) {
			JavadocReader.root = root;
			return true;
		}
	}

	public static void show() {
		ClassDoc[] classes = root.classes();
		for (int i = 0; i < classes.length; ++i) {
			log.info("{}=>{}", classes[i], classes[i].commentText());
			for (MethodDoc method : classes[i].methods()) {
				log.info("{}", method.commentText());
			}
		}
	}

	public static void main(String[] args) {
		com.sun.tools.javadoc.Main
				.execute(new String[] { "-doclet", Doclet.class.getName(), "-encoding", "utf-8", "xxx.java" });
		show();

	}

}