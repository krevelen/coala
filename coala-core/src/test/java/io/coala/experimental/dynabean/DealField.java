
package io.coala.experimental.dynabean;


import java.io.Serializable;


public enum DealField implements DynamicBeanField, Serializable
//, IsSerializable, DealEntityField
{
    USER( "user" ),

    TRUCK_KEY( "truckKey" ),
    TRUCK_NAME( "truckName" ),

    ACTIVITY_KEY( "activityKey" ),

    ORDER_KEY( "orderKey" ),
    ORDER_NAME( "orderName" ),
    ORDER_SIZE( "orderSize" ),
    ORDER_PICKUP_START_TIME( "orderPickupStartTime" ),
    ORDER_PICKUP_END_TIME( "orderPickupEndTime" ),
    ORDER_DELIVERY_START_TIME( "orderDeliveryStartTime" ),
    ORDER_DELIVERY_END_TIME( "orderDeliveryEndTime" ),
    ORDER_NOTIFICATION_TIME( "orderNotificationTime" ),

    TRIP_ORDER_KEY( "orderKey" ),
    TRIP_TRUCK_KEY( "truckKey" ),
    TRIP_TYPE( "tripType" ),
    NB_TRIP( "nbTrip" ),

    BAD_ORDER_KEY( "badOrderKey" ),
    BAD_ORDER_NAME( "badOrderName" ),

    TRIP_KEY( "tripKey" ),

    DATE( "date" ),

    // constraint series
    HAS_UNLOAD_CONSTRAINT( "unloadConstraint" ),
    UNLOAD_TIME( "unloadTime" ),

    NUMBER_OF_ORDERS( "numOfOrders" ),
    NUMBER_OF_TRUCKS( "numOfTrucks" ),
    BAD_TSP_TRUCK_KEYS( "badTSPTruckKeys" ),
    BAD_ROUTING_TRUCK_KEYS( "badRoutingTruckKeys" ),
    UNKNOWN_ERROR_TRUCK_KEYS( "unknownErrorTruckKeys" ),

    INITIAL_LOCATION_LONGITUDE( "initialLocationLongitude" ),
    INITIAL_LOCATION_LATITUDE( "initialLocationLatitude" ),
    INITIAL_ADDRESS_STRING( "initialAddressString" ),

    END_LOCATION_LONGITUDE( "endLocationLongitude" ),
    END_LOCATION_LATITUDE( "endLocationLatitude" ),
    END_ADDRESS_STRING( "endAddressString" ),

    MAP_RESPONSE_STATUS( "mapResponseStatus" ),

    // TASK_STATUS( "status" ),
    UNKNOWN_ERROR_MESSAGE( "unknownErrorMessage" ),
    ORDER_QUERY_PARAMETER( "orderQueryParameter" ),
    TRUCK_QUERY_PARAMETER( "truckQueryParameter" ),
    ACTIVE_TIME( "activeTime" ),
    MAXIMUM_DISTANCE( "maxDistance" ),
    FIELDS( "fields" ),

    SORT_VALUE( "sortValue" ),
    QUERY_PARAMETER( "queryParameter" ), // by Habib

    ENTITY_TYPE( "entityType" ),
    ENTITY_NAME( "entityName" ),
    ENTITY_KEY( "entityKey" ),
    WORKER_TASK_SIZE( "workerTaskSize" ),
    TOTAL_SIZE( "totalSize" ),
    QUERY_START( "queryStart" ),
    QUERY_SIZE( "querySize" ),
    POSITION( "position" ),

    INDEX( "index" ),

    IS_TRUCK( "isTruck" ),
    IS_ORDER( "isOrder" ),
    SHOULD_GARBAGE_COLLECT( "isExecuteGC" ),
    SHOULD_CLEAN_UP( "shouldCleanUp" ),
    TRUCK_COST( "truckCost" ),
    TRUCK_CAPACITY( "truckCapacity" ),
    TRUCK_START_TIME( "truckStartTime" ),
    TRUCK_END_TIME( "truckEndTime" ),
    ERROR_TYPE( "error-type" ),
    ERROR_MESSAGE( "message" ),
    IS_EXCEPTION_RUNTIME( "isExceptionRuntime" ),
    EXCEPTION_OBJECT( "exceptionObject" ),
    EXECUTION_CASE( "executionCase" ),
    LANGUAGE_FORMAT( "languageFormat" ),
    INSERTION_POINT( "insertionPoint" ),
    ATTACHED_OBJECT( "attachedObject" ),
    EMAIL( "email" ),
    AGENT_NAME( "agentName" ),
    TASK_STRING( "taskString" ),
    OBJECT_TYPE( "objectType" ),
    OBJECT_DATA( "objectData" ),
    IS_DYNAMIC_BEAN( "isDynamicBean" ),
    RAW_OBJECT_TYPE( "rawObjectType" ),
    CALLBACK_KEY( "callback-key" ),

    KEY( "key" ),
    TIMESTAMP( "timeStamp" ),
    TRUCKSCHEDULE_TIMESTAMP( "timestamp" ),
    ID( "id" ),
    GROUPID( "groupId" ),
    ICONTYPE( "iconType" ),
    PICKUPID( "pickupId" ),
    RECEIVERID( "receiverId" ),
    SENDERID( "senderId" ),
    SHARED( "shared" ),
    SHAREDGROUP( "sharedGroup" ),
    SHARESTATUS( "shareStatus" ),
    SIZE( "size" ),
    STATUS( "status" ),
    STATUS_UPDATE( "statusupdate"),
    LOCKSTATUS( "lockStatus" ),
    LATESTATUS( "lateStatus" ),
    CARRIERID( "carrierId" ),
    CARRIERUUID( "carrierUUID" ),
    MANIPULATORID( "manipulatorId" ),
    READERID( "readerId" ),
    OWNER( "owner" ),
    EXECUTOR( "executor" ),
    INITIALLOADINGTIME( "initialLoadingTime" ),
    ADDITIONALLOADINGTIME( "additionalLoadingTime" ),
    INITIALUNLOADINGTIME( "initialUnloadingTime" ),
    ADDITIONALUNLOADINGTIME( "additionalUnloadingTime" ),
    DESCRIPTION( "description" ),
    VISIBLEDAYS( "visibleDays" ),
    EXTRAS( "extras" ),
    
    PRE_TRUCK_UUID("preTruckUuid"),
    POST_TRUCK_UUID("postTruckUuid"),
    
    ORIGINATOR_ID( "originatorId" ),
    FORWARD_OPTION( "forwardOption" ),
    EVENT_TYPE( "eventType" ),
    ORIGINAL_EVENT_TYPE( "originalEventType" ),
    SOURCE( "source" ),
    DESTINATION( "destination" ),
    MESSAGE_CONTEXT( "messageContext" ),
    ORDER( "order" ),
    TRUCK( "truck" ),
    TRIP( "trip" ),
    IS_LAZY_ENTITY( "isLazyEntity" ),
    IS_UUID_CHANGED( "isUuidChanged" ),
    PRECEDING_INDEX( "preceding-index" ),
    SUCCEEDING_INDEX( "succeeding-index" ),
    TIME_WINDOW( "time-window" ),
    PERSPECTIVE( "perspective" ),
    NAME( "name" ),
    SUBSCRIPTION( "subscription" ),
    QUOTAS( "quotas" ),
    URL( "url" ),
    CARDINALITY( "cardinality" ),
    TARGET( "target" ),
    DURATION( "duration" ),
    HTML( "html" ),
    TNT_FIQS_JOB_ID( "job-id" ),
    jobId( "jobId" ),
    TNT_FIQS_EXTRAS_JOB_ID( "jobId" ),
    TNT_FIQS_DESCRIPTION("additional-information"),
    TNT_FIQS_PICKUP_START( "pickup-start" ),
    TNT_FIQS_PICKUP_END( "pickup-end" ),
    TNT_FIQS_PICKUP_ETA( "pickup-eta" ),
    TNT_FIQS_DELIVERY_START( "delivery-start" ),
    TNT_FIQS_DELIVERY_END( "delivery-end" ),
    TNT_FIQS_DELIVERY_ETA( "delivery-eta" ),
    TNT_FIQS_CUSTOMER_NAME( "customer-name" ),
    TNT_FIQS_CUSTOMER_CONTACT( "customer-contact" ),
    TNT_FIQS_CUSTOMER_PHONE( "customer-phone" ),
    TNT_FIQS_CUSTOMER_EMAIL( "customer-email" ),
    TNT_FIQS_PICKUP_COUNTRY( "pickup-country" ),
    TNT_FIQS_PICKUP_CITY( "pickup-city" ),
    TNT_FIQS_PICKUP_STREET( "pickup-street" ),
    TNT_FIQS_PICKUP_NUMBER( "pickup-number" ),
    TNT_FIQS_PICKUP_POSTCODE( "pickup-postcode" ),
    TNT_FIQS_PICKUP_CONTACT( "pickup-contact" ),
    TNT_FIQS_PICKUP_PHONE( "pickup-phone" ),
    TNT_FIQS_PICKUP_EMAIL( "pickup-email" ),
    TNT_FIQS_SEND_PICKUP_PREALERT( "send-pickup-prealert" ),
    notificationsSendPickupPrealert( "notificationsSendPickupPrealert" ),
    TNT_FIQS_PICKUP_ALERT_MINUTES( "pickup-alert-minutes" ),
    notificationsSendDeliveryPrealert( "notificationsSendDeliveryPrealert" ),
    notificationsPickupAlertMinutes( "notificationsPickupAlertMinutes" ),
    notificationsDeliveryAlertMinutes( "notificationsDeliveryAlertMinutes" ),
    TNT_FIQS_DELIVERY_COUNTRY( "delivery-country" ),
    TNT_FIQS_DELIVERY_CITY( "delivery-city" ),
    TNT_FIQS_DELIVERY_STREET( "delivery-street" ),
    TNT_FIQS_DELIVERY_NUMBER( "delivery-number" ),
    TNT_FIQS_DELIVERY_POSTCODE( "delivery-postcode" ),
    TNT_FIQS_DELIVERY_CONTACT( "delivery-contact" ),
    TNT_FIQS_DELIVERY_PHONE( "delivery-phone" ),
    TNT_FIQS_DELIVERY_EMAIL( "delivery-email" ),
    TNT_FIQS_SEND_DELIVERY_PREALERT( "send-delivery-prealert" ),
    TNT_FIQS_DELIVERY_ALERT_MINUTES( "delivery-alert-minutes" ),
    TNT_FIQS_POD_PHOTO_REQUIRED( "pod-photo-required" ),
    TNT_FIQS_ORDER_STATUS( "order-status" ),
    TNT_FIQS_LATE_STATUS( "late-status" ),
    TNT_FIQS_AGENT_NAME( "agent-name" ),
    TNT_FIQS_GPS_STATUS( "gps-status" ),
    TNT_FIQS_DRIVER_NAME( "driver-name" ),
    TNT_FIQS_DRIVER_PHONE( "driver-phone" ),
    TNT_FIQS_DRIVER_EMAIL( "driver-email" ),
    FALLBACK_EVENT_TYPE( "fallbackEventType" ),
    START_TIMESTAMP( "startTimestamp" ),
    END_TIMESTAMP( "endTimestamp" ),
    HOST( "host" ),
    PATH( "path" ),
    LOCATION( "location" ),
    IP_ADDRESS( "ip-address" ),

    SENSE_NOTIFICATION_SENSOR_ID( "sensor_id" ),
    SENSE_NOTIFICATION_TRIGGER_ID( "trigger_id" ),
    SENSE_NOTIFICATION_SENSOR_DATA_DATE( "sensor_data_date" ),
    SENSE_NOTIFICATION_TEXT( "text" ),
    SENSE_NOTIFICATION_SUBJECT_ID( "subject_id" ),

    NAMESPACE( "namespace" ),
    ROUTE( "route" ),

    GOOGLE_MAPS_DIRECTION_STEP_START_LOCATION_LATITUDE( "start_location/lat" ),
    GOOGLE_MAPS_DIRECTION_STEP_START_LOCATION_LONGITUDE( "start_location/lng" ),

    GOOGLE_MAPS_DIRECTION_STEP_END_LOCATION_LATITUDE( "end_location/lat" ),
    GOOGLE_MAPS_DIRECTION_STEP_END_LOCATION_LONGITUDE( "end_location/lng" ),
    GOOGLE_MAPS_DIRECTION_STEP_DURATION( "duration/value" ),
    GOOGLE_MAPS_DIRECTION_STEP_POLYLINE( "polyline/points" ),
    GOOGLE_MAPS_DIRECTION_ROUTE_LEGS( "routes/legs" ),
    GOOGLE_MAPS_DIRECTION_ROUTE_POINTS( "routes/overview_polyline/points" ),
    GOOGLE_MAPS_DIRECTION_STATUS( "status" ),

    GOOGLE_MAPS_GEOCODING_RESULTS( "results" ),
    GOOGLE_MAPS_GEOCODING_RESULTS_GEOMETRY( "results/geometry" ),
    GOOGLE_MAPS_GEOCODING_STATUS( "status" ),
    GOOGLE_MAPS_GEOCODING_GEOMETRY_LOCATION_LATITUDE( "location/lat" ),
    GOOGLE_MAPS_GEOCODING_GEOMETRY_LOCATION_LONGITUDE( "location/lng" ),
    GOOGLE_MAPS_GEOCODING_GEOMETRY_LOCATION_TYPE( "location_type" ),

    IVO_CSV_RESPONSE_TARGET_CHANGE_SET( "result/targetChangeset" ),
    IVO_CSV_RESPONSE_TARGET_CHANGES( "result/targetChangeset/changes" ),
    IVO_CSV_RESPONSE_SOURCE_CHANGES( "result/sourceChangeset/changes" ),
    IVO_CSV_RESPONSE_IVO_JOB_ID( "result/ivoJobId" ),
    IVO_CSV_RESPONSE_TARGET_START_TIME( "result/targetChangeset/startTime" ),
    IVO_CSV_RESPONSE_TARGET_END_TIME( "result/targetChangeset/endTime" ),
    IVO_CSV_RESPONSE_TARGET_NUM_CREATES( "result/targetChangeset/nofCreates" ),
    IVO_CSV_RESPONSE_TARGET_NUM_UPDATES( "result/targetChangeset/nofUpdates" ),
    IVO_CSV_RESPONSE_TARGET_NUM_DELETES( "result/targetChangeset/nofDeletes" ),
    IVO_CSV_RESPONSE_TARGET_CHANGE_NEWDATAOBJECT_ID( "newDataObject/id" ),
    IVO_CSV_RESPONSE_TARGET_CHANGE_NEWDATAOBJECT_KEY( "newDataObject/key" ),

    SOURCE_CHANGE_INDEX( "sourceChangeIndex" ),
    LOG_LEVEL( "logLevel" ),
    TEXT( "text" ),
    TYPE( "type" ),
    NEW_DATA_OBJECT_ID( "newDataObject/id" ),
    NEW_DATA_OBJECT_KEY( "newDataObject/key" ),
    NEW_DATA_OBJECT_TYPE( "newDataObject/type" ),
    IS_COMMITTED( "isCommited" ),
    LOG( "log" ),

    ADDRESS( "address" ),
    WAYPOINT( "waypoint" ),
    WARNING_MESSAGE( "warning-message" ),
    SPEED( "speed" ),
    PASSWORD( "mxyzptlk" ),
    //STOP_POINT( "stop-point" ), 
    //STOP_DURATION( "stop-duration" ),
    WAYPOINT_START_TIME( "waypoint-start-time" ),
    SHOULD_UPDATE_DURATION( "should-update-duration" ),
    SHOULD_FIX_OTHER_ACTIVITIES( "should-fix-other-activities" ),
    SHOULD_UPDATE_TRUCK( "should-update-truck" ),
    LOWER_LIMIT( "lower-limit" ),
    UPPER_LIMIT( "upper-limit" ),
    COMPANY_NAME( "name" ),
    MESSAGE( "message" ),
    END_USER_MESSAGE( "end-user-message" ),
    IS_EXIST( "isExist" ),
    NUMBER( "number" ),
    CODE( "code" ),
    ROOT_TOKEN( "root-token" ),
    TOKEN_OWNER( "token-owner" ),
    TOKEN( "token" ),
    AGENT_ID( "agent-id" ),
    PARAMS( "params" ),
    METHOD( "method" ),
    LOCKTOKEN( "+locktoken" ),
    DRIVER_EMAIL( "uuid" ),

    SCHEDULE_TRUCK_KEY( TRUCK_KEY ),
    DEFAULT_AGENT_NAME( "default-agent-name" ),

    GOOGLE_MAPS_DISTANCE_MATRIX_STATUS( "status" ),
    GOOGLE_MAPS_DISTANCE_MATRIX_DESTINATION_ADDRESSES( "destination_addresses" ),
    GOOGLE_MAPS_DISTANCE_MATRIX_ORIGIN_ADDRESSES( "origin_addresses" ),
    GOOGLE_MAPS_DISTANCE_MATRIX_ELEMENTS( "rows/elements" ),
    COMPUTED_ABSOLUTE_START_TIME( "computedAbsoluteStartTime" ),
    COMPUTED_ABSOLUTE_END_TIME( "computedAbsoluteEndTime" ),

    PRE_UPDATED_ENTITY( "preUpdatedEntity" ),
    POST_UPDATED_ENTITY( "postUpdatedEntity" ),
    ORDER_KEYS( "orderKeys" ),
    EXPECTED_HANDLING_START_TIME( "expected-handling-start-time" ),
    EXPECTED_DELIVERY_END_TIME( "expected-delivery-end-time" ),
    IS_UNLOAD_ONLY( "is-unload-only" ),
    PICKUP_START_TIME( "pickup-start-time" ),
    DELIVERY_START_TIME( "delivery-start-time" ), ;

    private static final String cIndexSeparatorString = "_";

    protected String mFieldName;

    private DealField( DealField pCopiedField )
    {
        mFieldName = pCopiedField.mFieldName;
    }

    private DealField( String pFieldName )
    {
        mFieldName = pFieldName;
    }

    @Override
    public String getFieldName()
    {
        return mFieldName;
    }

    //@Override
    public String getName()
    {
        return getFieldName();
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

    public DynamicBeanField useIndices( int pFieldFirstIndex,
                                        int pFieldSecondIndex,
                                        int pFieldThirdIndex )
    {
        return withSuffix( cIndexSeparatorString + pFieldFirstIndex +
                           cIndexSeparatorString + pFieldSecondIndex +
                           cIndexSeparatorString + pFieldThirdIndex );
    }

    public DynamicBeanField useIndices( int pFieldFirstIndex,
                                        int pFieldSecondIndex )
    {
        return withSuffix( cIndexSeparatorString + pFieldFirstIndex +
                           cIndexSeparatorString + pFieldSecondIndex );
    }

    public DynamicBeanField useIndex( Integer pFieldIndex )
    {
        if ( pFieldIndex == null )
        {
            return this;
        }
        return withSuffix( cIndexSeparatorString + pFieldIndex.toString() );
    }

    public DynamicBeanField withSuffix( String pSuffixString )
    {
        if ( pSuffixString == null )
        {
            return this;
        }
        return useCustomField( getFieldName() + pSuffixString );
    }

    public DynamicBeanField withPrefix( String pPrefixString )
    {
        if ( pPrefixString == null )
        {
            return this;
        }
        return useCustomField( pPrefixString + getFieldName() );
    }

    public static DynamicBeanField useCustomField( final String pCustomFieldName )
    {
        @SuppressWarnings( "serial" )
        DynamicBeanField tReturnField = new Base()
        {
            @Override
            public String getFieldName()
            {
                return pCustomFieldName;
            }
        };
        return tReturnField;
    }
}
