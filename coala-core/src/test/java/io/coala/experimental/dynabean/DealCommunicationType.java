
package io.coala.experimental.dynabean;


/**
 * Identifies the communication type. E.g. for messages: SMS, PUSH, Email, Gtalk
 * etc
 * 
 * @author Shravan 28-1-2013
 */
public enum DealCommunicationType implements DynamicBeanField
{
    sms( "text sms" ), push( "text push" ), gtalk( "gtalk" ), email( "email" ),

    invalid( "invalid notification type" ),

    phonecall( "phonecall" ),
    
    fiqscall ( "fiqscall" );

    protected String mFieldName;

    private DealCommunicationType( DealCommunicationType pCopiedField )
    {
        mFieldName = pCopiedField.mFieldName;
    }

    private DealCommunicationType( String pFieldName )
    {
        mFieldName = pFieldName;
    }

    @Override
    public String getFieldName()
    {
        return mFieldName;
    }

    @Override
    public String toString()
    {
        return getFieldName();
    }

    @Override
    public boolean equals( DynamicBeanField pComparedObject )
    {
        return DynamicBeanField.Comparator.equals( this, pComparedObject );
    }

    /**
     * returns the DealCommunicationType based on the supplied string. returns
     * the fallback if no matching DealCommunicationType is found
     * 
     * @param pDealCommunicationTypetext
     * @return
     */
    public static DealCommunicationType tryValueOf(
        String pDealCommunicationTypetext, DealCommunicationType pFallBackType )
    {
        try
        {
            return DealCommunicationType.valueOf( pDealCommunicationTypetext );
        }
        catch ( Exception e )
        {
            return pFallBackType;
        }
    }
}
