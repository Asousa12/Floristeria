/*plugins {
    id("java")
}

group = "com.ifruit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")

    implementation("org.mongodb:bson:5.0.0")
    implementation("org.mongodb:mongodb-driver-core:5.0.0")
    implementation("org.mongodb:mongodb-driver-sync:5.0.0")
    implementation("org.mongodb:mongodb-driver-legacy:5.0.0")

//implementation("com.zaxxer:HikariCP:5.1.0")
    //implementation("org.slf4j:slf4j-api:2.0.12")
    //implementation("ch.qos.logback:logback-classic:1.5.3")
}

tasks.test {
    useJUnitPlatform()
}*/
plugins {
    id("java")
}

group = "com.ifruit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("mysql:mysql-connector-java:8.0.28")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation ("org.mongodb:mongodb-driver-sync:4.11.1")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.6")
}

tasks.test {
    useJUnitPlatform()
}