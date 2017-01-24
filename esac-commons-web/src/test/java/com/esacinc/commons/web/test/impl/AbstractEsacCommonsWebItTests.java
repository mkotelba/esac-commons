package com.esacinc.commons.web.test.impl;

import com.esacinc.commons.test.context.EsacApplicationTest;
import com.esacinc.commons.test.context.impl.EsacCommonsApplicationConfiguration;
import com.esacinc.commons.test.impl.AbstractEsacCommonsItTests;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testng.annotations.Test;

@EsacApplicationTest(classes = { EsacCommonsApplicationConfiguration.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
@Test(groups = { "esac-commons.test.it.web" })
public abstract class AbstractEsacCommonsWebItTests extends AbstractEsacCommonsItTests {
}
