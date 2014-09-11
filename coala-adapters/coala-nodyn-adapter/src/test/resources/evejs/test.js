/**
 * Created by Alex on 7/23/14.
 */

var test = {RPCfunctions: {}};

test.init = function() {
  this.dummyData = {
    "ioTEntity": [
      {
        "name": "9",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:1",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "6",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:2",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "2",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "3",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:3",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "5",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "10",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:4",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "4",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:5",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "8",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:6",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "1",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000051000,
                "resultTime": 1404000054000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "unknown",
                "phenomenonTime": 1404000051000,
                "resultTime": 1404000054000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:7",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "3",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "36",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "unknown",
                "phenomenonTime": 1404000048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:8",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "7",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000044000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:9",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "CO2 Sensor Device",
        "meta": [],
        "ioTProperty": [
          {
            "name": "iotprop:00843454:CO2Sensor",
            "description": "CO2 Sensor Value.",
            "unitOfMeasurement": {
              "value": "ppm",
              "typeof": []
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "469",
                "phenomenonTime": 1401141600000,
                "resultTime": 1401141600000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertiaontologies:http://ns.inertia.eu/ontologies",
              "xs:XMLSchema"
            ],
            "about": "iotprop:00843454:CO2Sensor",
            "typeof": [
              "inertiaontologies:CO2Level"
            ],
            "datatype": "xs:int"
          },
          {
            "name": "iotprop:00843454:BatteryCapacity",
            "description": "Capacity of the sensor battery.",
            "unitOfMeasurement": {
              "value": "kWh",
              "typeof": []
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "1789",
                "phenomenonTime": 1401283297000,
                "resultTime": 1401283297000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertiaontologies:http://ns.inertia.eu/ontologies",
              "xs:XMLSchema"
            ],
            "about": "iotprop:00843454:BatteryCapacity",
            "typeof": [
              "inertiaontologies:BatteryCapacity"
            ],
            "datatype": "xs:int"
          }
        ],
        "any": [],
        "prefix": [
          "inertiaontologies:http://ns.inertia.eu/ontologies",
          "xs:XMLSchema"
        ],
        "about": "77EC6B0-F039-4734-925E-0A90CE7D1B5B:00843454:CO2",
        "typeof": [
          "inertiaontologies:CO2Sensor",
          "inertiaontologies:EnvironmentSensor"
        ]
      },
      {
        "name": "BrightnessDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "0005E8FA:Brigthness",
            "unitOfMeasurement": {
              "value": "lux",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "4",
                "phenomenonTime": 1402970309596,
                "resultTime": 1402970310047,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "0005E8FA:Brigthness",
            "typeof": [
              "inertia:Brightness"
            ],
            "datatype": "xs:double"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:0005E8FA:Brightness",
        "typeof": [
          "inertia:BrightnessSensor"
        ]
      },
      {
        "name": "MotionSensorDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "0005E8FA:MotionSensor",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "Yes",
                "phenomenonTime": 1402985144369,
                "resultTime": 1402985144750,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "0005E8FA:MotionSensor",
            "typeof": [
              "inertia:MotionStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:0005E8FA:Motion",
        "typeof": [
          "inertia:MotionSensor"
        ]
      },
      {
        "name": "CO2SensorDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "00843454:CO2Sensor",
            "unitOfMeasurement": {
              "value": "ppm",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "460",
                "phenomenonTime": 1402964033581,
                "resultTime": 1402964033991,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "00843454:CO2Sensor",
            "typeof": [
              "inertia:CO2Level"
            ],
            "datatype": "xs:int"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:00843454:CO2",
        "typeof": [
          "inertia:CO2Sensor"
        ]
      },
      {
        "name": "ThermometerDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "00843454:Thermometer",
            "unitOfMeasurement": {
              "value": "C",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "22",
                "phenomenonTime": 1402964034467,
                "resultTime": 1402964034869,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "00843454:Thermometer",
            "typeof": [
              "inertia:Temperature"
            ],
            "datatype": "xs:double"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:00843454:Thermometer",
        "typeof": [
          "inertia:Thermometer"
        ]
      },
      {
        "name": "DoorDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "0181FDED",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "OPEN",
                "phenomenonTime": 1402985140428,
                "resultTime": 1402985140820,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "0181FDED",
            "typeof": [
              "inertia:ContactStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:0181FDED",
        "typeof": [
          "inertia:ContactSensor"
        ]
      },
      {
        "name": "AirQualityDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "99990001",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "Moderate",
                "phenomenonTime": 1402993034207,
                "resultTime": 1402993034208,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "99990001",
            "typeof": [
              "inertia:AirQualityStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:99990001",
        "typeof": [
          "inertia:AirQualitySensor"
        ]
      },



      {
        "name": "9",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:1",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "6",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:2",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "2",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "3",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404008048000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:3",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "5",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "10",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:4",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "4",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:5",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "8",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:6",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "1",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000851000,
                "resultTime": 1404000054000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "unknown",
                "phenomenonTime": 1404000851000,
                "resultTime": 1404000054000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:7",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "3",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "36",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "unknown",
                "phenomenonTime": 1404000848000,
                "resultTime": 1404000051000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:8",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "7",
        "meta": [],
        "ioTProperty": [
          {
            "name": "PowerConsumption",
            "unitOfMeasurement": {
              "value": "W",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "0",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "PowerConsumption",
            "typeof": [
              "inertia:PowerConsumption"
            ],
            "datatype": "xs:double"
          },
          {
            "name": "DeviceStatus",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "on",
                "phenomenonTime": 1404000844000,
                "resultTime": 1404000047000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "DeviceStatus",
            "typeof": [
              "inertia:DeviceStatus",
              "DeviceStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "0C486748-CF2A-450C-BCF6-02AC1CB39A2D:9",
        "typeof": [
          "inertia:PowerSwitch"
        ]
      },
      {
        "name": "CO2 Sensor Device",
        "meta": [],
        "ioTProperty": [
          {
            "name": "iotprop:00843454:CO2Sensor",
            "description": "CO2 Sensor Value.",
            "unitOfMeasurement": {
              "value": "ppm",
              "typeof": []
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "469",
                "phenomenonTime": 1401142600000,
                "resultTime": 1401141600000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertiaontologies:http://ns.inertia.eu/ontologies",
              "xs:XMLSchema"
            ],
            "about": "iotprop:00843454:CO2Sensor",
            "typeof": [
              "inertiaontologies:CO2Level"
            ],
            "datatype": "xs:int"
          },
          {
            "name": "iotprop:00843454:BatteryCapacity",
            "description": "Capacity of the sensor battery.",
            "unitOfMeasurement": {
              "value": "kWh",
              "typeof": []
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "1789",
                "phenomenonTime": 1401284297000,
                "resultTime": 1401283297000,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertiaontologies:http://ns.inertia.eu/ontologies",
              "xs:XMLSchema"
            ],
            "about": "iotprop:00843454:BatteryCapacity",
            "typeof": [
              "inertiaontologies:BatteryCapacity"
            ],
            "datatype": "xs:int"
          }
        ],
        "any": [],
        "prefix": [
          "inertiaontologies:http://ns.inertia.eu/ontologies",
          "xs:XMLSchema"
        ],
        "about": "77EC6B0-F039-4734-925E-0A90CE7D1B5B:00843454:CO2",
        "typeof": [
          "inertiaontologies:CO2Sensor",
          "inertiaontologies:EnvironmentSensor"
        ]
      },
      {
        "name": "BrightnessDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "0005E8FA:Brigthness",
            "unitOfMeasurement": {
              "value": "lux",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "4",
                "phenomenonTime": 1402975309596,
                "resultTime": 1402970310047,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "0005E8FA:Brigthness",
            "typeof": [
              "inertia:Brightness"
            ],
            "datatype": "xs:double"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:0005E8FA:Brightness",
        "typeof": [
          "inertia:BrightnessSensor"
        ]
      },
      {
        "name": "MotionSensorDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "0005E8FA:MotionSensor",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "Yes",
                "phenomenonTime": 1402988144369,
                "resultTime": 1402985144750,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "0005E8FA:MotionSensor",
            "typeof": [
              "inertia:MotionStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:0005E8FA:Motion",
        "typeof": [
          "inertia:MotionSensor"
        ]
      },
      {
        "name": "CO2SensorDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "00843454:CO2Sensor",
            "unitOfMeasurement": {
              "value": "ppm",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "460",
                "phenomenonTime": 1402969033581,
                "resultTime": 1402964033991,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "00843454:CO2Sensor",
            "typeof": [
              "inertia:CO2Level"
            ],
            "datatype": "xs:int"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:00843454:CO2",
        "typeof": [
          "inertia:CO2Sensor"
        ]
      },
      {
        "name": "ThermometerDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "00843454:Thermometer",
            "unitOfMeasurement": {
              "value": "C",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "22",
                "phenomenonTime": 1402967034467,
                "resultTime": 1402964034869,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "00843454:Thermometer",
            "typeof": [
              "inertia:Temperature"
            ],
            "datatype": "xs:double"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:00843454:Thermometer",
        "typeof": [
          "inertia:Thermometer"
        ]
      },
      {
        "name": "DoorDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "0181FDED",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "OPEN",
                "phenomenonTime": 1402985240428,
                "resultTime": 1402985140820,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "0181FDED",
            "typeof": [
              "inertia:ContactStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:0181FDED",
        "typeof": [
          "inertia:ContactSensor"
        ]
      },
      {
        "name": "AirQualityDevice",
        "meta": [],
        "ioTProperty": [
          {
            "name": "99990001",
            "unitOfMeasurement": {
              "value": "",
              "typeof": [
                "inertia:UoM"
              ]
            },
            "meta": [],
            "ioTStateObservation": [
              {
                "value": "Moderate",
                "phenomenonTime": 1402996034207,
                "resultTime": 1402993034208,
                "any": []
              }
            ],
            "any": [],
            "prefix": [
              "inertia:http://ns.inertia.eu/ontologies",
              "linksmart:http://linksmart.org/Ontologies/1.0#Draft",
              "xs:XMLSchema"
            ],
            "about": "99990001",
            "typeof": [
              "inertia:AirQualityStatus"
            ],
            "datatype": "xs:string"
          }
        ],
        "any": [],
        "prefix": [
          "inertia:http://ns.inertia.eu/ontologies",
          "linksmart:http://linksmart.org/Ontologies/1.0#Draft"
        ],
        "about": "D77EC6B0-F039-4734-925E-0A90CE7D1B5B:99990001",
        "typeof": [
          "inertia:AirQualitySensor"
        ]
      }
    ]
  };
  this.dummyDataIndex = 0;
  this.repeat(this.sendDummyData,1000);
}

test.sendDummyData = function() {
  var dummyData = this.dummyData.ioTEntity[this.dummyDataIndex];
  this.dummyDataIndex += 1;
  if (this.dummyDataIndex == this.dummyData.ioTEntity.length) {
    this.stopRepeatingAll();
  }
  console.log('sending fake data', this.dummyDataIndex,"/", this.dummyData.ioTEntity.length);
  this.send("local://proxy", {method:"incoming", params:{event:dummyData}}, null);
}



module.exports = test;