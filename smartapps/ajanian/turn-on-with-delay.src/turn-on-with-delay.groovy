/**
 *  Turn On With Delay
 *
 *  Copyright 2017 Andrew Janian
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Turn On With Delay",
    namespace: "ajanian",
    author: "Andrew Janian",
    description: "Turn on a device after X minutes have passed",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Switch To Watch") {
    	input "theSwitchToWatch", "capability.switch", require: true
    }
    section("Switch To Turn On") {
    	input "theSwitchToTurnOn", "capability.switch", require: true
    }
    section("Delay") {
    	input "delayMinutes", "number", require: true
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	log.debug "Subscribing to events for ${theSwitchToWatch}"
	subscribe(theSwitchToWatch, "switch.on", switchOnHandler)
    log.debug "Subscribed to events for ${theSwitchToWatch}"
}

def switchOnHandler(evt) {
	log.debug "Handling on event for ${theSwitchToWatch}"
	runIn(delayMinutes * 60, turnOn, [overwrite: true])
    log.debug "Set to runIn in ${delayMinutes * 60} seconds"
}

def turnOn() {
	log.debug "turnOn() called"
    log.debug theSwitchToWatch.currentValue("switch")
    if(theSwitchToWatch.currentValue("switch") == "on") {
    	log.debug "Turning on ${theSwitchToTurnOn}"
    	theSwitchToTurnOn.on()
        log.debug "Turned on ${theSwitchToTurnOn}"
    } else {
    	log.debug "Not turning on ${theSwitchToTurnOn} because ${theSwitchToWatch} is not on: ${theSwitchToWatch.currentValue("switch")}"
    }
    theSwitchToWatch.off()
    log.debug "turnOn() complete"
}