
package io.coala.experimental.dynabean;


import java.io.Serializable;


public interface DynamicBeanField extends Serializable//, IsSerializable
{
    public String getFieldName();

    boolean equals( DynamicBeanField pComparedObject );

    public abstract static class Comparator
    {
        public static boolean equals( DynamicBeanField pFirstObject, DynamicBeanField pSecondObject )
        {
            if ( pFirstObject == null && pSecondObject == null ) return true;
            if ( pFirstObject == null ) return false;
            if ( pSecondObject == null ) return false;
            
            if ( pFirstObject.getFieldName() == null && pSecondObject.getFieldName() == null ) return true;
            if ( pFirstObject.getFieldName() == null ) return false;
            if ( pSecondObject.getFieldName() == null ) return false;

            return pFirstObject.getFieldName().equals( pSecondObject.getFieldName() );
        }
    }
    
    public abstract static class Base implements DynamicBeanField
    {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public boolean equals( DynamicBeanField pComparedObject )
        {
            return DynamicBeanField.Comparator.equals( this, pComparedObject );
        }
        
    }
}
