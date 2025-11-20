package no.fintlabs.fintadapterresourcevams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
@ComponentScan(basePackages = ["no.fintlabs.adapter"])
class FintAdapterResourceVamsApplication

fun main(args: Array<String>) {
    runApplication<FintAdapterResourceVamsApplication>(*args)
}
