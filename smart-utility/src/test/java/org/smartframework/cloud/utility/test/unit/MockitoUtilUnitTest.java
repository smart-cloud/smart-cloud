package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.smartframework.cloud.utility.MockitoUtil;
import org.smartframework.cloud.utility.MockitoUtil.MockTypeEnum;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MockitoUtilUnitTest extends TestCase {

	public void test() {
		Service service = new Service();
		Service mockService = Mockito.mock(Service.class);
		Controller controller = new Controller(service);

		AnnotationConfigApplicationContext context = new  AnnotationConfigApplicationContext();
		context.refresh();
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		beanFactory.registerSingleton("service", service);
		beanFactory.registerSingleton("controller", controller);
		
		MockitoUtil.setMockAttribute(controller, mockService, MockTypeEnum.MOCK_BORROW);
		MockitoUtil.revertMockAttribute(context);
		
		Assertions.assertThat(controller.getService()).isEqualTo(service);
	}
	
	@AllArgsConstructor
	@Getter
	public class Controller {
		private Service service;
	}
	
	public class Service {
		
	}
	
}