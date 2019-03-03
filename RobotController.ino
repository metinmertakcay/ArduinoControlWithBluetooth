#include <Servo.h>
Servo myservo;

int input = 0;
String string;
char command;

void setup() {
	myservo.attach(3);
	pinMode(A0, OUTPUT);
	pinMode(A1, OUTPUT);
	pinMode(A2, OUTPUT);
	pinMode(A3, OUTPUT);
	
	Serial.begin(9600);
}

void loop() {
	if(Serial.available()){
		string = "";
    }
	
	while(Serial.available()){
		if (Serial.available() > 0) {
            char c = Serial.read();
            string += c;
        }
		delay(3)
    }
	
	int index = string.indexOf(' ');
    String num = string.substring(0, index);
    input = num.toInt();
	
	if(input>=10 && input<70){
		myservo.write(input);
		delay(15)
	} else if (input == 242){
		digitalWrite(A0,HIGH);
		digitalWrite(A1,LOW);
		digitalWrite(A2,LOW);
		digitalWrite(A3,HIGH);
	} else if(input == 241) { //back
		digitalWrite(A0,LOW);
		digitalWrite(A1,HIGH);
		digitalWrite(A2,HIGH);
		digitalWrite(A3,LOW);
	} else if(input == 244){ //right
		digitalWrite(A0,HIGH);
		digitalWrite(A1,LOW);
		digitalWrite(A2,HIGH);
		digitalWrite(A3,LOW);
	} else if(input == 243){ //left 
		digitalWrite(A0,LOW);
		digitalWrite(A1,HIGH);
		digitalWrite(A2,LOW);
		digitalWrite(A3,HIGH);
	} else if(input == 200){ //default state
		digitalWrite(A0,LOW);
		digitalWrite(A1,LOW);
		digitalWrite(A2,LOW);
		digitalWrite(A3,LOW);
	} 
}