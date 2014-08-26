package io.coala.experimental.dynabean;

public interface CustomSerializable
{
    public boolean hasCustomDeserializer();
    
    public Object useCustomDeserializer( String pSerializedString );
    
    public String toCustomSerializedString();
}
