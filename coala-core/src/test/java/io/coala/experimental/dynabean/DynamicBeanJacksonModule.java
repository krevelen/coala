package io.coala.experimental.dynabean;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Deprecated // no need to use this module anymore, dynamic bean directly refers to the deserializer class through annotation
public class DynamicBeanJacksonModule extends SimpleModule
{

    /** */
	private static final long serialVersionUID = 1L;

	public DynamicBeanJacksonModule()
    {
        super( "Dynamic Bean Deserializer Module", new Version( 1, 0, 0, null ) );
        addDeserializer( DynamicBean.class, new DynamicBeanJacksonDeserializer() );
    }

}
