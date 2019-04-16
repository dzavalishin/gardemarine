<?xml version="1.0" encoding="UTF-8"?> 
<java version="1.6.0_10" class="java.beans.XMLDecoder"> 
 <object class="ru.dz.shipMaster.config.Configuration"> 
  <void property="alarmItems"> 
   <void method="add"> 
    <object id="CliAlarm0" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>120.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>110.0</double> 
     </void> 
     <void property="name"> 
      <string>Left engine temperature</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm1" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>120.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>110.0</double> 
     </void> 
     <void property="name"> 
      <string>Right engine temperature</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm2" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="lowCritical"> 
      <double>10.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>20.0</double> 
     </void> 
     <void property="name"> 
      <string>Low fuel</string> 
     </void> 
     <void property="type"> 
      <object class="ru.dz.shipMaster.config.items.CliAlarm$Type" method="valueOf"> 
       <string>LowerForbidden</string> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm3" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>245.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>235.0</double> 
     </void> 
     <void property="lowCritical"> 
      <double>160.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>190.0</double> 
     </void> 
     <void property="name"> 
      <string>Generator AC</string> 
     </void> 
     <void property="type"> 
      <object id="CliAlarm$Type0" class="ru.dz.shipMaster.config.items.CliAlarm$Type" method="valueOf"> 
       <string>SidesForbidden</string> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm4" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>245.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>235.0</double> 
     </void> 
     <void property="lowCritical"> 
      <double>160.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>190.0</double> 
     </void> 
     <void property="name"> 
      <string>Shore AC</string> 
     </void> 
     <void property="type"> 
      <object idref="CliAlarm$Type0"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm5" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>5000.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>4000.0</double> 
     </void> 
     <void property="lowCritical"> 
      <double>300.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>450.0</double> 
     </void> 
     <void property="name"> 
      <string>Left engine RPM</string> 
     </void> 
     <void property="type"> 
      <object idref="CliAlarm$Type0"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm6" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>5000.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>4000.0</double> 
     </void> 
     <void property="lowCritical"> 
      <double>300.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>450.0</double> 
     </void> 
     <void property="name"> 
      <string>Right engine RPM</string> 
     </void> 
     <void property="type"> 
      <object idref="CliAlarm$Type0"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm7" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>15.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>12.0</double> 
     </void> 
     <void property="lowCritical"> 
      <double>-15.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>-12.0</double> 
     </void> 
     <void property="name"> 
      <string>Clin</string> 
     </void> 
     <void property="type"> 
      <object idref="CliAlarm$Type0"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliAlarm8" class="ru.dz.shipMaster.config.items.CliAlarm"> 
     <void property="hiCritical"> 
      <double>85.0</double> 
     </void> 
     <void property="hiWarning"> 
      <double>80.0</double> 
     </void> 
     <void property="lowCritical"> 
      <double>-85.0</double> 
     </void> 
     <void property="lowWarning"> 
      <double>-80.0</double> 
     </void> 
     <void property="name"> 
      <string>Rudder angle</string> 
     </void> 
     <void property="type"> 
      <object idref="CliAlarm$Type0"/> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="alarmStationItems"> 
   <void method="add"> 
    <object id="CliAlarmStation0" class="ru.dz.shipMaster.config.items.CliAlarmStation"> 
     <void property="place"> 
      <string>Bridge</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliAlarmStation"> 
     <void property="nextStation"> 
      <object idref="CliAlarmStation0"/> 
     </void> 
     <void property="place"> 
      <string>Engines room</string> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="conversionItems"> 
   <void method="add"> 
    <object id="CliConversion0" class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="limitInput"> 
      <boolean>false</boolean> 
     </void> 
     <void property="name"> 
      <string>test.25-75</string> 
     </void> 
     <void property="outMax"> 
      <double>75.0</double> 
     </void> 
     <void property="outMin"> 
      <double>25.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>500.0</double> 
     </void> 
     <void property="name"> 
      <string>temperature.dallas</string> 
     </void> 
     <void property="outMax"> 
      <double>125.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliConversion1" class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="name"> 
      <string>general.*100</string> 
     </void> 
     <void property="outMax"> 
      <double>100.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>1.024E9</double> 
     </void> 
     <void property="name"> 
      <string>genegal.div1024</string> 
     </void> 
     <void property="outMax"> 
      <double>1000000.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliConversion2" class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>1.048576E9</double> 
     </void> 
     <void property="name"> 
      <string>general.divMbytes</string> 
     </void> 
     <void property="outMax"> 
      <double>1000.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>100.0</double> 
     </void> 
     <void property="name"> 
      <string>general./100</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>256.0</double> 
     </void> 
     <void property="name"> 
      <string>general./256*100</string> 
     </void> 
     <void property="outMax"> 
      <double>100.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliConversion3" class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>90.0</double> 
     </void> 
     <void property="inMin"> 
      <double>-10.0</double> 
     </void> 
     <void property="name"> 
      <string>dimmer.1-x/100</string> 
     </void> 
     <void property="outMax"> 
      <double>0.0</double> 
     </void> 
     <void property="outMin"> 
      <double>0.5</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliConversion4" class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>74.0</double> 
     </void> 
     <void property="inMin"> 
      <double>26.0</double> 
     </void> 
     <void property="name"> 
      <string>test: 25-75 to 0-1</string> 
     </void> 
     <void property="outputLPFilterWindowSize"> 
      <int>2</int> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void id="Vector0" property="filters"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.data.filter.AngleFromDutyCycleFilter"/> 
      </void> 
     </void> 
     <void property="filters"> 
      <object idref="Vector0"/> 
     </void> 
     <void property="inMax"> 
      <double>255.0</double> 
     </void> 
     <void property="name"> 
      <string>clin.angleFromDuty</string> 
     </void> 
     <void property="outMax"> 
      <double>255.0</double> 
     </void> 
     <void property="outputLPFilterWindowSize"> 
      <int>3</int> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMax"> 
      <double>64.0</double> 
     </void> 
     <void property="inMin"> 
      <double>10.0</double> 
     </void> 
     <void property="name"> 
      <string>level.pressure.test</string> 
     </void> 
     <void property="outMax"> 
      <double>100.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliConversion5" class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void property="inMin"> 
      <double>-1.0</double> 
     </void> 
     <void property="name"> 
      <string>general.-1 to 1 to 25-75</string> 
     </void> 
     <void property="outMax"> 
      <double>75.0</double> 
     </void> 
     <void property="outMin"> 
      <double>25.0</double> 
     </void> 
     <void property="outputLPFilterWindowSize"> 
      <int>2</int> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void id="Vector1" property="filters"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.data.filter.TriggerFilter"/> 
      </void> 
     </void> 
     <void property="filters"> 
      <object idref="Vector1"/> 
     </void> 
     <void property="name"> 
      <string>light.trigger</string> 
     </void> 
     <void property="outputLPFilterWindowSize"> 
      <int>1</int> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliConversion"> 
     <void id="Vector2" property="filters"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.data.filter.CountPerSecondFilter"/> 
      </void> 
     </void> 
     <void property="filters"> 
      <object idref="Vector2"/> 
     </void> 
     <void property="limitInput"> 
      <boolean>false</boolean> 
     </void> 
     <void property="name"> 
      <string>consumpt</string> 
     </void> 
     <void property="outputLPFilterWindowSize"> 
      <int>1</int> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="driverItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliDriver"> 
     <void property="driver"> 
      <object class="ru.dz.shipMaster.dev.misc.TestSignalDriver"> 
       <void property="ports"> 
        <void index="0"> 
         <void property="convertor"> 
          <object idref="CliConversion0"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object id="CliParameter0" class="ru.dz.shipMaster.config.items.CliParameter"> 
           <void property="legend"> 
            <string>test.meander</string> 
           </void> 
           <void property="name"> 
            <string>test.meander</string> 
           </void> 
          </object> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="convertor"> 
          <object idref="CliConversion5"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object id="CliParameter1" class="ru.dz.shipMaster.config.items.CliParameter"> 
           <void property="legend"> 
            <string>Random data</string> 
           </void> 
           <void property="name"> 
            <string>test.random</string> 
           </void> 
          </object> 
         </void> 
        </void> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliDriver"> 
     <void property="driver"> 
      <object class="ru.dz.shipMaster.dev.controller.Wago750"> 
       <void property="ipAddressStr"> 
        <string>192.168.10.230</string> 
       </void> 
       <void property="ports"> 
        <void index="0"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="1"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="3"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="128"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="129"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="130"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="131"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="132"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="133"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="134"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="135"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="136"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="137"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="138"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="139"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="140"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="141"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="142"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="143"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="144"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="145"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="146"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="147"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="148"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="149"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="150"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="151"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="152"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="153"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="154"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="155"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="156"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="157"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="158"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="159"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="160"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="161"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="162"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="163"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="164"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="165"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="166"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="167"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="168"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="169"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="170"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="171"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="172"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="173"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="174"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="175"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="176"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="177"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="178"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="179"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="180"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="181"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="182"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="183"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="184"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="185"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="186"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="187"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="188"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="189"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="190"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="191"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="192"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="193"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="194"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="195"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="196"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="197"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="198"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="199"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="200"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="201"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="202"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="203"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="204"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="205"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="206"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="207"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="256"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="257"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="258"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="259"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="260"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="261"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="262"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="263"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="264"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="265"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="266"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="267"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="268"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="269"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="270"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="271"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="272"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="273"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="274"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="275"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="276"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="277"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="278"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="279"> 
         <void property="absent"> 
          <boolean>false</boolean> 
         </void> 
         <void property="convertor"> 
          <object idref="CliConversion4"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter0"/> 
         </void> 
        </void> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="driverLibItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliDriver"/> 
   </void> 
  </void> 
  <void property="general"> 
   <void property="autologin"> 
    <boolean>true</boolean> 
   </void> 
   <void property="autologinUser"> 
    <object id="CliUser0" class="ru.dz.shipMaster.config.items.CliUser"> 
     <void property="groups"> 
      <void method="add"> 
       <object id="CliGroup0" class="ru.dz.shipMaster.config.items.CliGroup"> 
        <void property="name"> 
         <string>Техник</string> 
        </void> 
        <void property="rights"> 
         <void method="add"> 
          <object id="CliRight0" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="name"> 
            <string>Parameters setup</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight1" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit users</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliUser</string> 
           </void> 
          </object> 
         </void> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="CliGroup1" class="ru.dz.shipMaster.config.items.CliGroup"> 
        <void property="name"> 
         <string>Все права</string> 
        </void> 
        <void property="rights"> 
         <void method="add"> 
          <object idref="CliRight1"/> 
         </void> 
         <void method="add"> 
          <object id="CliRight2" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit groups</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliGroup</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight3" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit system drivers</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliSystemDriver</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight4" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit drivers</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliDriver</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight5" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit window structures</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliWindowStructure</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight6" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit parameters</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliParameter</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight7" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit windows</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliWindow</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight8" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit panels</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliInstrumentPanel</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight9" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit instruments</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliInstrument</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight10" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit network inputs</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliNetInput</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight11" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit network hosts</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliNetHost</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight12" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit conversions</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliConversion</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight13" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit loggers</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliLogger</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight14" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit panel constraints</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliWindowPanelConstraint</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight15" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit alarms</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliAlarm</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight16" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit alarm stations</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliAlarmStation</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight17" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit buses</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliBus</string> 
           </void> 
          </object> 
         </void> 
         <void method="add"> 
          <object id="CliRight18" class="ru.dz.shipMaster.config.items.CliRight"> 
           <void property="canEdit"> 
            <boolean>true</boolean> 
           </void> 
           <void property="name"> 
            <string>Edit channels</string> 
           </void> 
           <void property="targetClassName"> 
            <string>ru.dz.shipMaster.config.items.CliPipe</string> 
           </void> 
          </object> 
         </void> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="login"> 
      <string>test</string> 
     </void> 
     <void property="password"> 
      <string>test</string> 
     </void> 
    </object> 
   </void> 
   <void property="demoMode"> 
    <boolean>false</boolean> 
   </void> 
   <void property="netNodeName"> 
    <string>DeveloperNode</string> 
   </void> 
   <void property="networkActive"> 
    <boolean>true</boolean> 
   </void> 
   <void property="splashScreenShown"> 
    <boolean>false</boolean> 
   </void> 
  </void> 
  <void property="groupItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliGroup"> 
     <void property="name"> 
      <string>Рулевой</string> 
     </void> 
     <void property="rights"> 
      <void method="add"> 
       <object id="CliRight19" class="ru.dz.shipMaster.config.items.CliRight"> 
        <void property="name"> 
         <string>Ship Control</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="CliRight1"/> 
      </void> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliGroup"> 
     <void property="name"> 
      <string>Механик</string> 
     </void> 
     <void property="rights"> 
      <void method="add"> 
       <object idref="CliRight0"/> 
      </void> 
      <void method="add"> 
       <object idref="CliRight19"/> 
      </void> 
      <void method="add"> 
       <object idref="CliRight1"/> 
      </void> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliGroup2" class="ru.dz.shipMaster.config.items.CliGroup"> 
     <void property="name"> 
      <string>Капитан</string> 
     </void> 
     <void property="rights"> 
      <void method="add"> 
       <object idref="CliRight19"/> 
      </void> 
      <void method="add"> 
       <object idref="CliRight0"/> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliRight"> 
        <void property="name"> 
         <string>Users control</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliRight"> 
        <void property="name"> 
         <string>Groups control</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="CliRight1"/> 
      </void> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliGroup0"/> 
   </void> 
   <void method="add"> 
    <object idref="CliGroup1"/> 
   </void> 
  </void> 
  <void property="instrumentItems"> 
   <void method="add"> 
    <object id="CliInstrument0" class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="component"> 
      <object class="ru.dz.shipMaster.ui.meter.ClinoMeter"> 
       <void property="current"> 
        <double>52.0</double> 
       </void> 
       <void property="legend"> 
        <string>Clin</string> 
       </void> 
       <void property="units"> 
        <string>°</string> 
       </void> 
      </object> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.meter.ClinoMeter</string> 
     </void> 
     <void property="legend"> 
      <string>Clin</string> 
     </void> 
     <void property="name"> 
      <string>ship.clinometer</string> 
     </void> 
     <void property="units"> 
      <string>°</string> 
     </void> 
     <void property="valueMaximum"> 
      <double>15.0</double> 
     </void> 
     <void property="valueMinimum"> 
      <double>-15.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrument1" class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="component"> 
      <object class="ru.dz.shipMaster.ui.meter.GeneralGraph"> 
       <void property="current"> 
        <double>52.0</double> 
       </void> 
       <void property="legend"> 
        <string>Temperature</string> 
       </void> 
       <void property="maximum"> 
        <double>100.0</double> 
       </void> 
       <void property="units"> 
        <string>°C</string> 
       </void> 
      </object> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.meter.GeneralGraph</string> 
     </void> 
     <void property="name"> 
      <string>Graph</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrument2" class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="component"> 
      <object class="ru.dz.shipMaster.ui.meter.CenteredBarRulerMeter"> 
       <void property="current"> 
        <double>52.0</double> 
       </void> 
       <void property="legend"> 
        <string>Rudder</string> 
       </void> 
       <void property="maximum"> 
        <double>90.0</double> 
       </void> 
       <void property="minimum"> 
        <double>-90.0</double> 
       </void> 
       <void property="units"> 
        <string>°</string> 
       </void> 
      </object> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.meter.CenteredBarRulerMeter</string> 
     </void> 
     <void property="legend"> 
      <string>Rudder</string> 
     </void> 
     <void property="name"> 
      <string>ship.Rudder</string> 
     </void> 
     <void property="units"> 
      <string>°</string> 
     </void> 
     <void property="valueMaximum"> 
      <double>90.0</double> 
     </void> 
     <void property="valueMinimum"> 
      <double>-90.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrument3" class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="component"> 
      <object class="ru.dz.shipMaster.ui.meter.CompassMeter"> 
       <void property="current"> 
        <double>52.0</double> 
       </void> 
       <void property="legend"> 
        <string>Engine</string> 
       </void> 
       <void property="maximum"> 
        <double>4000.0</double> 
       </void> 
       <void property="units"> 
        <string>rpm</string> 
       </void> 
      </object> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.meter.CompassMeter</string> 
     </void> 
     <void property="legend"> 
      <string>Engine</string> 
     </void> 
     <void property="name"> 
      <string>engine.rpm</string> 
     </void> 
     <void property="units"> 
      <string>rpm</string> 
     </void> 
     <void property="valueMaximum"> 
      <double>4000.0</double> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="imageShortName"> 
      <string>light.png</string> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.meter.CriticalPictogram</string> 
     </void> 
     <void property="name"> 
      <string>Pictogram</string> 
     </void> 
     <void property="offMessage"> 
      <string></string> 
     </void> 
     <void property="onMessage"> 
      <string></string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrument4" class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="component"> 
      <object class="ru.dz.shipMaster.ui.meter.RawPictogram"> 
       <void property="current"> 
        <double>44.21225030953441</double> 
       </void> 
       <void property="legend"> 
        <string>Temperature</string> 
       </void> 
       <void property="maximum"> 
        <double>100.0</double> 
       </void> 
       <void property="offMessage"> 
        <string></string> 
       </void> 
       <void property="onMessage"> 
        <string></string> 
       </void> 
       <void property="pictogramFileName"> 
        <string>ship_mask_140.png</string> 
       </void> 
       <void property="units"> 
        <string>°C</string> 
       </void> 
      </object> 
     </void> 
     <void property="imageShortName"> 
      <string>ship_mask_140.png</string> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.meter.RawPictogram</string> 
     </void> 
     <void property="name"> 
      <string>RAW (ship cut)</string> 
     </void> 
     <void property="offMessage"> 
      <string></string> 
     </void> 
     <void property="onMessage"> 
      <string></string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
     <void property="component"> 
      <object class="ru.dz.shipMaster.ui.control.DashButton"> 
       <void property="pictogramFileName"> 
        <string>duke.png</string> 
       </void> 
      </object> 
     </void> 
     <void property="imageShortName"> 
      <string>duke.png</string> 
     </void> 
     <void property="instrumentClassName"> 
      <string>ru.dz.shipMaster.ui.control.DashButton</string> 
     </void> 
     <void property="name"> 
      <string>Button</string> 
     </void> 
     <void property="offMessage"> 
      <string></string> 
     </void> 
     <void property="onMessage"> 
      <string></string> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="instrumentLibItems"> 
   <void method="add"> 
    <object idref="CliInstrument1"/> 
   </void> 
   <void method="add"> 
    <object idref="CliInstrument0"/> 
   </void> 
   <void method="add"> 
    <object idref="CliInstrument2"/> 
   </void> 
   <void method="add"> 
    <object idref="CliInstrument3"/> 
   </void> 
  </void> 
  <void property="instrumentPanelItems"> 
   <void method="add"> 
    <object id="CliInstrumentPanel0" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.CenteredBarRulerMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Перо руля</string> 
          </void> 
          <void property="maximum"> 
           <double>90.0</double> 
          </void> 
          <void property="minimum"> 
           <double>-90.0</double> 
          </void> 
          <void property="units"> 
           <string>Degrees</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.CenteredBarRulerMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Rudder</string> 
        </void> 
        <void property="name"> 
         <string>Rudder</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter2" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm8"/> 
          </void> 
          <void property="legend"> 
           <string>Перо руля</string> 
          </void> 
          <void property="maxValue"> 
           <double>90.0</double> 
          </void> 
          <void property="minValue"> 
           <double>-90.0</double> 
          </void> 
          <void property="name"> 
           <string>ship.rudder</string> 
          </void> 
          <void property="unit"> 
           <object id="Unit0" class="ru.dz.shipMaster.data.units.Unit"> 
            <void property="group"> 
             <object id="UnitGroup0" class="ru.dz.shipMaster.data.units.UnitGroup"> 
              <void property="imperialDefault"> 
               <object idref="Unit0"/> 
              </void> 
              <void property="metricDefault"> 
               <object idref="Unit0"/> 
              </void> 
              <void property="reference"> 
               <object idref="Unit0"/> 
              </void> 
             </object> 
            </void> 
            <void property="longName"> 
             <string>Degrees</string> 
            </void> 
            <void property="name"> 
             <string>°</string> 
            </void> 
           </object> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>°</string> 
        </void> 
        <void property="valueMaximum"> 
         <double>90.0</double> 
        </void> 
        <void property="valueMinimum"> 
         <double>-90.0</double> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Rudder</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="absolutePositioning"> 
      <boolean>true</boolean> 
     </void> 
     <void property="absolutePositions"> 
      <void method="add"> 
       <object id="Point0" class="java.awt.Point"> 
        <int>4</int> 
        <int>3</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>306</int> 
        <int>1</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>113</int> 
        <int>11</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>106</int> 
        <int>2</int> 
       </object> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object id="Point1" class="java.awt.Point"> 
        <int>226</int> 
        <int>154</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point2" class="java.awt.Point"> 
        <int>169</int> 
        <int>72</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point3" class="java.awt.Point"> 
        <int>104</int> 
        <int>56</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object id="Point4" class="java.awt.Point"> 
        <int>305</int> 
        <int>55</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point5" class="java.awt.Point"> 
        <int>164</int> 
        <int>144</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <object idref="Point0"/> 
      </void> 
      <void method="add"> 
       <object idref="Point4"/> 
      </void> 
      <void method="add"> 
       <object idref="Point5"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point1"/> 
      </void> 
      <void method="add"> 
       <object idref="Point2"/> 
      </void> 
      <void method="add"> 
       <object idref="Point3"/> 
      </void> 
     </void> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.component.GeneralImage"/> 
        </void> 
        <void property="imageShortName"> 
         <string>temperature.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.component.GeneralImage</string> 
        </void> 
        <void property="name"> 
         <string>temp</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrument3"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrument4"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrument0"/> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Engine - test</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel1" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.HalfGaugeMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>tanks.fuel.level</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>Percent</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.HalfGaugeMeter</string> 
        </void> 
        <void property="legend"> 
         <string>tanks.fuel.level</string> 
        </void> 
        <void property="name"> 
         <string>fuel level</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter3" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm2"/> 
          </void> 
          <void property="legend"> 
           <string>Уровень топлива</string> 
          </void> 
          <void property="name"> 
           <string>tanks.fuel.level</string> 
          </void> 
          <void property="unit"> 
           <object id="Unit1" class="ru.dz.shipMaster.data.units.Unit"> 
            <void property="group"> 
             <object id="UnitGroup1" class="ru.dz.shipMaster.data.units.UnitGroup"> 
              <void property="imperialDefault"> 
               <object idref="Unit1"/> 
              </void> 
              <void property="metricDefault"> 
               <object idref="Unit1"/> 
              </void> 
              <void property="reference"> 
               <object idref="Unit1"/> 
              </void> 
             </object> 
            </void> 
            <void property="longName"> 
             <string>Проценты</string> 
            </void> 
            <void property="name"> 
             <string>%</string> 
            </void> 
           </object> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>Проценты</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.LinearVerticalMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.LinearVerticalMeter</string> 
        </void> 
        <void property="name"> 
         <string>temp a</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter4" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm0"/> 
          </void> 
          <void property="legend"> 
           <string>Температура лев. дв.</string> 
          </void> 
          <void property="maxValue"> 
           <double>150.0</double> 
          </void> 
          <void property="name"> 
           <string>engine.left.temperature</string> 
          </void> 
          <void property="unit"> 
           <object id="Unit2" class="ru.dz.shipMaster.data.units.Unit"> 
            <void property="group"> 
             <object id="UnitGroup2" class="ru.dz.shipMaster.data.units.UnitGroup"> 
              <void property="imperialDefault"> 
               <object idref="Unit2"/> 
              </void> 
              <void property="metricDefault"> 
               <object idref="Unit2"/> 
              </void> 
              <void property="reference"> 
               <object idref="Unit2"/> 
              </void> 
             </object> 
            </void> 
            <void property="longName"> 
             <string>Degrees celsius</string> 
            </void> 
            <void property="name"> 
             <string>°C</string> 
            </void> 
           </object> 
          </void> 
         </object> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.LinearVerticalMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.LinearVerticalMeter</string> 
        </void> 
        <void property="name"> 
         <string>temp b</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter5" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm1"/> 
          </void> 
          <void property="legend"> 
           <string>Температура прав. дв.</string> 
          </void> 
          <void property="maxValue"> 
           <double>150.0</double> 
          </void> 
          <void property="name"> 
           <string>engine.right.temperature</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit2"/> 
          </void> 
         </object> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Engine A</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel2" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="absolutePositioning"> 
      <boolean>true</boolean> 
     </void> 
     <void property="absolutePositions"> 
      <void method="add"> 
       <object id="Point6" class="java.awt.Point"> 
        <int>4</int> 
        <int>92</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point7" class="java.awt.Point"> 
        <int>4</int> 
        <int>48</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point8" class="java.awt.Point"> 
        <int>4</int> 
        <int>5</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point9" class="java.awt.Point"> 
        <int>0</int> 
        <int>0</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point10" class="java.awt.Point"> 
        <int>0</int> 
        <int>-1</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point11" class="java.awt.Point"> 
        <int>0</int> 
        <int>-1</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point6"/> 
      </void> 
      <void method="add"> 
       <object idref="Point7"/> 
      </void> 
      <void method="add"> 
       <object idref="Point8"/> 
      </void> 
      <void method="add"> 
       <object idref="Point9"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point6"/> 
      </void> 
      <void method="add"> 
       <object idref="Point7"/> 
      </void> 
      <void method="add"> 
       <object idref="Point8"/> 
      </void> 
      <void method="add"> 
       <object idref="Point9"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point6"/> 
      </void> 
      <void method="add"> 
       <object idref="Point7"/> 
      </void> 
      <void method="add"> 
       <object idref="Point8"/> 
      </void> 
      <void method="add"> 
       <object idref="Point9"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
      <void method="add"> 
       <object idref="Point10"/> 
      </void> 
      <void method="add"> 
       <object idref="Point11"/> 
      </void> 
     </void> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralPictogram"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string></string> 
          </void> 
          <void property="onMessage"> 
           <string>Generator stop active</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>diode.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>diode.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralPictogram</string> 
        </void> 
        <void property="name"> 
         <string>Stop</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string>Generator stop active</string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter6" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>Generator stop</string> 
          </void> 
          <void property="maxValue"> 
           <double>1.0</double> 
          </void> 
          <void property="name"> 
           <string>generator.control.stop</string> 
          </void> 
         </object> 
        </void> 
        <void property="valueMaximum"> 
         <double>1.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralPictogram"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Ignition</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string>Generator Ignition OFF</string> 
          </void> 
          <void property="onMessage"> 
           <string>Generator Ignition ON</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>lamp.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>lamp.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralPictogram</string> 
        </void> 
        <void property="legend"> 
         <string>Ignition</string> 
        </void> 
        <void property="name"> 
         <string>Ignition</string> 
        </void> 
        <void property="offMessage"> 
         <string>Generator Ignition OFF</string> 
        </void> 
        <void property="onMessage"> 
         <string>Generator Ignition ON</string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter7" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>Generator ignition</string> 
          </void> 
          <void property="maxValue"> 
           <double>1.0</double> 
          </void> 
          <void property="name"> 
           <string>generator.control.ignition</string> 
          </void> 
         </object> 
        </void> 
        <void property="valueMaximum"> 
         <double>1.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralPictogram"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>1.0</double> 
          </void> 
          <void property="offMessage"> 
           <string></string> 
          </void> 
          <void property="onMessage"> 
           <string>Generator start active</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>rotation.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>rotation.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralPictogram</string> 
        </void> 
        <void property="name"> 
         <string>Starter</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string>Generator start active</string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter8" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>Generator start</string> 
          </void> 
          <void property="maxValue"> 
           <double>1.0</double> 
          </void> 
          <void property="name"> 
           <string>generator.control.start</string> 
          </void> 
         </object> 
        </void> 
        <void property="valueMaximum"> 
         <double>1.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.RawPictogram"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string></string> 
          </void> 
          <void property="onMessage"> 
           <string></string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>generator_med.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.RawPictogram</string> 
        </void> 
        <void property="name"> 
         <string>Background</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Generator</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel3" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.misc.RunningLine"/> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.misc.RunningLine</string> 
        </void> 
        <void property="name"> 
         <string>Running line</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Running line</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel4" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.logger.MessageWindow"/> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.logger.MessageWindow</string> 
        </void> 
        <void property="name"> 
         <string>Messages</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="verticalWeight"> 
         <double>0.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.logger.LogWindow"/> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.logger.LogWindow</string> 
        </void> 
        <void property="name"> 
         <string>Log</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Logs panel</string> 
     </void> 
     <void property="vertical"> 
      <boolean>true</boolean> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel5" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.CompassMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Left engine</string> 
          </void> 
          <void property="maximum"> 
           <double>6000.0</double> 
          </void> 
          <void property="units"> 
           <string>Rotations per minute</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.CompassMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Left engine</string> 
        </void> 
        <void property="name"> 
         <string>L RPM</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter9" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm5"/> 
          </void> 
          <void property="legend"> 
           <string>Обороты лев.</string> 
          </void> 
          <void property="maxValue"> 
           <double>6000.0</double> 
          </void> 
          <void property="name"> 
           <string>engine.left.rpm</string> 
          </void> 
          <void property="unit"> 
           <object id="Unit3" class="ru.dz.shipMaster.data.units.Unit"> 
            <void property="group"> 
             <object id="UnitGroup3" class="ru.dz.shipMaster.data.units.UnitGroup"> 
              <void property="imperialDefault"> 
               <object idref="Unit3"/> 
              </void> 
              <void property="metricDefault"> 
               <object idref="Unit3"/> 
              </void> 
              <void property="reference"> 
               <object idref="Unit3"/> 
              </void> 
             </object> 
            </void> 
            <void property="longName"> 
             <string>Rotations per minute</string> 
            </void> 
            <void property="name"> 
             <string>RPM</string> 
            </void> 
           </object> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>Rotations per minute</string> 
        </void> 
        <void property="valueMaximum"> 
         <double>6000.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.CompassMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Right engine</string> 
          </void> 
          <void property="maximum"> 
           <double>6000.0</double> 
          </void> 
          <void property="units"> 
           <string>Rotations per minute</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.CompassMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Right engine</string> 
        </void> 
        <void property="name"> 
         <string>R RPM</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter10" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm6"/> 
          </void> 
          <void property="legend"> 
           <string>Обороты прав.</string> 
          </void> 
          <void property="maxValue"> 
           <double>6000.0</double> 
          </void> 
          <void property="name"> 
           <string>engine.right.rpm</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit3"/> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>Rotations per minute</string> 
        </void> 
        <void property="valueMaximum"> 
         <double>6000.0</double> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Power</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel6" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.ClinoMeter"> 
          <void property="current"> 
           <double>-14.109717340532143</double> 
          </void> 
          <void property="legend"> 
           <string>Clin</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>front_ship_mask.png</string> 
          </void> 
          <void property="units"> 
           <string>Degrees</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>front_ship_mask.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.ClinoMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Clin</string> 
        </void> 
        <void property="name"> 
         <string>Clin</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter11" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="alarm"> 
           <object idref="CliAlarm7"/> 
          </void> 
          <void property="legend"> 
           <string>Крен</string> 
          </void> 
          <void property="maxValue"> 
           <double>15.0</double> 
          </void> 
          <void property="minValue"> 
           <double>-15.0</double> 
          </void> 
          <void property="name"> 
           <string>ship.clin</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit0"/> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>Degrees</string> 
        </void> 
        <void property="valueMaximum"> 
         <double>15.0</double> 
        </void> 
        <void property="valueMinimum"> 
         <double>-15.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.ClinoMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>side_ship_mask.png</string> 
          </void> 
          <void property="units"> 
           <string>°</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>side_ship_mask.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.ClinoMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Different</string> 
        </void> 
        <void property="name"> 
         <string>Diff</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter12" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>Дифферент</string> 
          </void> 
          <void property="maxValue"> 
           <double>15.0</double> 
          </void> 
          <void property="minValue"> 
           <double>-15.0</double> 
          </void> 
          <void property="name"> 
           <string>ship.different</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit0"/> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>Degrees</string> 
        </void> 
        <void property="valueMaximum"> 
         <double>15.0</double> 
        </void> 
        <void property="valueMinimum"> 
         <double>-15.0</double> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Ship</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel7" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralGraph"> 
          <void property="current"> 
           <double>66.12900350108029</double> 
          </void> 
          <void property="legend"> 
           <string>test.meander</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralGraph</string> 
        </void> 
        <void property="legend"> 
         <string>test.meander</string> 
        </void> 
        <void property="name"> 
         <string>meander</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter0"/> 
        </void> 
        <void property="units"> 
         <string>°C</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralGraph"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>test.100-meander</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>%</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralGraph</string> 
        </void> 
        <void property="legend"> 
         <string>test.100-meander</string> 
        </void> 
        <void property="name"> 
         <string>neg meander</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter13" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>test.100-meander</string> 
          </void> 
          <void property="name"> 
           <string>test.100-meander</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit1"/> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>%</string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Graphs</string> 
     </void> 
     <void property="vertical"> 
      <boolean>true</boolean> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel8" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="absolutePositioning"> 
      <boolean>true</boolean> 
     </void> 
     <void property="absolutePositions"> 
      <void method="add"> 
       <object id="Point12" class="java.awt.Point"> 
        <int>7</int> 
        <int>5</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point13" class="java.awt.Point"> 
        <int>7</int> 
        <int>70</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point14" class="java.awt.Point"> 
        <int>7</int> 
        <int>135</int> 
       </object> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object id="Point15" class="java.awt.Point"> 
        <int>0</int> 
        <int>64</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point16" class="java.awt.Point"> 
        <int>0</int> 
        <int>128</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point17" class="java.awt.Point"> 
        <int>4</int> 
        <int>5</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point18" class="java.awt.Point"> 
        <int>4</int> 
        <int>70</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point19" class="java.awt.Point"> 
        <int>4</int> 
        <int>136</int> 
       </object> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
      <void method="add"> 
       <object idref="Point12"/> 
      </void> 
      <void method="add"> 
       <object idref="Point13"/> 
      </void> 
      <void method="add"> 
       <object idref="Point14"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
      <void method="add"> 
       <object idref="Point17"/> 
      </void> 
      <void method="add"> 
       <object idref="Point18"/> 
      </void> 
      <void method="add"> 
       <object idref="Point19"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
      <void method="add"> 
       <object idref="Point12"/> 
      </void> 
      <void method="add"> 
       <object idref="Point13"/> 
      </void> 
      <void method="add"> 
       <object idref="Point14"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
      <void method="add"> 
       <object idref="Point17"/> 
      </void> 
      <void method="add"> 
       <object idref="Point18"/> 
      </void> 
      <void method="add"> 
       <object idref="Point19"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
      <void method="add"> 
       <object idref="Point12"/> 
      </void> 
      <void method="add"> 
       <object idref="Point13"/> 
      </void> 
      <void method="add"> 
       <object idref="Point14"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
      <void method="add"> 
       <object idref="Point17"/> 
      </void> 
      <void method="add"> 
       <object idref="Point18"/> 
      </void> 
      <void method="add"> 
       <object idref="Point19"/> 
      </void> 
      <void method="add"> 
       <null/> 
      </void> 
      <void method="add"> 
       <object idref="Point15"/> 
      </void> 
      <void method="add"> 
       <object idref="Point16"/> 
      </void> 
     </void> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.PlainDigitalMeter"> 
          <void property="current"> 
           <double>47.98212755484299</double> 
          </void> 
          <void property="legend"> 
           <string>consumpt</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>%</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.PlainDigitalMeter</string> 
        </void> 
        <void property="legend"> 
         <string>test.meander</string> 
        </void> 
        <void property="name"> 
         <string>consumpt</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter0"/> 
        </void> 
        <void property="units"> 
         <string>%</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.PlainDigitalMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Gen voltage</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>V</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.PlainDigitalMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Gen voltage</string> 
        </void> 
        <void property="name"> 
         <string>Gen Voltage</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter1"/> 
        </void> 
        <void property="units"> 
         <string>V</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.PlainDigitalMeter"> 
          <void property="current"> 
           <double>45.817819126060485</double> 
          </void> 
          <void property="legend"> 
           <string>Start</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>Knots</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.PlainDigitalMeter</string> 
        </void> 
        <void property="legend"> 
         <string>Start</string> 
        </void> 
        <void property="name"> 
         <string>Start</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter8"/> 
        </void> 
        <void property="units"> 
         <string>Knots</string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Numerics</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel9" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="absolutePositioning"> 
      <boolean>true</boolean> 
     </void> 
     <void property="absolutePositions"> 
      <void method="add"> 
       <object id="Point20" class="java.awt.Point"> 
        <int>8</int> 
        <int>13</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point21" class="java.awt.Point"> 
        <int>57</int> 
        <int>13</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point22" class="java.awt.Point"> 
        <int>57</int> 
        <int>155</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point23" class="java.awt.Point"> 
        <int>8</int> 
        <int>155</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>18</int> 
        <int>68</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point24" class="java.awt.Point"> 
        <int>8</int> 
        <int>59</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point25" class="java.awt.Point"> 
        <int>8</int> 
        <int>109</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point21"/> 
      </void> 
      <void method="add"> 
       <object idref="Point22"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
      <void method="add"> 
       <object idref="Point20"/> 
      </void> 
      <void method="add"> 
       <object idref="Point24"/> 
      </void> 
      <void method="add"> 
       <object idref="Point25"/> 
      </void> 
      <void method="add"> 
       <object idref="Point23"/> 
      </void> 
     </void> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralPictogram"> 
          <void property="current"> 
           <double>47.206814996609246</double> 
          </void> 
          <void property="legend"> 
           <string>test.meander</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string>(Навигационные огни выкл)</string> 
          </void> 
          <void property="onMessage"> 
           <string>Навигационные огни вкл</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>light.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>light.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralPictogram</string> 
        </void> 
        <void property="legend"> 
         <string>test.meander</string> 
        </void> 
        <void property="name"> 
         <string>Lights</string> 
        </void> 
        <void property="offMessage"> 
         <string>(Навигационные огни выкл)</string> 
        </void> 
        <void property="onMessage"> 
         <string>Навигационные огни вкл</string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter0"/> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.WarningPictogram"> 
          <void property="current"> 
           <double>63.21610058858942</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string>(Температура в норме)</string> 
          </void> 
          <void property="onMessage"> 
           <string>Перегрев двигателя</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>temperature.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>temperature.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.WarningPictogram</string> 
        </void> 
        <void property="name"> 
         <string>Temperature</string> 
        </void> 
        <void property="offMessage"> 
         <string>(Температура в норме)</string> 
        </void> 
        <void property="onMessage"> 
         <string>Перегрев двигателя</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralPictogram"> 
          <void property="current"> 
           <double>83.70487177427175</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string>(Стоп)</string> 
          </void> 
          <void property="onMessage"> 
           <string>Ход</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>move.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>move.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralPictogram</string> 
        </void> 
        <void property="name"> 
         <string>Run</string> 
        </void> 
        <void property="offMessage"> 
         <string>(Стоп)</string> 
        </void> 
        <void property="onMessage"> 
         <string>Ход</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.GeneralPictogram"> 
          <void property="current"> 
           <double>54.5976518958183</double> 
          </void> 
          <void property="legend"> 
           <string>Random data</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string>(разблокировано)</string> 
          </void> 
          <void property="onMessage"> 
           <string>Заблокировано</string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>lock.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>lock.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.GeneralPictogram</string> 
        </void> 
        <void property="legend"> 
         <string>Random data</string> 
        </void> 
        <void property="name"> 
         <string>Lock</string> 
        </void> 
        <void property="offMessage"> 
         <string>(Разблокировано)</string> 
        </void> 
        <void property="onMessage"> 
         <string>Заблокировано</string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter1"/> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.control.DashButton"> 
          <void property="pictogramFileName"> 
           <string>home-lamp-off.png</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>home-lamp-off.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.control.DashButton</string> 
        </void> 
        <void property="legend"> 
         <string>W1</string> 
        </void> 
        <void property="name"> 
         <string>btn</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter14" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>W1</string> 
          </void> 
          <void property="name"> 
           <string>control.w1</string> 
          </void> 
          <void property="unit"> 
           <object id="Unit4" class="ru.dz.shipMaster.data.units.Unit"> 
            <void property="firstMultiplicator"> 
             <double>100.0</double> 
            </void> 
            <void property="group"> 
             <object idref="UnitGroup1"/> 
            </void> 
            <void property="longName"> 
             <string>%/100</string> 
            </void> 
            <void property="name"> 
             <string>%/100</string> 
            </void> 
           </object> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>%/100</string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Lamps</string> 
     </void> 
     <void property="panelSize"> 
      <object class="java.awt.Dimension"> 
       <int>100</int> 
       <int>200</int> 
      </object> 
     </void> 
     <void property="vertical"> 
      <boolean>true</boolean> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel10" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.HalfGaugeMeter"> 
          <void property="current"> 
           <double>50.424886257663786</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.HalfGaugeMeter</string> 
        </void> 
        <void property="name"> 
         <string>A RPM</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter9"/> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>2: Engine A</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel11" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.HalfGaugeMeter"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.HalfGaugeMeter</string> 
        </void> 
        <void property="name"> 
         <string>RPM B</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object idref="CliParameter10"/> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>2: Engine B</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliInstrumentPanel12" class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
     <void property="absolutePositioning"> 
      <boolean>true</boolean> 
     </void> 
     <void property="absolutePositions"> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>339</int> 
        <int>199</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>339</int> 
        <int>199</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>37</int> 
        <int>130</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="java.awt.Point"> 
        <int>0</int> 
        <int>-1</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point26" class="java.awt.Point"> 
        <int>37</int> 
        <int>127</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point27" class="java.awt.Point"> 
        <int>0</int> 
        <int>-3</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point28" class="java.awt.Point"> 
        <int>244</int> 
        <int>38</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object id="Point29" class="java.awt.Point"> 
        <int>37</int> 
        <int>130</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point30" class="java.awt.Point"> 
        <int>-1</int> 
        <int>-1</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object id="Point31" class="java.awt.Point"> 
        <int>340</int> 
        <int>195</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point32" class="java.awt.Point"> 
        <int>39</int> 
        <int>125</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object id="Point33" class="java.awt.Point"> 
        <int>1</int> 
        <int>-6</int> 
       </object> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point31"/> 
      </void> 
      <void method="add"> 
       <object idref="Point32"/> 
      </void> 
      <void method="add"> 
       <object idref="Point33"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point31"/> 
      </void> 
      <void method="add"> 
       <object idref="Point32"/> 
      </void> 
      <void method="add"> 
       <object idref="Point33"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point29"/> 
      </void> 
      <void method="add"> 
       <object idref="Point30"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point26"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
      <void method="add"> 
       <object idref="Point28"/> 
      </void> 
      <void method="add"> 
       <object idref="Point27"/> 
      </void> 
     </void> 
     <void property="instruments"> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.RawOnOffPictogram"> 
          <void property="current"> 
           <double>0.47567484270269583</double> 
          </void> 
          <void property="legend"> 
           <string>Cbus in 0</string> 
          </void> 
          <void property="maximum"> 
           <double>1.0</double> 
          </void> 
          <void property="offMessage"> 
           <string></string> 
          </void> 
          <void property="onMessage"> 
           <string></string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>room_back_on.png</string> 
          </void> 
          <void property="units"> 
           <string>%/100</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>room_back_on.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.RawOnOffPictogram</string> 
        </void> 
        <void property="legend"> 
         <string>Cbus in 0</string> 
        </void> 
        <void property="name"> 
         <string>room</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter15" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>Light in 0</string> 
          </void> 
          <void property="maxValue"> 
           <double>1.0</double> 
          </void> 
          <void property="name"> 
           <string>light.in0</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit4"/> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>%/100</string> 
        </void> 
        <void property="valueMaximum"> 
         <double>1.0</double> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.RawPictogram"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string></string> 
          </void> 
          <void property="onMessage"> 
           <string></string> 
          </void> 
          <void property="pictogramFileName"> 
           <string>room_back_off.png</string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>room_back_off.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.RawPictogram</string> 
        </void> 
        <void property="name"> 
         <string>backroom</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.control.DashImageButton"> 
          <void property="pictogramFileName"> 
           <string>home-lamp-on.png</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>home-lamp-on.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.control.DashImageButton</string> 
        </void> 
        <void property="legend"> 
         <string>group.name</string> 
        </void> 
        <void property="name"> 
         <string>lamp</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
        <void property="parameter"> 
         <object id="CliParameter16" class="ru.dz.shipMaster.config.items.CliParameter"> 
          <void property="legend"> 
           <string>light.out0</string> 
          </void> 
          <void property="name"> 
           <string>light.out0</string> 
          </void> 
          <void property="unit"> 
           <object idref="Unit1"/> 
          </void> 
         </object> 
        </void> 
        <void property="units"> 
         <string>Percent</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
        <void property="component"> 
         <object class="ru.dz.shipMaster.ui.meter.RawPictogram"> 
          <void property="current"> 
           <double>52.0</double> 
          </void> 
          <void property="legend"> 
           <string>Temperature</string> 
          </void> 
          <void property="maximum"> 
           <double>100.0</double> 
          </void> 
          <void property="offMessage"> 
           <string></string> 
          </void> 
          <void property="onMessage"> 
           <string></string> 
          </void> 
          <void property="units"> 
           <string>°C</string> 
          </void> 
         </object> 
        </void> 
        <void property="imageShortName"> 
         <string>home2-back.png</string> 
        </void> 
        <void property="instrumentClassName"> 
         <string>ru.dz.shipMaster.ui.meter.RawPictogram</string> 
        </void> 
        <void property="name"> 
         <string>back</string> 
        </void> 
        <void property="offMessage"> 
         <string></string> 
        </void> 
        <void property="onMessage"> 
         <string></string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="name"> 
      <string>Home</string> 
     </void> 
     <void property="panelSize"> 
      <object class="java.awt.Dimension"> 
       <int>864</int> 
       <int>648</int> 
      </object> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="instrumentPanelLibItems"> 
   <void method="add"> 
    <object idref="CliInstrumentPanel2"/> 
   </void> 
  </void> 
  <void property="loggerItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliLogger"> 
     <void property="logDestinationName"> 
      <string>c:/tmp/Gardemarine.log</string> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="netInputItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliNetInput"> 
     <void property="enabled"> 
      <boolean>true</boolean> 
     </void> 
     <void property="hostName"> 
      <string>TestNode</string> 
     </void> 
     <void property="itemName"> 
      <string>DiskFree</string> 
     </void> 
     <void property="target"> 
      <object id="CliParameter17" class="ru.dz.shipMaster.config.items.CliParameter"> 
       <void property="legend"> 
        <string>Disk free</string> 
       </void> 
       <void property="name"> 
        <string>net.diskfree</string> 
       </void> 
       <void property="translateToNet"> 
        <boolean>false</boolean> 
       </void> 
       <void property="unit"> 
        <object idref="Unit1"/> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="parameterItems"> 
   <void method="add"> 
    <object idref="CliParameter4"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter5"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter9"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter10"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter3"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>cameras.rearView</string> 
     </void> 
     <void property="name"> 
      <string>cameras.rearView</string> 
     </void> 
     <void property="type"> 
      <object id="CliParameter$Type0" class="ru.dz.shipMaster.config.items.CliParameter$Type" method="valueOf"> 
       <string>Image</string> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>Speed</string> 
     </void> 
     <void property="name"> 
      <string>ship.speed</string> 
     </void> 
     <void property="unit"> 
      <object id="Unit5" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="group"> 
        <object id="UnitGroup4" class="ru.dz.shipMaster.data.units.UnitGroup"> 
         <void property="imperialDefault"> 
          <object id="Unit6" class="ru.dz.shipMaster.data.units.Unit"> 
           <void property="firstMultiplicator"> 
            <double>1000.0</double> 
           </void> 
           <void property="group"> 
            <object idref="UnitGroup4"/> 
           </void> 
           <void property="longName"> 
            <string>Kilometers per hour</string> 
           </void> 
           <void property="name"> 
            <string>km/h</string> 
           </void> 
           <void property="secondMultiplicator"> 
            <double>2.777778E-4</double> 
           </void> 
          </object> 
         </void> 
         <void property="metricDefault"> 
          <object idref="Unit6"/> 
         </void> 
         <void property="reference"> 
          <object idref="Unit5"/> 
         </void> 
        </object> 
       </void> 
       <void property="longName"> 
        <string>Meters per second</string> 
       </void> 
       <void property="name"> 
        <string>m/s</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter11"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter12"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter2"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="alarm"> 
      <object idref="CliAlarm4"/> 
     </void> 
     <void property="legend"> 
      <string>Shore voltage</string> 
     </void> 
     <void property="maxValue"> 
      <double>300.0</double> 
     </void> 
     <void property="name"> 
      <string>power.shore.voltage</string> 
     </void> 
     <void property="unit"> 
      <object id="Unit7" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="group"> 
        <object id="UnitGroup5" class="ru.dz.shipMaster.data.units.UnitGroup"> 
         <void property="imperialDefault"> 
          <object idref="Unit7"/> 
         </void> 
         <void property="metricDefault"> 
          <object idref="Unit7"/> 
         </void> 
         <void property="reference"> 
          <object idref="Unit7"/> 
         </void> 
        </object> 
       </void> 
       <void property="longName"> 
        <string>Volts</string> 
       </void> 
       <void property="name"> 
        <string>V</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="alarm"> 
      <object idref="CliAlarm3"/> 
     </void> 
     <void property="legend"> 
      <string>Generator voltage</string> 
     </void> 
     <void property="maxValue"> 
      <double>300.0</double> 
     </void> 
     <void property="name"> 
      <string>power.generator.voltage</string> 
     </void> 
     <void property="unit"> 
      <object idref="Unit7"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter0"/> 
   </void> 
   <void method="add"> 
    <object id="CliParameter18" class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>CPU Load</string> 
     </void> 
     <void property="name"> 
      <string>system.cpu.load</string> 
     </void> 
     <void property="unit"> 
      <object idref="Unit1"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliParameter19" class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>Memory left</string> 
     </void> 
     <void property="maxValue"> 
      <double>1000.0</double> 
     </void> 
     <void property="name"> 
      <string>system.memory.left</string> 
     </void> 
     <void property="unit"> 
      <object id="Unit8" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="longName"> 
        <string>Megabytes</string> 
       </void> 
       <void property="name"> 
        <string>Mb</string> 
       </void> 
       <void property="secondMultiplicator"> 
        <double>1024.0</double> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliParameter20" class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>Memory used</string> 
     </void> 
     <void property="maxValue"> 
      <double>1000.0</double> 
     </void> 
     <void property="name"> 
      <string>system.memory.used</string> 
     </void> 
     <void property="unit"> 
      <object idref="Unit8"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter17"/> 
   </void> 
   <void method="add"> 
    <object id="CliParameter21" class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>Lighting</string> 
     </void> 
     <void property="name"> 
      <string>local.lighting</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>test.an1</string> 
     </void> 
     <void property="name"> 
      <string>test.an1</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>local.temperature</string> 
     </void> 
     <void property="name"> 
      <string>local.temperature</string> 
     </void> 
     <void property="unit"> 
      <object idref="Unit2"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter1"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter7"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter8"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter6"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>Generator run</string> 
     </void> 
     <void property="maxValue"> 
      <double>1.0</double> 
     </void> 
     <void property="name"> 
      <string>generator.monitor.run</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliParameter22" class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>camera.115</string> 
     </void> 
     <void property="name"> 
      <string>camera.115</string> 
     </void> 
     <void property="type"> 
      <object idref="CliParameter$Type0"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="CliParameter23" class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>Camera 116</string> 
     </void> 
     <void property="name"> 
      <string>camera.116</string> 
     </void> 
     <void property="type"> 
      <object idref="CliParameter$Type0"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter16"/> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter15"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>test.const.100</string> 
     </void> 
     <void property="name"> 
      <string>test.const.100</string> 
     </void> 
     <void property="unit"> 
      <object idref="Unit1"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter13"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliParameter"> 
     <void property="legend"> 
      <string>consumpt</string> 
     </void> 
     <void property="name"> 
      <string>test.cons</string> 
     </void> 
     <void property="unit"> 
      <object idref="Unit1"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliParameter14"/> 
   </void> 
  </void> 
  <void property="rightsItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliRight"> 
     <void property="name"> 
      <string>Ship Control</string> 
     </void> 
     <void property="targetClassName"> 
      <null/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliRight"> 
     <void property="name"> 
      <string>Parameters setup</string> 
     </void> 
     <void property="targetClassName"> 
      <null/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliRight"> 
     <void property="name"> 
      <string>Users control</string> 
     </void> 
     <void property="targetClassName"> 
      <null/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliRight"> 
     <void property="name"> 
      <string>Groups control</string> 
     </void> 
     <void property="targetClassName"> 
      <null/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliRight1"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight2"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight3"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight4"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight5"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight6"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight7"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight8"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight9"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight10"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight11"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight12"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight13"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight14"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight15"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight16"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight17"/> 
   </void> 
   <void method="add"> 
    <object idref="CliRight18"/> 
   </void> 
  </void> 
  <void property="systemDriverItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliSystemDriver"> 
     <void property="driver"> 
      <object class="ru.dz.shipMaster.dev.system.DataPump"> 
       <void property="interval"> 
        <long>100</long> 
       </void> 
       <void property="ports"> 
        <void index="0"> 
         <void property="convertor"> 
          <object idref="CliConversion1"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter18"/> 
         </void> 
        </void> 
        <void index="1"> 
         <void property="convertor"> 
          <object idref="CliConversion2"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter20"/> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="convertor"> 
          <object idref="CliConversion2"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter19"/> 
         </void> 
        </void> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliSystemDriver"> 
     <void property="driver"> 
      <object class="ru.dz.shipMaster.dev.system.Tray"> 
       <void property="firstMessageTitle"> 
        <string>CPU load</string> 
       </void> 
       <void property="ports"> 
        <void index="0"> 
         <void property="givenName"> 
          <string>CPU load</string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter18"/> 
         </void> 
        </void> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliSystemDriver"> 
     <void property="driver"> 
      <object class="ru.dz.shipMaster.dev.system.ScreenDimmer"> 
       <void property="ports"> 
        <void index="0"> 
         <void property="convertor"> 
          <object idref="CliConversion3"/> 
         </void> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter21"/> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="givenName"> 
          <string></string> 
         </void> 
         <void property="target"> 
          <object idref="CliParameter14"/> 
         </void> 
        </void> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="unitGroupItems"> 
   <void method="add"> 
    <object id="UnitGroup6" class="ru.dz.shipMaster.data.units.UnitGroup"> 
     <void property="imperialDefault"> 
      <object id="Unit9" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="group"> 
        <object idref="UnitGroup6"/> 
       </void> 
       <void property="longName"> 
        <string>Meters</string> 
       </void> 
       <void property="name"> 
        <string>m</string> 
       </void> 
      </object> 
     </void> 
     <void property="metricDefault"> 
      <object idref="Unit9"/> 
     </void> 
     <void property="reference"> 
      <object idref="Unit9"/> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object id="UnitGroup7" class="ru.dz.shipMaster.data.units.UnitGroup"> 
     <void property="imperialDefault"> 
      <object id="Unit10" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="firstMultiplicator"> 
        <double>2.777778E-4</double> 
       </void> 
       <void property="group"> 
        <object idref="UnitGroup7"/> 
       </void> 
       <void property="longName"> 
        <string>Hours</string> 
       </void> 
       <void property="name"> 
        <string>hr</string> 
       </void> 
      </object> 
     </void> 
     <void property="metricDefault"> 
      <object idref="Unit10"/> 
     </void> 
     <void property="reference"> 
      <object id="Unit11" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="group"> 
        <object idref="UnitGroup7"/> 
       </void> 
       <void property="longName"> 
        <string>Seconds</string> 
       </void> 
       <void property="name"> 
        <string>sec</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="UnitGroup4"/> 
   </void> 
   <void method="add"> 
    <object id="UnitGroup8" class="ru.dz.shipMaster.data.units.UnitGroup"> 
     <void property="imperialDefault"> 
      <object id="Unit12" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="firstMultiplicator"> 
        <double>1024.0</double> 
       </void> 
       <void property="group"> 
        <object idref="UnitGroup8"/> 
       </void> 
       <void property="longName"> 
        <string>Kilobytes</string> 
       </void> 
       <void property="name"> 
        <string>Kb</string> 
       </void> 
      </object> 
     </void> 
     <void property="metricDefault"> 
      <object idref="Unit12"/> 
     </void> 
     <void property="reference"> 
      <object id="Unit13" class="ru.dz.shipMaster.data.units.Unit"> 
       <void property="group"> 
        <object idref="UnitGroup8"/> 
       </void> 
       <void property="longName"> 
        <string>Bytes</string> 
       </void> 
       <void property="name"> 
        <string>b</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="UnitGroup2"/> 
   </void> 
   <void method="add"> 
    <object idref="UnitGroup5"/> 
   </void> 
   <void method="add"> 
    <object idref="UnitGroup0"/> 
   </void> 
   <void method="add"> 
    <object idref="UnitGroup3"/> 
   </void> 
   <void method="add"> 
    <object idref="UnitGroup1"/> 
   </void> 
  </void> 
  <void property="unitItems"> 
   <void method="add"> 
    <object idref="Unit9"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.data.units.Unit"> 
     <void property="firstMultiplicator"> 
      <double>0.01</double> 
     </void> 
     <void property="group"> 
      <object idref="UnitGroup6"/> 
     </void> 
     <void property="longName"> 
      <string>Centimeters</string> 
     </void> 
     <void property="name"> 
      <string>cm</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="Unit5"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit6"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit11"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit10"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit2"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit7"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit0"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit3"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit1"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit4"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit13"/> 
   </void> 
   <void method="add"> 
    <object idref="Unit12"/> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.data.units.Unit"> 
     <void property="firstMultiplicator"> 
      <double>1024.0</double> 
     </void> 
     <void property="group"> 
      <object idref="UnitGroup8"/> 
     </void> 
     <void property="longName"> 
      <string>Megabytes</string> 
     </void> 
     <void property="name"> 
      <string>Mb</string> 
     </void> 
     <void property="secondMultiplicator"> 
      <double>1024.0</double> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="unitLibItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.data.units.Unit"> 
     <void property="longName"> 
      <string>m/sec</string> 
     </void> 
     <void property="name"> 
      <string>m/sec</string> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="userItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliUser"> 
     <void property="groups"> 
      <void method="add"> 
       <object idref="CliGroup2"/> 
      </void> 
      <void method="add"> 
       <object idref="CliGroup1"/> 
      </void> 
     </void> 
     <void property="login"> 
      <string>dz</string> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object idref="CliUser0"/> 
   </void> 
  </void> 
  <void property="windowItems"> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliWindow"> 
     <void property="name"> 
      <string>main.engines</string> 
     </void> 
     <void property="panels"> 
      <void method="add"> 
       <object idref="CliInstrumentPanel3"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel0"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel1"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel2"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel5"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel6"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel7"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel4"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel9"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel8"/> 
      </void> 
     </void> 
     <void property="structure"> 
      <object id="CliWindowStructure0" class="ru.dz.shipMaster.config.items.CliWindowStructure"> 
       <void property="constraints"> 
        <void index="0"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>2</int> 
          </void> 
          <void property="gridwidth"> 
           <int>8</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>0</int> 
          </void> 
          <void property="insets"> 
           <object class="java.awt.Insets"> 
            <int>0</int> 
            <int>0</int> 
            <int>2</int> 
            <int>0</int> 
           </object> 
          </void> 
          <void property="weighty"> 
           <double>0.0</double> 
          </void> 
         </void> 
        </void> 
        <void index="1"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridwidth"> 
           <int>4</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>1</int> 
          </void> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>2</int> 
          </void> 
         </void> 
        </void> 
        <void index="3"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridx"> 
           <int>3</int> 
          </void> 
          <void property="gridy"> 
           <int>2</int> 
          </void> 
         </void> 
        </void> 
        <void index="4"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridwidth"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>3</int> 
          </void> 
         </void> 
        </void> 
        <void index="5"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridwidth"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>2</int> 
          </void> 
          <void property="gridy"> 
           <int>3</int> 
          </void> 
         </void> 
        </void> 
        <void index="6"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridwidth"> 
           <int>4</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>4</int> 
          </void> 
         </void> 
        </void> 
        <void index="7"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridheight"> 
           <int>4</int> 
          </void> 
          <void property="gridwidth"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>6</int> 
          </void> 
          <void property="gridy"> 
           <int>1</int> 
          </void> 
          <void property="weightx"> 
           <double>0.0</double> 
          </void> 
         </void> 
        </void> 
        <void index="8"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridx"> 
           <int>1</int> 
          </void> 
          <void property="gridy"> 
           <int>2</int> 
          </void> 
         </void> 
        </void> 
        <void index="9"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridx"> 
           <int>2</int> 
          </void> 
          <void property="gridy"> 
           <int>2</int> 
          </void> 
         </void> 
        </void> 
       </void> 
       <void property="name"> 
        <string>Engines</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliWindow"> 
     <void property="name"> 
      <string>big.engines</string> 
     </void> 
     <void property="panels"> 
      <void method="add"> 
       <object idref="CliInstrumentPanel10"/> 
      </void> 
      <void method="add"> 
       <object idref="CliInstrumentPanel11"/> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
        <void property="instruments"> 
         <void method="add"> 
          <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
           <void property="component"> 
            <object class="ru.dz.shipMaster.ui.meter.CameraImage"> 
             <void property="current"> 
              <double>49.61242398780008</double> 
             </void> 
             <void property="legend"> 
              <string>Temperature</string> 
             </void> 
             <void property="maximum"> 
              <double>100.0</double> 
             </void> 
             <void property="units"> 
              <string>°C</string> 
             </void> 
            </object> 
           </void> 
           <void property="instrumentClassName"> 
            <string>ru.dz.shipMaster.ui.meter.CameraImage</string> 
           </void> 
           <void property="name"> 
            <string>Camera 115</string> 
           </void> 
           <void property="offMessage"> 
            <string></string> 
           </void> 
           <void property="onMessage"> 
            <string></string> 
           </void> 
           <void property="parameter"> 
            <object idref="CliParameter22"/> 
           </void> 
          </object> 
         </void> 
        </void> 
        <void property="name"> 
         <string>Camera A</string> 
        </void> 
       </object> 
      </void> 
      <void method="add"> 
       <object class="ru.dz.shipMaster.config.items.CliInstrumentPanel"> 
        <void property="instruments"> 
         <void method="add"> 
          <object class="ru.dz.shipMaster.config.items.CliInstrument"> 
           <void property="component"> 
            <object class="ru.dz.shipMaster.ui.meter.CameraImage"> 
             <void property="current"> 
              <double>52.0</double> 
             </void> 
             <void property="legend"> 
              <string>Temperature</string> 
             </void> 
             <void property="maximum"> 
              <double>100.0</double> 
             </void> 
             <void property="units"> 
              <string>°C</string> 
             </void> 
            </object> 
           </void> 
           <void property="instrumentClassName"> 
            <string>ru.dz.shipMaster.ui.meter.CameraImage</string> 
           </void> 
           <void property="name"> 
            <string>Camera 116</string> 
           </void> 
           <void property="offMessage"> 
            <string></string> 
           </void> 
           <void property="onMessage"> 
            <string></string> 
           </void> 
           <void property="parameter"> 
            <object idref="CliParameter23"/> 
           </void> 
          </object> 
         </void> 
        </void> 
        <void property="name"> 
         <string>Camera B</string> 
        </void> 
       </object> 
      </void> 
     </void> 
     <void property="screenNumber"> 
      <int>1</int> 
     </void> 
     <void property="structure"> 
      <object id="CliWindowStructure1" class="ru.dz.shipMaster.config.items.CliWindowStructure"> 
       <void property="constraints"> 
        <void index="0"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridwidth"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>0</int> 
          </void> 
         </void> 
        </void> 
        <void index="1"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridwidth"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>2</int> 
          </void> 
          <void property="gridy"> 
           <int>0</int> 
          </void> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>1</int> 
          </void> 
         </void> 
        </void> 
        <void index="3"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>1</int> 
          </void> 
          <void property="gridy"> 
           <int>1</int> 
          </void> 
         </void> 
        </void> 
        <void index="4"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>2</int> 
          </void> 
          <void property="gridy"> 
           <int>1</int> 
          </void> 
         </void> 
        </void> 
        <void index="5"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>2</int> 
          </void> 
          <void property="gridx"> 
           <int>3</int> 
          </void> 
          <void property="gridy"> 
           <int>1</int> 
          </void> 
         </void> 
        </void> 
        <void index="6"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="7"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="8"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="9"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
       </void> 
       <void property="name"> 
        <string>Second mon</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
   <void method="add"> 
    <object class="ru.dz.shipMaster.config.items.CliWindow"> 
     <void property="autostart"> 
      <boolean>false</boolean> 
     </void> 
     <void property="name"> 
      <string>Home.Main</string> 
     </void> 
     <void property="panels"> 
      <void method="add"> 
       <object idref="CliInstrumentPanel12"/> 
      </void> 
     </void> 
     <void property="structure"> 
      <object id="CliWindowStructure2" class="ru.dz.shipMaster.config.items.CliWindowStructure"> 
       <void property="constraints"> 
        <void index="0"> 
         <void property="constraints"> 
          <void property="fill"> 
           <int>1</int> 
          </void> 
          <void property="gridx"> 
           <int>0</int> 
          </void> 
          <void property="gridy"> 
           <int>0</int> 
          </void> 
         </void> 
        </void> 
        <void index="1"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="2"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="3"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="4"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="5"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="6"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="7"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="8"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
        <void index="9"> 
         <void property="visible"> 
          <boolean>false</boolean> 
         </void> 
        </void> 
       </void> 
       <void property="name"> 
        <string>OneBigPanel</string> 
       </void> 
      </object> 
     </void> 
    </object> 
   </void> 
  </void> 
  <void property="windowStructureItems"> 
   <void method="add"> 
    <object idref="CliWindowStructure0"/> 
   </void> 
   <void method="add"> 
    <object idref="CliWindowStructure1"/> 
   </void> 
   <void method="add"> 
    <object idref="CliWindowStructure2"/> 
   </void> 
  </void> 
 </object> 
</java> 
