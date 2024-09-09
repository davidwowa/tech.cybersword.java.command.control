#!/bin/sh

sudo iptables -A INPUT -p tcp --dport 1337 -j ACCEPT
sudo -u game java -jar /home/game/tech.cybersword.java.command.control-1.0-SNAPSHOT.jar &