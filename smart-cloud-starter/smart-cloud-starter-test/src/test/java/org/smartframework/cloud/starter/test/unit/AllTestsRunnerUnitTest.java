package org.smartframework.cloud.starter.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runners.model.InitializationError;
import org.smartframework.cloud.starter.test.runner.AllTestsRunner;

import junit.framework.TestCase;

public class AllTestsRunnerUnitTest extends TestCase {

	public void test() throws InitializationError {
		AllTestsRunner allTestsRunner = new AllTestsRunner(AllTestsRunnerUnitTest.class, new AllDefaultPossibilitiesBuilder(true));
		Assertions.assertThat(allTestsRunner.testCount()).isGreaterThanOrEqualTo(1);
	}
	
}