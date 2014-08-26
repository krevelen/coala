package io.coala.experimental.dynabean;

/**
 * {@link DynamicMessage}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public abstract class DynamicMessage extends DynamicBean
{
    public abstract void checkSelfValidity() throws Exception;

}
