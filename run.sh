#!/bin/sh


Exargs="-javaagent:"`find .. -name "aspectjweaver*.jar" | head -1`

java $Exargs -classpath ../conf:`find .. -name "*.jar" -exec echo -n {}: \;` com.weibo.httpserver.HTTPServerLike
