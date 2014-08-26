package io.coala.capability.plan;

import io.coala.time.ClockID;

/**
 * {@link ClockStatusUpdate}
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:suki@almende.org">suki</a>
 *
 */
public interface ClockStatusUpdate
{

	/**
	 * @return
	 */
	ClockID getClockID();

	/**
	 * @return
	 */
	ClockStatus getStatus();

}