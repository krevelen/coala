package io.coala.capability.configure;

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.config.CoalaPropertyGetter;
import io.coala.config.PropertyGetter;
import io.coala.log.InjectLogger;
import io.coala.message.Message;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link BasicConfiguringCapability}
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * @author <a href="mailto:Suki@almende.org">Suki</a>
 */
public class BasicConfiguringCapability extends BasicCapability implements
		ConfiguringCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link AGlobeMessengerService} constructor
	 * 
	 * @param binder
	 */
	@Inject
	private <T extends Message<?>> BasicConfiguringCapability(final Binder binder)
	{
		super(binder);
	}

	/** @see BasicCapability#getProperty(String, String[]) */
	@Override
	public PropertyGetter getProperty(final String key,
			final String... prefixes)
	{
		return new CoalaPropertyGetter(CoalaPropertyGetter.addKeyPrefixes(key,
				prefixes));
	}
}