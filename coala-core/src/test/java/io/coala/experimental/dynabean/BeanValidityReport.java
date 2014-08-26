package io.coala.experimental.dynabean;

public class BeanValidityReport
{
    protected String mInvalidReason = null;
    
    protected BeanValidityReport()
    {
    }
    
    public static BeanValidityReport reportAsValid()
    {
        BeanValidityReport tReport = new BeanValidityReport();
        
        return tReport;
    }
    
    public static BeanValidityReport reportAsInvalid(String pInvalidityReason)
    {
        BeanValidityReport tReport = new BeanValidityReport();
        tReport.setInvalidityReason( pInvalidityReason );
        
        return tReport;
    }
    
    protected void setInvalidityReason( String pInvalidityReason )
    {
        mInvalidReason = pInvalidityReason;
    }
    
    public String getInvalidityReason()
    {
        return mInvalidReason;
    }
    
    public boolean isValid()
    {
        return mInvalidReason == null;
    }
}
