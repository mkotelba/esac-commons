package com.esacinc.commons.test.impl;

import com.esacinc.commons.test.context.EsacApplicationTest;
import com.esacinc.commons.test.context.impl.EsacCommonsApplicationConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@EsacApplicationTest(classes = { EsacCommonsApplicationConfiguration.class })
@Test(groups = { "esac-commons.test" })
public abstract class AbstractEsacCommonsTests extends AbstractTestNGSpringContextTests {
}
