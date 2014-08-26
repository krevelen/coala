package io.coala.capability;


/**
 * {@link CapabilityFactory}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:suki@almende.org">suki</a>
 * 
 */
public interface CapabilityFactory<T>
{
	/** @return the {@link Capability} object */
	T create();
}